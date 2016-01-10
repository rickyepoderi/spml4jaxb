/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.sim;

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.DeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * <p>Test to run against a real DSML SPML server implementation in
 * Oracle Waveset Identity Manager (SIM - Sun Identity Manager because
 * it is the software that comes from Sun Microsystems).</p>
 * 
 * @author ricky
 */
public class SIMTest {
    
    static private SIMClient client = null;
    
    public SIMTest() {
        // noop
    }
    
    public ResponseAccessor waitUntilExecuted(String requestId, boolean returnResults) throws SpmlException {
        boolean executed = false;
        StatusResponseAccessor sra = null;
        while (!executed) {
            try {
                Thread.sleep(500L);
            } catch(InterruptedException e) {}
            sra = RequestBuilder.builderForStatus()
                    .requestId()
                    .asyncRequestId(requestId)
                    .returnResults(returnResults)
                    .send(client)
                    .asStatus();
            executed = !sra.getNestedResponse().isPending();
        }
        Assert.assertTrue(sra.isSuccess());
        Assert.assertEquals(sra.getAsyncRequestId(), requestId);
        ResponseAccessor res = sra.getNestedResponse();
        Assert.assertEquals(res.getRequestId(), requestId);
        return res;
    }
    
    @Test
    public void testAsync() throws SpmlException {
        // add a simple user
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .asynchronous()
                .requestId()
                .dsmlAttribute("objectclass", "spml2Person")
                .dsmlAttribute("accountId", "testuser")
                .dsmlAttribute("firstname", "testuser")
                .dsmlAttribute("lastname", "testuser")
                .dsmlAttribute("credentials", "testuser")
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isPending());
        ara = waitUntilExecuted(ara.getRequestId(), true).asAdd();
        Assert.assertTrue(ara.isSuccess());
        Map<String, DsmlAttr> map = ara.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser");
        // look for the user
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .asynchronous()
                .requestId()
                .returnData()
                .psoId("testuser")
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isPending());
        lra = waitUntilExecuted(lra.getRequestId(), true).asLookup();
        Assert.assertTrue(lra.isSuccess());
        map = lra.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser");
        // modify the user
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .asynchronous()
                .requestId()
                .psoId("testuser")
                .returnData()
                .dsmlReplace("firstname", "testuser2")
                .dsmlReplace("lastname", "testuser2")
                .send(client)
                .asModify();
        Assert.assertTrue(mra.isPending());
        mra = waitUntilExecuted(mra.getRequestId(), true).asModify();
        Assert.assertTrue(mra.isSuccess());
        map = mra.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser2");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser2");
        // look for the user again
        lra = RequestBuilder.builderForLookup()
                .asynchronous()
                .requestId()
                .returnData()
                .psoId("testuser")
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isPending());
        lra = waitUntilExecuted(lra.getRequestId(), true).asLookup();
        map = lra.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser2");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser2");
        // delete the user
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .asynchronous()
                .requestId()
                .psoId("testuser")
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isPending());
        dra = waitUntilExecuted(dra.getRequestId(), true).asDelete();
        Assert.assertTrue(dra.isSuccess());
    }
    
    @Test
    public void testSync() throws SpmlException {
        // add a simple user
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .dsmlAttribute("objectclass", "spml2Person")
                .dsmlAttribute("accountId", "testuser")
                .dsmlAttribute("firstname", "testuser")
                .dsmlAttribute("lastname", "testuser")
                .dsmlAttribute("credentials", "testuser")
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        Map<String, DsmlAttr> map = ara.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser");
        // look for the user
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .returnData()
                .psoId("testuser")
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isSuccess());
        map = lra.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser");
        // modify the user
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .synchronous()
                .requestId()
                .psoId("testuser")
                .returnData()
                .dsmlReplace("firstname", "testuser2")
                .dsmlReplace("lastname", "testuser2")
                .send(client)
                .asModify();
        Assert.assertTrue(mra.isSuccess());
        map = mra.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser2");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser2");
        // look for the user again
        lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .returnData()
                .psoId("testuser")
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isSuccess());
        map = lra.getDsmlAttributesMap();
        Assert.assertEquals(map.get("accountId").getValue().get(0), "testuser");
        Assert.assertEquals(map.get("firstname").getValue().get(0), "testuser2");
        Assert.assertEquals(map.get("lastname").getValue().get(0), "testuser2");
        // delete the user
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId("testuser")
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        client = new SIMClient("http://192.168.122.11:8080/idm/servlet/openspml2", "configurator", "configurator");
    }
}
