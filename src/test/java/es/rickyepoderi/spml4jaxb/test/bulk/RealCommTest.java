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
package es.rickyepoderi.spml4jaxb.test.bulk;

import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BulkModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.FilterBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.search.ScopeType;
import es.rickyepoderi.spml4jaxb.test.HttpServer;
import es.rickyepoderi.spml4jaxb.user.User;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
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
        
    }
    
    static private User createSampleUser(int number) {
        User u = new User();
        u.setUid("ricky" + number);
        u.setCn("Ricardo Martin Camarero");
        u.setPassword(u.getUid());
        if (number %2 == 1) {
            // odd has description
            u.setDescription("me");
        }
        u.getRole().add("User");
        if (number % 2 == 0) {
            // even has Admin role
            u.getRole().add("Admin");
        }
        return u;
    }
    
    public ResponseAccessor waitUntilExecuted(String requestId, boolean returnResults) throws SpmlException {
        boolean executed = false;
        StatusResponseAccessor sra = null;
        while (!executed) {
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
    
    private static int searchAll() throws SpmlException {
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlAttributes("uid")
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        int n = 0;
        while (sra.hasNext()) {
            sra.next();
            n++;
        }
        while (sra.getIteratorId() != null) {
            sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(sra.getIteratorId())
                .send(client)
                .asSearch();
            Assert.assertTrue(sra.isSuccess());
            while (sra.hasNext()) {
                sra.next();
                n++;
            }
        }
        return n;
    }
    
    @Test
    public void testBulkDeleteDsmlSync() throws SpmlException {
        // delete 2, 4, and 6
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.not(
                                        FilterBuilder.present("description")
                                )
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 3);
        // delete the rest
        bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.present("description")
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 0);
    }
    
    @Test
    public void testBulkDeleteDsmlAsync() throws SpmlException {
        // delete 2, 4, and 6
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .asynchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.not(
                                        FilterBuilder.present("description")
                                )
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isPending());
        bdra = waitUntilExecuted(bdra.getRequestId(), false).asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 3);
        // delete the rest
        bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .asynchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.present("description")
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isPending());
        bdra = waitUntilExecuted(bdra.getRequestId(), false).asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 0);
    }
    
    @Test
    public void testBulkDeleteXsdSync() throws SpmlException {
        // delete admin roles (3)
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[role='Admin']")
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 3);
        // delete the rest
        bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[role='User']")
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 0);
    }
    
    @Test
    public void testBulkDeleteXsdAsync() throws SpmlException {
        // delete admin roles (3)
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .asynchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[role='Admin']")
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isPending());
        bdra = waitUntilExecuted(bdra.getRequestId(), false).asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 3);
        // delete the rest
        bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .asynchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[role='User']")
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isPending());
        bdra = waitUntilExecuted(bdra.getRequestId(), false).asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        Assert.assertEquals(searchAll(), 0);
    }
    
    private User getUser(String uid) throws SpmlException {
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .synchronous()
                .requestId()
                .psoId(uid)
                .psoTargetId(targetXsdId)
                .returnData()
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isSuccess());
        User u = (User) lra.getXsdObject(User.class);
        Assert.assertNotNull(u);
        return u;
    }
    
    private void deleteUsers() throws SpmlException {
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId(targetXsdId)
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user")
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
    }
    
    @Test
    public void testBulkModifyDsmlSync() throws SpmlException {
        BulkModifyResponseAccessor bmra = RequestBuilder.builderForBulkModify()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.present("description")
                        )
                )
                .dsmlReplace("cn", "Ricardo Martin")
                .dsmlReplace("description", "new me")
                .dsmlDelete("role", "User")
                .send(client)
                .asBulkModify();
        Assert.assertTrue(bmra.isSuccess());
        User u = getUser("ricky1");
        Assert.assertTrue(u.getRole().isEmpty());
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getDescription(), "new me");
        u = getUser("ricky3");
        Assert.assertTrue(u.getRole().isEmpty());
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getDescription(), "new me");
        u = getUser("ricky5");
        Assert.assertTrue(u.getRole().isEmpty());
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getDescription(), "new me");
        deleteUsers();
    }
    
    @Test
    public void testBulkModifyDsmlAsync() throws SpmlException {
        BulkModifyResponseAccessor bmra = RequestBuilder.builderForBulkModify()
                .requestId()
                .asynchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.not(
                                        FilterBuilder.present("description")
                                )
                        )
                )
                .dsmlReplace("cn", "Ricardo Martin")
                .dsmlReplace("description", "new me")
                .dsmlReplace("role", "Admin", "Other")
                .send(client)
                .asBulkModify();
        Assert.assertTrue(bmra.isPending());
        bmra = waitUntilExecuted(bmra.getRequestId(), false).asBulkModify();
        Assert.assertTrue(bmra.isSuccess());
        User u = getUser("ricky2");
        Assert.assertEquals(u.getRole().size(), 2);
        Assert.assertTrue(u.getRole().contains("Admin"));
        Assert.assertTrue(u.getRole().contains("Other"));
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getDescription(), "new me");
        u = getUser("ricky4");
        Assert.assertEquals(u.getRole().size(), 2);
        Assert.assertTrue(u.getRole().contains("Admin"));
        Assert.assertTrue(u.getRole().contains("Other"));
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getDescription(), "new me");
        u = getUser("ricky6");
        Assert.assertEquals(u.getRole().size(), 2);
        Assert.assertTrue(u.getRole().contains("Admin"));
        Assert.assertTrue(u.getRole().contains("Other"));
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getDescription(), "new me");
        deleteUsers();
    }
    
    @Test
    public void testBulkModifyXsdSync() throws SpmlException {
        BulkModifyResponseAccessor bmra = RequestBuilder.builderForBulkModify()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[description='me']")
                )
                .xsdReplace("/usr:user/usr:cn", "<usr:cn xmlns:usr=\"urn:ddbb-spml-dsml:user\">Ricardo Martin</usr:cn>")
                .xsdReplace("/user/password", "<usr:password xmlns:usr=\"urn:ddbb-spml-dsml:user\">ricky123</usr:password>")
                .xsdAdd("/user", "<usr:role xmlns:usr=\"urn:ddbb-spml-dsml:user\">Test</usr:role>")
                .send(client)
                .asBulkModify();
        Assert.assertTrue(bmra.isSuccess());
        User u = getUser("ricky1");
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getPassword(), "ricky123");
        Assert.assertTrue(u.getRole().contains("Test"));
        u = getUser("ricky3");
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getPassword(), "ricky123");
        Assert.assertTrue(u.getRole().contains("Test"));
        u = getUser("ricky5");
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getPassword(), "ricky123");
        Assert.assertTrue(u.getRole().contains("Test"));
        deleteUsers();
    }
    
    @Test
    public void testBulkModifyXsdAsync() throws SpmlException {
        BulkModifyResponseAccessor bmra = RequestBuilder.builderForBulkModify()
                .requestId()
                .asynchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[description='me']")
                )
                .xsdReplace("/usr:user/usr:cn", "<usr:cn xmlns:usr=\"urn:ddbb-spml-dsml:user\">Ricardo Martin</usr:cn>")
                .xsdReplace("/user/password", "<usr:password xmlns:usr=\"urn:ddbb-spml-dsml:user\">ricky123</usr:password>")
                .xsdAdd("/user", "<usr:role xmlns:usr=\"urn:ddbb-spml-dsml:user\">Test</usr:role>")
                .send(client)
                .asBulkModify();
        Assert.assertTrue(bmra.isPending());
        bmra = waitUntilExecuted(bmra.getRequestId(), false).asBulkModify();
        Assert.assertTrue(bmra.isSuccess());
        User u = getUser("ricky1");
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getPassword(), "ricky123");
        Assert.assertTrue(u.getRole().contains("Test"));
        u = getUser("ricky3");
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getPassword(), "ricky123");
        Assert.assertTrue(u.getRole().contains("Test"));
        u = getUser("ricky5");
        Assert.assertEquals(u.getCn(), "Ricardo Martin");
        Assert.assertEquals(u.getPassword(), "ricky123");
        Assert.assertTrue(u.getRole().contains("Test"));
        deleteUsers();
    }
    
    @BeforeMethod
    public static void setupMethod() throws SpmlException {
        // create 6 users before the bulk delete
        for (int i = 1; i <= 6; i++) {
            RequestBuilder.builderForAdd()
                    .requestId()
                    .synchronous()
                    .returnIdentifier()
                    .targetId(targetXsdId)
                    .xsdObject(createSampleUser(i))
                    .send(client)
                    .asAdd();
        }
    }
    
    @BeforeClass
    public static void setUpClass() throws SpmlException {
        server = new HttpServer("/rpcrouter2", 8000, 1, true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        server.start();
        client = new SOAPClient("http://localhost:8000/rpcrouter2", true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        // get the targets
        targetXsdId = RequestBuilder.builderForListTargets()
                .requestId()
                .synchronous()
                .profileXsd()
                .send(client)
                .asListTargets()
                .getTargets()[0]
                .getTargetId();
        targetDsmlId = RequestBuilder.builderForListTargets()
                .requestId()
                .synchronous()
                .profileDsml()
                .send(client)
                .asListTargets()
                .getTargets()[0]
                .getTargetId();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(5);
        client.close();
    }
    
}
