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
package es.rickyepoderi.spml4jaxb.test.batch;

import es.rickyepoderi.spml4jaxb.accessor.BatchResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
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
    
    private User createSampleUser(String name) {
        User u = new User();
        u.setUid(name);
        u.setCn(name + " " + name);
        u.setPassword(name + "123");
        u.setDescription(name);
        u.getRole().add("User");
        u.getRole().add("Admin");
        return u;
    }
    
    @Test
    public void testBatchSynchSequential() throws SpmlException {
        User u1 = createSampleUser("user1");
        User u2 = createSampleUser("user2");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .synchronous()
                .onErrorExit()
                .processingSequential()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u2.getUid())
                        .dsmlAttribute("password", u2.getPassword())
                        .dsmlAttribute("cn", u2.getCn())
                        .dsmlAttribute("description", u2.getDescription())
                        .dsmlAttribute("role", u2.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isSuccess());
    }
    
    @Test
    public void testBatchSynchSequentialExitOnError() throws SpmlException {
        User u1 = createSampleUser("user3");
        User u2 = createSampleUser("user4");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .synchronous()
                .onErrorExit()
                .processingSequential()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isFailure());
        Assert.assertEquals(bra.getNestedResponses().length, 1);
        Assert.assertTrue(bra.getNestedResponses()[0].isUnsupportedIdentifierType());
    }
    
    @Test
    public void testBatchSynchSequentialResumeOnError() throws SpmlException {
        User u1 = createSampleUser("user5");
        User u2 = createSampleUser("user6");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .synchronous()
                .onErrorResume()
                .processingSequential()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isFailure());
        Assert.assertEquals(bra.getNestedResponses().length, 2);
        Assert.assertTrue(bra.getNestedResponses()[0].isUnsupportedIdentifierType());
        Assert.assertTrue(bra.getNestedResponses()[1].isSuccess());
    }
    
    @Test
    public void testBatchSynchParallelOk() throws SpmlException {
        User u1 = createSampleUser("user7");
        User u2 = createSampleUser("user8");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .synchronous()
                .processingParallel()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u2.getUid())
                        .dsmlAttribute("password", u2.getPassword())
                        .dsmlAttribute("cn", u2.getCn())
                        .dsmlAttribute("description", u2.getDescription())
                        .dsmlAttribute("role", u2.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isSuccess());
        Assert.assertEquals(bra.getNestedResponses().length, 2);
    }
    
    @Test
    public void testBatchSynchParallelError() throws SpmlException {
        User u1 = createSampleUser("user9");
        User u2 = createSampleUser("user10");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .synchronous()
                .processingParallel()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u2.getUid())
                        .dsmlAttribute("password", u2.getPassword())
                        .dsmlAttribute("cn", u2.getCn())
                        .dsmlAttribute("description", u2.getDescription())
                        .dsmlAttribute("role", u2.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isFailure());
        Assert.assertEquals(bra.getNestedResponses().length, 2);
        Assert.assertTrue(bra.getNestedResponses()[0].isUnsupportedIdentifierType());
        Assert.assertTrue(bra.getNestedResponses()[1].isSuccess());
    }
    
    //
    // ASYNCH
    //
    
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
    public void testBatchASynchSequentialOk() throws SpmlException {
        User u1 = createSampleUser("user11");
        User u2 = createSampleUser("user12");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .asynchronous()
                .processingSequential()
                .onErrorResume()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u2.getUid())
                        .dsmlAttribute("password", u2.getPassword())
                        .dsmlAttribute("cn", u2.getCn())
                        .dsmlAttribute("description", u2.getDescription())
                        .dsmlAttribute("role", u2.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isPending());
        bra = this.waitUntilExecuted(bra.getRequestId(), true).asBatch();
        Assert.assertTrue(bra.isSuccess());
        Assert.assertEquals(bra.getNestedResponses().length, 2);
    }
    
    @Test
    public void testBatchASynchParallelOk() throws SpmlException {
        User u1 = createSampleUser("user13");
        User u2 = createSampleUser("user14");
        // list targets
        ListTargetsResponseAccessor ltra = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .send(client)
                .asListTargets();
        Assert.assertTrue(ltra.isSuccess());
        Assert.assertEquals(ltra.getTargets().length, 1);
        Assert.assertTrue(ltra.getTargets()[0].isDsml());
        String targetId = ltra.getTargets()[0].getTargetId();
        BatchResponseAccessor bra = RequestBuilder.builderForBatch()
                .requestId()
                .asynchronous()
                .processingParallel()
                .onErrorResume()
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u1.getUid())
                        .dsmlAttribute("password", u1.getPassword())
                        .dsmlAttribute("cn", u1.getCn())
                        .dsmlAttribute("description", u1.getDescription())
                        .dsmlAttribute("role", u1.getRole().toArray(new String[0]))
                )
                .nestedRequest(
                        RequestBuilder.builderForAdd()
                        .synchronous()
                        .requestId()
                        .targetId(targetId)
                        .dsmlAttribute("objectclass", "user")
                        .dsmlAttribute("uid", u2.getUid())
                        .dsmlAttribute("password", u2.getPassword())
                        .dsmlAttribute("cn", u2.getCn())
                        .dsmlAttribute("description", u2.getDescription())
                        .dsmlAttribute("role", u2.getRole().toArray(new String[0]))
                )
                .send(client)
                .asBatch();
        Assert.assertTrue(bra.isPending());
        bra = this.waitUntilExecuted(bra.getRequestId(), true).asBatch();
        Assert.assertTrue(bra.isSuccess());
        Assert.assertEquals(bra.getNestedResponses().length, 2);
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new HttpServer("/rpcrouter2", 8000, 2, true,
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
