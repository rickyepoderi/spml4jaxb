/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.sim;

import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>This client is an extension of the library SOAPClient to communicate
 * with the Waveset Identity Manager (old Sun Identity software) SPMLv2 server.
 * This server extends the listTargets requests and can send a username and a 
 * password. The response for that request includes another extended attribute
 * which represents a session. This session identifier should sent in the
 * next request. After every request a new session identifier is updated.
 * This server just supports the DSML profile.</p>
 * 
 * @author ricky
 */
public class SIMClient extends SOAPClient {

    /**
     * Operational name for the accountId (Username) attribute.
     */
    static public final String OP_ACCOUNT_ID = "accountId";
    
    /**
     * Operational name for the password attribute.
     */
    static public final String OP_PASSWORD = "password";
    
    /**
     * Operational name for the session attribute.
     */
    static public final String OP_SESSION = "session";
    
    /**
     * The session that is maintained and updated after every response.
     */
    private String session = null;
    
    /**
     * Constructor using the URL of the Waveset server.
     * 
     * @param url The URL to connect
     * @param username The username to use
     * @param password The password to use
     * @throws SpmlException Some error conneting to the server
     */
    public SIMClient(String url, String username, String password) throws SpmlException {
        super(url, true, es.rickyepoderi.spml4jaxb.test.sim.ObjectFactory.class);
        this.login(username, password);
    }
    
    /**
     * Method that sends the listTargets request with the username and
     * password and recovers the inistial session id.
     * 
     * @param username The username to use
     * @param password The password to use
     * @return The listTargets response accessor
     * @throws SpmlException Some error in the login
     */
    public final ListTargetsResponseAccessor login(String username, String password) throws SpmlException {
        OperationalNameValuePair user = new OperationalNameValuePair();
        user.setName(OP_ACCOUNT_ID);
        user.setValue(username);
        OperationalNameValuePair pass = new OperationalNameValuePair();
        pass.setName(OP_PASSWORD);
        pass.setValue(password);
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .operationalObject(user)
                .operationalObject(pass)
                .send(this);
        if (!ltra.isSuccess()) {
            throw new SpmlException(ltra.getErrorMessagesInOne());
        }
        return ltra;
    }
    
    /**
     * Method that updates the session received in every response.
     * 
     * @param res The response received
     */
    protected final void refreshSession(ResponseAccessor res) {
        // get the session attribute
        OperationalNameValuePair[] ops = res.getOperationalObjects(OperationalNameValuePair.class);
        for (OperationalNameValuePair op: ops) {
            if (OP_SESSION.equals(op.getName())) {
                this.session = op.getValue();
                break;
            }
        }
    } 
    
    /**
     * The send method for SOAP is overriden to include the session id
     * in every request.
     * 
     * @param request The request to send
     * @return The response accessor
     * @throws SpmlException Some error
     */
    @Override
    public ResponseAccessor send(Object request) throws SpmlException {
        if (this.session != null) {
            // add the session operational attribute
            RequestType req = null;
            if (request instanceof RequestType) {
                req = (RequestType) request;
            } else if (request instanceof JAXBElement && ((JAXBElement) request).getValue() instanceof RequestType) {
                req = (RequestType) ((JAXBElement) request).getValue();
            }
            if (req != null) {
                OperationalNameValuePair ses = new OperationalNameValuePair();
                ses.setName(OP_SESSION);
                ses.setValue(session);
                req.getAny().add(ses);
            }
        }
        ResponseAccessor res = super.send(request);
        refreshSession(res);
        return res;
    }
    
}
