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
package es.rickyepoderi.spml4jaxb.client;

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;

/**
 * <p>Simple class that uses SOAP to connect to the server. This class has no
 * methods to do a wsse security login (although it means necessary because 
 * SPMLv2 standard comments to use it to login).</p>
 * 
 * @author ricky
 */
public class SOAPClient extends SpmlClient {
    
    /**
     * logger for the class.
     */
    protected static final Logger log = Logger.getLogger(SOAPClient.class.getName());
    
    /**
     * SOAP message factory.
     */
    private MessageFactory mf = null;
    
    /**
     * SOAP connection.
     */
    private SOAPConnection conn = null;
    
    /**
     * Server URL to connect to.
     */
    private URL url;
    
    /**
     * Initialize the SOAP factory and connection to reuse it.
     * 
     * @param url The URL of the server to connect
     * @throws SpmlException Some error creating the client
     */
    private void init(String url) throws SpmlException {
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            conn = soapConnectionFactory.createConnection();
            mf = MessageFactory.newInstance();
            this.url = new URL(url);
        } catch (SOAPException|MalformedURLException e) {
            throw new SpmlException(e);
        }
    }
    
    /**
     * Complex constructor that lets define the factories to be used.
     * 
     * @param url The URL of the server to connect
     * @param appendFactories if true the factories are appended to the default ones, 
     *               if false just the passed as argument are used
     * @param factories The factories to use in JAXB (appended or alone)
     * @throws SpmlException 
     */
    public SOAPClient(String url, boolean appendFactories, Class... factories) throws SpmlException {
        super(appendFactories, factories);
        init(url);
    }
    
    /**
     * Constructor using default SPMLv2 factories and URL.
     * 
     * @param url The URL of the server to connect
     * @throws SpmlException Some error creating the client
     */
    public SOAPClient(String url) throws SpmlException {
        super();
        init(url);
    }
    
    /**
     * Method to create the SOAP message before sending to the URL. The method
     * uses JAXB in order to create the internal body with the SPMLv2 message.
     * 
     * @param request The request to send
     * @return The soap message created with that request
     * @throws SpmlException Some error creating the message
     */
    protected SOAPMessage createSoapMessage(Object request) throws SpmlException {
        try {
            SOAPMessage message = mf.createMessage();
            SOAPBody reqBody = message.getSOAPBody();
            // create the marshaller for the SPML request
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(request, reqBody);
            if (log.isLoggable(Level.FINE)) {
                StringWriter sw = new StringWriter();
                log.log(Level.FINE, "REQUEST: ");
                marshaller.marshal(request, sw);
                log.log(Level.FINE, sw.toString());
            }
            return message;
        } catch (JAXBException | SOAPException e) {
            throw new SpmlException(e);
        }
    }
    
    /**
     * The send method in SOAP creates a SOAP message and set the SPMLv2
     * request as the SOAP body. As it is said before no wsse is used
     * right now (although it is recommended in the standard). The SOAP
     * message is sent through the connection and the response is
     * received. The body of the SOAP response is the SPMLv2 response
     * which is used to construct a generic accessor.
     * 
     * @param request The SPML request type to send (JAXB)
     * @return The response received as an accessor
     * @throws SpmlException Some error sending the request or receiving the 
     *         response
     */
    @Override
    public ResponseAccessor send(Object request) throws SpmlException {
        try {
            // create the message
            SOAPMessage message = this.createSoapMessage(request);
            // save changes and send the message
            message.saveChanges();
            SOAPMessage soapResponse = conn.call(message, url);
            SOAPBody resBody = soapResponse.getSOAPBody();
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            JAXBElement<ResponseType> el = (JAXBElement<ResponseType>) unmarshaller.unmarshal(
                    new DOMSource(resBody.extractContentAsDocument()));
            if (log.isLoggable(Level.FINE)) {
                StringWriter sw = new StringWriter();
                log.log(Level.FINE, "RESPONSE: ");
                Marshaller marshaller = ctx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(el, sw);
                log.log(Level.FINE, sw.toString());
            }
            return BaseResponseAccessor.accessorForResponse(el.getValue());
        } catch (SOAPException|JAXBException e) {
            throw new SpmlException(e);
        }
    }
    
    /**
     * Close SOAP connection.
     * 
     * @throws IOException Some error
     */
    @Override
    public void close() throws IOException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SOAPException e) {
            throw new IOException(e);
        }
    }
    
}
