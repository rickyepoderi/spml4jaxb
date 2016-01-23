/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.DeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.test.HttpServer;
import es.rickyepoderi.spml4jaxb.user.User;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class RealCommTest {
    
    static private HttpServer server = null;
    static private SOAPClient client = null;
    static private String targetXsdId = null;
    static private String targetDsmlId = null;

    public RealCommTest() {
        // noop
    }
    
    private User createSampleUser() {
        User u = new User();
        u.setUid("ricky");
        u.setCn("Ricardo Martin Camarero");
        u.setPassword("ricky123");
        u.setDescription("me");
        u.getRole().add("User");
        u.getRole().add("Admin");
        return u;
    }
    
    private void checkUser(User u, Map<String, DsmlAttr> attr) {
        Assert.assertEquals(u.getUid(), (attr.get("uid") == null) ? null : attr.get("uid").getValue().get(0));
        Assert.assertEquals(u.getCn(), (attr.get("cn") == null) ? null : attr.get("cn").getValue().get(0));
        Assert.assertEquals(u.getPassword(), (attr.get("password") == null) ? null : attr.get("password").getValue().get(0));
        Assert.assertEquals(u.getDescription(), (attr.get("description") == null) ? null : attr.get("description").getValue().get(0));
        Set<String> userRoles = new HashSet<>(u.getRole());
        Set<String> attrRoles = new HashSet<>();
        if (attr.get("role") != null) {
            attrRoles.addAll(attr.get("role").getValue());
        }
        Assert.assertEquals(userRoles, attrRoles);
    }
    
    public ResponseAccessor waitUntilExecuted(String requestId, boolean returnResults) throws SpmlException {
        boolean executed = false;
        StatusResponseAccessor sra = null;
        while (!executed) {
            sra = RequestBuilder.builderForStatus()
                    .requestId()
                    .asyncRequestId(requestId)
                    .returnResults(returnResults)
                    .send(client);
            executed = !sra.getNestedResponse().isPending();
        }
        Assert.assertTrue(sra.isSuccess());
        Assert.assertEquals(sra.getAsyncRequestId(), requestId);
        ResponseAccessor res = sra.getNestedResponse();
        Assert.assertEquals(res.getRequestId(), requestId);
        return res;
    }
    
    @Test
    public void testXsd() throws SpmlException {
        // create a user to be used as template
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(u)
                .send(client);
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // call the clone
        User cloned = new User();
        cloned.setUid("ricky2");
        cloned.setPassword("ricky2");
        CloneResponseAccessor cra = CloneRequestBuilder.builderForClone()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(cloned)
                .templateId(u.getUid())
                .templateTargetId(targetXsdId)
                .send(client);
        Assert.assertTrue(cra.isSuccess());
        User u2 = new User();
        u2.setUid(cloned.getUid());
        u2.setPassword(cloned.getPassword());
        u2.setCn(u.getCn());
        u2.setDescription(u.getDescription());
        u2.getRole().addAll(u.getRole());
        Assert.assertEquals(u2, cra.getXsdObject(User.class));
        // read the cloned user and compare it
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(cloned.getUid())
                .psoTargetId(targetXsdId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        Assert.assertEquals(u2, lra.getXsdObject(User.class));
        // delete the users
        DeleteResponseAccessor drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
        drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u2.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
    }
    
    @Test
    public void testXsdAsync() throws SpmlException {
        // create a user to be used as template
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(u)
                .send(client);
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // call the clone
        User cloned = new User();
        cloned.setUid("ricky2");
        cloned.setPassword("ricky2");
        CloneResponseAccessor cra = CloneRequestBuilder.builderForClone()
                .asynchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(cloned)
                .templateId(u.getUid())
                .templateTargetId(targetXsdId)
                .send(client);
        Assert.assertTrue(cra.isPending());
        cra = waitUntilExecuted(cra.getRequestId(), true).asAccessor(CloneResponseAccessor.emptyResponseAccessor());
        Assert.assertTrue(cra.isSuccess());
        User u2 = new User();
        u2.setUid(cloned.getUid());
        u2.setPassword(cloned.getPassword());
        u2.setCn(u.getCn());
        u2.setDescription(u.getDescription());
        u2.getRole().addAll(u.getRole());
        Assert.assertEquals(u2, cra.getXsdObject(User.class));
        // read the cloned user and compare it
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(cloned.getUid())
                .psoTargetId(targetXsdId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        Assert.assertEquals(u2, lra.getXsdObject(User.class));
        // delete the users
        DeleteResponseAccessor drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
        drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u2.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
    }
    
    @Test
    public void testDsml() throws SpmlException {
        // create a user to be used as template
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetDsmlId)
                .dsmlAttribute("objectclass", "user")
                .dsmlAttribute("uid", u.getUid())
                .dsmlAttribute("password", u.getPassword())
                .dsmlAttribute("cn", u.getCn())
                .dsmlAttribute("description", u.getDescription())
                .dsmlAttribute("role", u.getRole().toArray(new String[0]))
                .send(client);
        Assert.assertTrue(ara.isSuccess());
        checkUser(u, ara.getDsmlAttributesMap());
        // call the clone
        User cloned = new User();
        cloned.setUid("ricky2");
        cloned.setPassword("ricky2");
        CloneResponseAccessor cra = CloneRequestBuilder.builderForClone()
                .synchronous()
                .requestId()
                .targetId(targetDsmlId)
                .dsmlAttribute("objectclass", "user")
                .dsmlAttribute("uid", cloned.getUid())
                .dsmlAttribute("password", cloned.getPassword())
                .templateId(u.getUid())
                .templateTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(cra.isSuccess());
        User u2 = new User();
        u2.setUid(cloned.getUid());
        u2.setPassword(cloned.getPassword());
        u2.setCn(u.getCn());
        u2.setDescription(u.getDescription());
        u2.getRole().addAll(u.getRole());
        checkUser(u2, cra.getDsmlAttributesMap());
        // read the cloned user and compare it
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(cloned.getUid())
                .psoTargetId(targetDsmlId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        checkUser(u2, lra.getDsmlAttributesMap());
        // delete the users
        DeleteResponseAccessor drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
        drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u2.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
    }

    @Test
    public void testDsmlAsync() throws SpmlException {
        // create a user to be used as template
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetDsmlId)
                .dsmlAttribute("objectclass", "user")
                .dsmlAttribute("uid", u.getUid())
                .dsmlAttribute("password", u.getPassword())
                .dsmlAttribute("cn", u.getCn())
                .dsmlAttribute("description", u.getDescription())
                .dsmlAttribute("role", u.getRole().toArray(new String[0]))
                .send(client);
        Assert.assertTrue(ara.isSuccess());
        checkUser(u, ara.getDsmlAttributesMap());
        // call the clone
        User cloned = new User();
        cloned.setUid("ricky2");
        cloned.setPassword("ricky2");
        CloneResponseAccessor cra = CloneRequestBuilder.builderForClone()
                .asynchronous()
                .requestId()
                .targetId(targetDsmlId)
                .dsmlAttribute("objectclass", "user")
                .dsmlAttribute("uid", cloned.getUid())
                .dsmlAttribute("password", cloned.getPassword())
                .templateId(u.getUid())
                .templateTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(cra.isPending());
        cra = waitUntilExecuted(cra.getRequestId(), true).asAccessor(CloneResponseAccessor.emptyResponseAccessor());
        Assert.assertTrue(cra.isSuccess());
        User u2 = new User();
        u2.setUid(cloned.getUid());
        u2.setPassword(cloned.getPassword());
        u2.setCn(u.getCn());
        u2.setDescription(u.getDescription());
        u2.getRole().addAll(u.getRole());
        checkUser(u2, cra.getDsmlAttributesMap());
        // read the cloned user and compare it
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(cloned.getUid())
                .psoTargetId(targetDsmlId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        checkUser(u2, lra.getDsmlAttributesMap());
        // delete the users
        DeleteResponseAccessor drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
        drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u2.getUid())
                .psoTargetId(targetDsmlId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new HttpServer("/rpcrouter2", 8000, 1, true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.test.clone.ObjectFactory.class);
        server.start();
        client = new SOAPClient("http://localhost:8000/rpcrouter2", true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.test.clone.ObjectFactory.class);
        // get the targets
        targetXsdId = RequestBuilder.builderForListTargets()
                .requestId()
                .synchronous()
                .profileXsd()
                .send(client)
                .getTargets()[0]
                .getTargetId();
        targetDsmlId = RequestBuilder.builderForListTargets()
                .requestId()
                .synchronous()
                .profileDsml()
                .send(client)
                .getTargets()[0]
                .getTargetId();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(5);
        client.close();
    }
}
