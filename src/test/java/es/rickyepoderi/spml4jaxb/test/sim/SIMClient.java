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
 *
 * @author ricky
 */
public class SIMClient extends SOAPClient {

    static public final String OP_ACCOUNT_ID = "accountId";
    static public final String OP_PASSWORD = "password";
    static public final String OP_SESSION = "session";
    
    private String session = null;
    
    public SIMClient(String url, String username, String password) throws SpmlException {
        super(url, true, es.rickyepoderi.spml4jaxb.test.sim.ObjectFactory.class);
        this.login(username, password);
    }
    
    public final ResponseAccessor login(String username, String password) throws SpmlException {
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
        return ltra.asUnknown();
    }
    
    protected final void refreshSession(ResponseAccessor res) {
        // get the session attribute
        OperationalNameValuePair[] ops = res.getOperationalObjects(OperationalNameValuePair.class);
        for (OperationalNameValuePair op: ops) {
            if (OP_SESSION.equals(op.getName())) {
                this.session = op.getValue();
            }
        }
    } 
    
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
