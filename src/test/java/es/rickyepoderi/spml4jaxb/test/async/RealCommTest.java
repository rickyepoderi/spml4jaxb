/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.test.async;

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.CancelResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.test.HttpServer;
import es.rickyepoderi.spml4jaxb.user.User;
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
        // noop
    }

    private void testWithoutExecutor() throws SpmlException {
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileXsd()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isXsd());
        String targetId = ltra.getTargets()[0].getTargetId();
        // add async
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .requestId()
                .asynchronous()
                .targetId(targetId)
                .xsdObject(u)
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isPending());
        String requestId = ara.getRequestId();
        // check that the request remains in pending until the worker is started
        StatusResponseAccessor sra = RequestBuilder.builderForStatus()
                .requestId()
                .asyncRequestId(requestId)
                .noReturnResults()
                .send(client)
                .asStatus();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertEquals(sra.getAsyncRequestId(), requestId);
        Assert.assertNotNull(sra.getNestedResponse());
        Assert.assertTrue(sra.getNestedResponse().asAdd().isPending());
        Assert.assertEquals(sra.getNestedResponse().asAdd().getRequestId(), requestId);
        // cancel the request
        CancelResponseAccessor cra = RequestBuilder.builderForCancel()
                .requestId()
                .asyncRequestId(requestId)
                .send(client)
                .asCancel();
        Assert.assertTrue(cra.isSuccess());
        Assert.assertEquals(cra.getAsyncRequestId(), requestId);
        // check noSuchRequest in status
        sra = RequestBuilder.builderForStatus()
                .requestId()
                .asyncRequestId(requestId)
                .noReturnResults()
                .send(client)
                .asStatus();
        Assert.assertTrue(sra.isFailure());
        Assert.assertTrue(sra.isNoSuchRequest());
        // check noSuchRequest in cancel
        cra = RequestBuilder.builderForCancel()
                .requestId()
                .asyncRequestId(requestId)
                .send(client)
                .asCancel();
        Assert.assertTrue(cra.isFailure());
        Assert.assertTrue(cra.isNoSuchRequest());
        // check invalidIdentfier
        sra = RequestBuilder.builderForStatus()
                .requestId()
                .noReturnResults()
                .send(client)
                .asStatus();
        Assert.assertTrue(sra.isFailure());
        Assert.assertTrue(sra.isInvalidIdentifier());
        // check InvalidIdentifier
        cra = RequestBuilder.builderForCancel()
                .requestId()
                .send(client)
                .asCancel();
        Assert.assertTrue(cra.isFailure());
        Assert.assertTrue(cra.isInvalidIdentifier());
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
    
    public void testWithExecutor() throws SpmlException {
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileXsd()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isXsd());
        String targetId = ltra.getTargets()[0].getTargetId();
        //
        // add async
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .requestId()
                .asynchronous()
                .targetId(targetId)
                .xsdObject(u)
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isPending());
        String requestId = ara.getRequestId();
        // check without results
        ara = waitUntilExecuted(requestId, false).asAdd();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertNull(ara.getXsdObject(User.class));
        // check with results
        ara = waitUntilExecuted(requestId, true).asAdd();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        //
        // lookup async
        LookupResponseAccessor lra = RequestBuilder.builderForLookup()
                .asynchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isPending());
        requestId = lra.getRequestId();
        lra = waitUntilExecuted(requestId, true).asLookup();
        Assert.assertTrue(lra.isSuccess());
        Assert.assertEquals(u, lra.getXsdObject(User.class));
        //
        // Modify
        u.setDescription(null);
        u.setPassword("123password");
        u.getRole().remove("Admin");
        u.getRole().add("Other");
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .asynchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .xsdReplace("/user", u)
                .send(client)
                .asModify();
        Assert.assertTrue(mra.isPending());
        requestId = mra.getRequestId();
        mra = waitUntilExecuted(requestId, true).asModify();
        Assert.assertTrue(mra.isSuccess());
        Assert.assertEquals(u, mra.getXsdObject(User.class));
        //
        // Delete
        ResponseAccessor dra = RequestBuilder.builderForDelete()
                .asynchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(dra.isPending());
        requestId = dra.getRequestId();
        dra = waitUntilExecuted(requestId, true);
        Assert.assertTrue(dra.isSuccess());
        //
        // Lookup error
        lra = RequestBuilder.builderForLookup()
                .asynchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetId)
                .returnData()
                .send(client)
                .asLookup();
        Assert.assertTrue(lra.isPending());
        requestId = lra.getRequestId();
        lra = waitUntilExecuted(requestId, true).asLookup();
        Assert.assertTrue(lra.isFailure());
        Assert.assertTrue(lra.isNoSuchIdentifier());
    }
    
    @Test
    public void testAsync() throws SpmlException, InterruptedException {
        // test without the executor to test cancel
        testWithoutExecutor();
        server.addAsyncExecutor();
        Thread.sleep(2000L);
        // test with the executor started
        testWithExecutor();
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
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new HttpServer("/rpcrouter2", 8000, 0,
                es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        server.start();
        client = new SOAPClient("http://localhost:8000/rpcrouter2",
                es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(5);
        client.close();
    }
}
