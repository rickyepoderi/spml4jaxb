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
 * es.rickyepoderi.spml4jaxb.SpmlMapperExecutorFactory
 * es.rickyepoderi.spml4jaxb.ObjectFactoryClasses
 * 
 * @author ricky
 */
public class ServletImpl extends HttpServlet {

    static public final String SPML_MAPPER_FACTORY_PROP = "es.rickyepoderi.spml4jaxb.SpmlMapperExecutorFactory";
    static public final String OBJECT_FACTORY_CLASSES_PROP = "es.rickyepoderi.spml4jaxb.ObjectFactoryClasses";
    
    protected JAXBContext ctx = null;
    protected Map<Class, SpmlExecutor> mapper = null;
    protected MessageFactory factory = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            System.err.println("Starting...");
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
            System.err.println("Starting...");
        } catch (JAXBException|SOAPException|ClassNotFoundException|InstantiationException|IllegalAccessException|SpmlException e) {
            throw new ServletException(e);
        }
    }

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
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
