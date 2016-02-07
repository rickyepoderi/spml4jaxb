/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.client;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;

/**
 * <p>Client extension that send the SOAP message but adding the HTTP basic
 * authorization header. The header is just added with the username
 * and password encoded in Base64 (sun.misc.BASE64Encoder).</p>
 * 
 * @author ricky
 */
public class BasicAuthSOAPClient extends SOAPClient {

    /**
     * The base 64 authorization header.
     */
    private String authorization = null;
    
    /**
     * Constructor for the client giving all the parameter and factories.
     * 
     * @param url The server url to connect to
     * @param username The username to use in the basic authentication
     * @param password The password to use in the basic authentication
     * @param appendFactories if true the factories are appended to the default ones, 
     *                        if false just the passed as argument are used
     * @param factories The factories to use in JAXB (appended or alone)
     * @throws SpmlException Some error creating the client
     */
    public BasicAuthSOAPClient(String url, String username, String password,
            boolean appendFactories, Class... factories) throws SpmlException {
        super(url, appendFactories, factories);
        authorization = "Basic " + new sun.misc.BASE64Encoder().encode(
                (username + ":" + password).getBytes());
    }
    
    /**
     * Constructor using default SPMLv2 factories and URL.
     * 
     * @param url The URL of the server to connect
     * @param username The username to use in the basic authentication
     * @param password The password to use in the basic authentication
     * @throws SpmlException Some error creating the client
     */
    public BasicAuthSOAPClient(String url, String username, String password) throws SpmlException {
        super(url);
        authorization = new sun.misc.BASE64Encoder().encode(
                (username + ":" + password).getBytes());
    }
    
    /**
     * Creates the soap message but adds an HTTP header with the
     * authorization for the basic auth.
     * @param request The request to send
     * @return The SOAP message to send with the authorization header
     * @throws SpmlException Some error creating the message
     */
    @Override
    protected SOAPMessage createSoapMessage(Object request) throws SpmlException {
        SOAPMessage message = super.createSoapMessage(request);
        MimeHeaders hd = message.getMimeHeaders();
        hd.addHeader("Authorization", authorization);
        return message;
    }
}