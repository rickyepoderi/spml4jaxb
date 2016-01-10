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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *
 * @author ricky
 */
public class HandlerImpl implements HttpHandler {
    
    protected static final Logger log = Logger.getLogger(HandlerImpl.class.getName());
    
    protected JAXBContext ctx = null;
    protected Map<Class, SpmlExecutor> mapper = null;
    protected MessageFactory factory = null;
    
    public HandlerImpl(SpmlMapperExecutorFactory fact) throws SpmlException {
        this(fact, true);
    }
    
    public HandlerImpl(SpmlMapperExecutorFactory fact, boolean append, Class... factories) throws SpmlException {
        try {
            this.factory = MessageFactory.newInstance();
            if (append) {
                Set<Class> facts = new HashSet();
                facts.addAll(Arrays.asList(RequestBuilder.getAllSpmlv2ObjectFactoryClasses()));
                facts.addAll(Arrays.asList(factories));
                ctx = JAXBContext.newInstance(facts.toArray(new Class[0]));
            } else {
                ctx = JAXBContext.newInstance(factories);
            }
            this.mapper = fact.createMapper(ctx);
        } catch (JAXBException | SOAPException e) {
            throw new SpmlException(e);
        }
    }
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            if ("post".equalsIgnoreCase(he.getRequestMethod())) {
                // read the SOAP message
                InputStream is = he.getRequestBody();
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
                    responseBuilder.send(new HttpExchangeResponder(he, ctx, factory));
                } else {
                    throw new IOException("Operation not supported.");
                }
            } else {
                throw new IOException("Only POST is accepted.");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error handling the request", e);
            throw e;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error handling the request", e);
            throw new IOException(e);
        }
    }
    
}
