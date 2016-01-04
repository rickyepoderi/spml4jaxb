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

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 *
 * @author ricky
 */
public class HttpServletResponder implements SpmlResponder {

    private HttpServletResponse servletRes = null;
    private JAXBContext ctx = null;
    private MessageFactory factory = null;

    public HttpServletResponder(HttpServletResponse servletRes, JAXBContext ctx, MessageFactory factory) {
        this.servletRes = servletRes;
        this.ctx = ctx;
        this.factory = factory;
    }
    
    @Override
    public void write(Object response) throws SpmlException {
        try {
            SOAPMessage resMess = factory.createMessage();
            SOAPBody reqBody = resMess.getSOAPBody();
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.marshal(response, reqBody);
            try (OutputStream os = servletRes.getOutputStream()) {
                servletRes.setContentType("text/xml; charset=UTF-8");
                resMess.writeTo(os);
            }
        } catch (JAXBException | SOAPException | IOException e) {
            throw new SpmlException(e);
        }
    }
    
}
