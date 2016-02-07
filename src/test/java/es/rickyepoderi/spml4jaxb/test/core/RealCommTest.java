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
package es.rickyepoderi.spml4jaxb.test.core;

import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.DeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.test.HttpServer;
import es.rickyepoderi.spml4jaxb.user.User;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
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

    public RealCommTest() {
        
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
    
    @Test
    public void testXsd() throws SpmlException {
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileXsd()
                .send(client);
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isXsd());
        String targetId = ltra.getTargets()[0].getTargetId();
        // add
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetId)
                .xsdObject(u)
                .send(client);
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // lookup
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        Assert.assertEquals(u, lra.getXsdObject(User.class));
        // add error
        ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetId)
                .xsdObject(u)
                .send(client);
        Assert.assertTrue(ara.isFailure());
        Assert.assertTrue(ara.isAlreadyExists());
        // modify whole object
        u.setDescription(null);
        u.setPassword("123password");
        u.getRole().remove("Admin");
        u.getRole().add("Other");
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .xsdReplace("/user", u)
                .send(client);
        Assert.assertTrue(mra.isSuccess());
        Assert.assertEquals(u, mra.getXsdObject(User.class));
        // lookup again
        lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        Assert.assertEquals(u, lra.getXsdObject(User.class));
        // modify user in different parts
        u.getRole().add("Test");
        u.getRole().remove("User");
        u.setCn("Ricardo Martin");
        mra = RequestBuilder.builderForModify()
                .requestId()
                .synchronous()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .xsdAdd("/user", "<usr:role xmlns:usr=\"urn:ddbb-spml-dsml:user\">Test</usr:role>")
                .xsdDelete("/user/role[text()='User']")
                .xsdReplace("/usr:user/usr:cn", "<usr:cn xmlns:usr=\"urn:ddbb-spml-dsml:user\">Ricardo Martin</usr:cn>")
                .send(client);
        Assert.assertTrue(mra.isSuccess());
        Assert.assertEquals(u, mra.getXsdObject(User.class));
        // lookup again
        lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        Assert.assertEquals(u, lra.getXsdObject(User.class));
        // delete user
        DeleteResponseAccessor drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
        // lookup again
        lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isFailure());
        Assert.assertTrue(lra.isNoSuchIdentifier());
    }

    @Test
    public void testDsml() throws SpmlException {
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client);
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        // add
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetId)
                .dsmlAttribute("objectclass", "user")
                .dsmlAttribute("uid", u.getUid())
                .dsmlAttribute("password", u.getPassword())
                .dsmlAttribute("cn", u.getCn())
                .dsmlAttribute("description", u.getDescription())
                .dsmlAttribute("role", u.getRole().toArray(new String[0]))
                .send(client);
        Assert.assertTrue(ara.isSuccess());
        checkUser(u, ara.getDsmlAttributesMap());
        // lookup
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        checkUser(u, lra.getDsmlAttributesMap());
        // add error
        ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetId)
                .dsmlAttribute("objectclass", "user")
                .dsmlAttribute("uid", u.getUid())
                .dsmlAttribute("password", u.getPassword())
                .dsmlAttribute("cn", u.getCn())
                .dsmlAttribute("description", u.getDescription())
                .dsmlAttribute("role", u.getRole().toArray(new String[0]))
                .send(client);
        Assert.assertTrue(ara.isFailure());
        Assert.assertTrue(ara.isAlreadyExists());
        // modify
        u.setDescription(null);
        u.setPassword("123password");
        u.getRole().remove("Admin");
        u.getRole().add("Other");
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .dsmlDelete("description", "me")
                .dsmlReplace("password", u.getPassword())
                .dsmlDelete("role", "Admin")
                .dsmlAdd("role", "Other")
                .send(client);
        Assert.assertTrue(mra.isSuccess());
        checkUser(u, mra.getDsmlAttributesMap());
        // lookup again
        lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isSuccess());
        checkUser(u, lra.getDsmlAttributesMap());
        // delete user
        DeleteResponseAccessor drb = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(drb.isSuccess());
        // lookup again
        lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client);
        Assert.assertTrue(lra.isFailure());
        Assert.assertTrue(lra.isNoSuchIdentifier());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new HttpServer("/rpcrouter2", 8000, 0, true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        server.start();
        client = new SOAPClient("http://localhost:8000/rpcrouter2", true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(5);
        client.close();
    }
}
