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

import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
 *
 * @author ricky
 */
public class SOAPClient extends SpmlClient {
    
    private MessageFactory mf = null;
    private SOAPConnection conn = null;
    private URL url;
    
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
    
    public SOAPClient(String url, boolean appendFactories, Class... factories) throws SpmlException {
        super(appendFactories, factories);
        init(url);
    }
    
    public SOAPClient(String url) throws SpmlException {
        super();
        init(url);
    }
    
    @Override
    public ResponseAccessor send(Object request) throws SpmlException {
        try {
            // create the message
            SOAPMessage message = mf.createMessage();
            /*// add a user and password using WSS
            // https://www.oasis-open.org/committees/download.php/13392/wss-v1.1-spec-pr-UsernameTokenProfile-01.htm#_Toc104276211
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
            usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
            SOAPElement username = usernameToken.addChildElement("Username", "wsse");
            username.addTextNode("TestUser");
            SOAPElement password = usernameToken.addChildElement("Password", "wsse");
            password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
            password.addTextNode("TestPassword");*/
            // add the body
            SOAPBody reqBody = message.getSOAPBody();
            // create the marshaller for the SPML request
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(request, reqBody);
            System.err.println("REQUEST: ");
            marshaller.marshal(request, System.err);
            message.saveChanges();
            // send the message to the server
            SOAPMessage soapResponse = conn.call(message, url);
            SOAPBody resBody = soapResponse.getSOAPBody();
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            JAXBElement<ResponseType> el = (JAXBElement<ResponseType>) unmarshaller.unmarshal(
                    new DOMSource(resBody.extractContentAsDocument()));
            System.err.println("RESPONSE: ");
            marshaller.marshal(el, System.err);
            ResponseAccessor accessor = ResponseAccessor.accessorForResponse(el.getValue());
            return accessor;
        } catch (SOAPException|JAXBException e) {
            throw new SpmlException(e);
        }
    }
    
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
    
    static public void main(String[] args) throws Exception {
        try (SOAPClient client = new SOAPClient("http://localhost:8000/rpcrouter2")) {            
            
            System.err.println(RequestBuilder.builderForListTargets()
                    .synchronous()
                    .requestId()
                    .send(client)
                    .asListTargets());
            
            System.err.println(RequestBuilder.builderForAdd()
                    .synchronous()
                    .requestId()
                    .targetId("ddbb-spml-dsml")
                    .dsmlAttribute("uid", "ricky")
                    .dsmlAttribute("objectclass", "user")
                    .dsmlAttribute("password", "ricky")
                    .dsmlAttribute("cn", "Ricardo Martin")
                    .dsmlAttribute("description", "me")
                    .dsmlAttribute("role", "Admin", "User")
                    .send(client)
                    .asAdd());
            
            System.err.println(RequestBuilder.builderForLookup()
                    .synchronous()
                    .requestId()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-dsml")
                    .returnData()
                    .send(client)
                    .asLookup());
            
            System.err.println(RequestBuilder.builderForModify()
                    .synchronous()
                    .requestId()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-dsml")
                    .dsmlDelete("description", "me")
                    .dsmlReplace("password", "ricky123")
                    .dsmlDelete("role", "Admin")
                    .dsmlAdd("role", "Test")
                    .send(client)
                    .asModify());
            
            
            System.err.println(RequestBuilder.builderForLookup()
                    .synchronous()
                    .requestId()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-dsml")
                    .returnData()
                    .send(client)
                    .asLookup());
            
            System.err.println(RequestBuilder.builderForDelete()
                    .synchronous()
                    .requestId()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-dsml")
                    .send(client));
            
            System.err.println(RequestBuilder.builderForLookup()
                    .synchronous()
                    .requestId()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-dsml")
                    .returnData()
                    .send(client)
                    .asLookup());
        }
    }
}
