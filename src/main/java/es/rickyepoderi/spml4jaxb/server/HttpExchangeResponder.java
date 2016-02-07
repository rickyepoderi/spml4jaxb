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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * <p>SpmlResponder for the HTTP framework inside Java7. It just writes the 
 * response into the Exchange response output stream. This responder
 * is mainly used for the unit tests.</p>
 * 
 * @author ricky
 */
public class HttpExchangeResponder implements SpmlResponder {

    /**
     * The exchange used in Java7 HTTP server implementation.
     */
    private HttpExchange he = null;
    
    /**
     * The JAXB context to use.
     */
    private JAXBContext ctx = null;
    
    /**
     * The message factory to create SOAP messages.
     */
    private MessageFactory factory = null;

    /**
     * Constructor using the three elements.
     * 
     * @param he The http exchange object of the Java7 HTTP server framework
     * @param ctx The JAXB context to use
     * @param factory The SOAP factory to use
     */
    public HttpExchangeResponder(HttpExchange he, JAXBContext ctx, MessageFactory factory) {
        this.he = he;
        this.ctx = ctx;
        this.factory = factory;
    }

    /**
     * Method that writes the response as a SOAP response and the argument
     * is placed in the body. It writes it to the exchange object.
     * 
     * @param response The response to write
     * @throws SpmlException Some error
     */
    @Override
    public void write(Object response) throws SpmlException {
        try {
            SOAPMessage resMess = factory.createMessage();
            SOAPBody reqBody = resMess.getSOAPBody();
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.marshal(response, reqBody);
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    OutputStream os = he.getResponseBody()) {
                resMess.writeTo(bos);
                Headers responseHeaders = he.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/xml; charset=UTF-8");
                he.sendResponseHeaders(200, bos.size());
                os.write(bos.toByteArray());
            }
        } catch (JAXBException | SOAPException | IOException e) {
            throw new SpmlException(e);
        }
    }
}
