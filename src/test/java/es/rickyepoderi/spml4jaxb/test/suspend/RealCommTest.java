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
package es.rickyepoderi.spml4jaxb.test.suspend;

import es.rickyepoderi.spml4jaxb.accessor.ActiveResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.test.HttpServer;
import es.rickyepoderi.spml4jaxb.user.User;
import java.util.Date;
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
    static private User user = createSampleUser();
    static private String targetId = null;

    public RealCommTest() {
        // noop
    }
    
    static private User createSampleUser() {
        User u = new User();
        u.setUid("ricky");
        u.setCn("Ricardo Martin Camarero");
        u.setPassword("ricky123");
        u.setDescription("me");
        u.getRole().add("User");
        u.getRole().add("Admin");
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
    
    @Test
    public void testSuspendAsync() throws SpmlException {
        // check user is enabled
        ActiveResponseAccessor ara = RequestBuilder.builderForActive()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isPending());
        String requestId = ara.getRequestId();
        ara = waitUntilExecuted(requestId, true).asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertTrue(ara.isActive());
        // disable user
        ResponseAccessor sra = RequestBuilder.builderForSuspend()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(sra.isPending());
        requestId = sra.getRequestId();
        sra = waitUntilExecuted(requestId, true);
        Assert.assertTrue(sra.isSuccess());
        // check user is disabled
        ara = RequestBuilder.builderForActive()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isPending());
        requestId = ara.getRequestId();
        ara = waitUntilExecuted(requestId, true).asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertFalse(ara.isActive());
        // enable user
        ResponseAccessor rra = RequestBuilder.builderForResume()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(rra.isPending());
        requestId = rra.getRequestId();
        rra = waitUntilExecuted(requestId, true);
        Assert.assertTrue(rra.isSuccess());
        // check is again enabled
        ara = RequestBuilder.builderForActive()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isPending());
        requestId = ara.getRequestId();
        ara = waitUntilExecuted(requestId, true).asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertTrue(ara.isActive());
    }
    
    @Test
    public void testSuspendSync() throws SpmlException {
        // check user is enabled
        ActiveResponseAccessor ara = RequestBuilder.builderForActive()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertTrue(ara.isActive());
        // disable user
        ResponseAccessor sra = RequestBuilder.builderForSuspend()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(sra.isSuccess());
        // check user is disabled
        ara = RequestBuilder.builderForActive()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertFalse(ara.isActive());
        // enable user
        ResponseAccessor rra = RequestBuilder.builderForResume()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client);
        Assert.assertTrue(rra.isSuccess());
        // check is again enabled
        ara = RequestBuilder.builderForActive()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertTrue(ara.isActive());
    }
    
    @Test
    public void testSuspendSyncWithDates() throws SpmlException {
        // check user is enabled
        ActiveResponseAccessor ara = RequestBuilder.builderForActive()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertTrue(ara.isActive());
        // disable user (1 day ago)
        ResponseAccessor sra = RequestBuilder.builderForSuspend()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .effectiveDate(new Date(System.currentTimeMillis() - 86400000L))
                .send(client);
        Assert.assertTrue(sra.isSuccess());
        // check user is disabled
        ara = RequestBuilder.builderForActive()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertFalse(ara.isActive());
        // enable user (half day ago)
        ResponseAccessor rra = RequestBuilder.builderForResume()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .effectiveDate(new Date(System.currentTimeMillis() - 43200000L))
                .send(client);
        Assert.assertTrue(rra.isSuccess());
        // check is again enabled
        ara = RequestBuilder.builderForActive()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asActive();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertTrue(ara.isActive());
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new HttpServer("/rpcrouter2", 8000, 1, true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        server.start();
        client = new SOAPClient("http://localhost:8000/rpcrouter2", true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        // create a user
        targetId = RequestBuilder.builderForListTargets()
                .requestId()
                .synchronous()
                .profileXsd()
                .send(client)
                .asListTargets()
                .getTargets()[0]
                .getTargetId();
        RequestBuilder.builderForAdd()
                .requestId()
                .synchronous()
                .returnIdentifier()
                .targetId(targetId)
                .xsdObject(user)
                .send(client)
                .asAdd();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // delete the user
        RequestBuilder.builderForDelete()
                .requestId()
                .synchronous()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client);
        server.shutdown(5);
        client.close();
    }
}
