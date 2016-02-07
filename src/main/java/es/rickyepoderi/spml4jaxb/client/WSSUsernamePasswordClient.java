/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.client;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.DOMException;

/**
 * <p>Client that extends the SOAPCLient to include WSSE security with
 * simple username and password token. This is used by some server
 * implementations like the OIM.</p>
 * 
 * @author ricky
 */
public class WSSUsernamePasswordClient extends SOAPClient {

    /**
     * The username to use in the client.
     */
    protected String username = null;

    /**
     * The password to use in the client.
     */
    protected String password = null;
    
    /**
     * Constructor passing all the parameters to the method.
     * 
     * @param url The url of the SPMLv2 server
     * @param username The username to use in the WSEE header
     * @param password The password to use in the WSEE header
     * @param appendFactories if true the passed factories are appended to the
     *                        default ones, if false only the passed factories are used
     * @param factories The factories to use for JAXB objects (appended or alone)
     * @throws SpmlException Some error creating the client
     */
    public WSSUsernamePasswordClient(String url, String username, String password, 
            boolean appendFactories, Class... factories) throws SpmlException {
        super(url, appendFactories, factories);
        this.username = username;
        this.password = password;
    }
    
    /**
     * Constructor using default JAXB factories (the ones specified by the standard)
     * 
     * @param url The url of the SPMLv2 server
     * @param username The username to use in the WSEE header
     * @param password The password to use in the WSEE header
     * @throws SpmlException Some error creating the client
     */
    public WSSUsernamePasswordClient(String url, String username, String password) throws SpmlException {
        super(url);
        this.username = username;
        this.password = password;
    }

    /**
     * The method to create the SOAP message is overrided in order to add
     * the WSSE UsernameToken header. This token adds the Username and
     * Password elements as described in WSSE security standard (the default
     * username/password in clear) to the original (super) message.
     * 
     * @param request The request object to send
     * @return The SOAP message created ready to be send (it should be saved)
     * @throws SpmlException Some error creating the soap message
     */
    @Override
    protected SOAPMessage createSoapMessage(Object request) throws SpmlException {
        try {
            // create the message without any WSSE header
            SOAPMessage message = super.createSoapMessage(request);
            // now add the WSSE headers => simple username and password
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
            usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
            SOAPElement usernameEl = usernameToken.addChildElement("Username", "wsse");
            usernameEl.addTextNode(username);
            SOAPElement passwordEl = usernameToken.addChildElement("Password", "wsse");
            passwordEl.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
            passwordEl.addTextNode(password);
            return message;
        } catch (DOMException | SOAPException e) {
            throw new SpmlException(e);
        }
    }
    
}