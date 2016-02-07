/* 
 * Copyright (c) 2015 ricky <https://github.com/rickyepoderi/spml4jaxb>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.server;

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * <p>The servlet implementation is just a HttpServlet that uses the idea
 * of the SpmlExecutor and the SpmlMapperExecutorFactory to create a 
 * set of executors capable to deal with SPMLv2 requests.</p>
 * 
 * <p>The servlet implementation uses two config parameters to initialize
 * the servlet:</p>
 * 
 * <ul>
 * <li>es.rickyepoderi.spml4jaxb.SpmlMapperExecutorFactory:
 *     The factory that is going to create the set of executor the servlet 
 *     uses to answer SPMLv2 requests.</li>
 * <li>es.rickyepoderi.spml4jaxb.ObjectFactoryClasses:
 *     The factory classes to use in JAXB to marshall and unmarshall
 *     requests and responses. Comma separated list of classes.</li>
 * </ul>
 * 
 * @author ricky
 */
public class ServletImpl extends HttpServlet {

    /**
     * Logger for the class.
     */
    protected static final Logger log = Logger.getLogger(ServletImpl.class.getName());
    
    /**
     * Property to specify the mapper factory to use: es.rickyepoderi.spml4jaxb.SpmlMapperExecutorFactory
     */
    static public final String SPML_MAPPER_FACTORY_PROP = "es.rickyepoderi.spml4jaxb.SpmlMapperExecutorFactory";
    
    /**
     * Property to specify the the object factories (comma separated): es.rickyepoderi.spml4jaxb.ObjectFactoryClasses
     */
    static public final String OBJECT_FACTORY_CLASSES_PROP = "es.rickyepoderi.spml4jaxb.ObjectFactoryClasses";
    
    /**
     * The JAXBContext to use for JAXB.
     */
    protected JAXBContext ctx = null;
    
    /**
     * The mapper that joins request types and executors.
     */
    protected Map<Class<? extends RequestType>, SpmlExecutor> mapper = null;
    
    /**
     * The factory for SOAP messages.
     */
    protected MessageFactory factory = null;

    /**
     * The method initializes the servlets. The idea is reading the two properties
     * and constructing the JAXBContext and the mapper to attend the requests.
     * 
     * @param config The Servlet configuration as defined in the Servlet specification
     * @throws ServletException Some error initializing the servlet
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            this.factory = MessageFactory.newInstance();
            // create the JAXBContext
            String factoryClasses = config.getInitParameter(OBJECT_FACTORY_CLASSES_PROP);
            if (factoryClasses == null) {
                throw new ServletException(String.format("Object factory class property %s is compulsory", OBJECT_FACTORY_CLASSES_PROP));
            }
            List<Class> factoryList = new ArrayList<>();
            for (String clazz: factoryClasses.split(",")) {
                factoryList.add(Class.forName(clazz.trim()));
            }
            ctx = JAXBContext.newInstance(factoryList.toArray(new Class[0]));
            // create the mapper
            String mapperClass = config.getInitParameter(SPML_MAPPER_FACTORY_PROP);
            if (mapperClass == null) {
                throw new ServletException(String.format("Mapper class property %s is compulsory", SPML_MAPPER_FACTORY_PROP));
            }
            SpmlMapperExecutorFactory spmlFact = (SpmlMapperExecutorFactory) Class.forName(mapperClass).newInstance();
            this.mapper = spmlFact.createMapper(ctx);
        } catch (JAXBException|SOAPException|ClassNotFoundException|InstantiationException|IllegalAccessException|SpmlException e) {
            throw new ServletException(e);
        }
    }

    /**
     * The post method receives the request type and, depending the mapper,
     * the executor is used to create the response. If the mapper that not
     * maps a specified request a ServletException is thrown.
     * 
     * @param req The servlet request to attend (contains the SOAP request)
     * @param res The servlet response
     * @throws ServletException Some error
     * @throws IOException Some error
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            // read the SOAP message
            InputStream is = req.getInputStream();
            SOAPMessage reqMess = factory.createMessage(new MimeHeaders(), is);
            SOAPBody body = reqMess.getSOAPBody();
            // unmarshall the SPML message from the SOAP body and get the accessor
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            JAXBElement<RequestType> el = (JAXBElement<RequestType>) unmarshaller.unmarshal(body.extractContentAsDocument());
            RequestType request = el.getValue();
            RequestAccessor requestAccessor = BaseRequestAccessor.accessorForRequest(request);
            // get the executor for this class
            SpmlExecutor exec = mapper.get(requestAccessor.getRequestClass());
            if (exec != null) {
                // execute the request and the response builder
                ResponseBuilder responseBuilder = exec.execute(requestAccessor);
                // just send it using a responder
                responseBuilder.send(new HttpServletResponder(res, ctx, factory));
            } else {
                throw new IOException("Operation not supported.");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error handling the request", e);
            throw e;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error handling the request", e);
            throw new ServletException(e);
        }
    }
}
