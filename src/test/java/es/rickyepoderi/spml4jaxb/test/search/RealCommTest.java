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
package es.rickyepoderi.spml4jaxb.test.search;

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
import java.util.Arrays;
import java.util.HashSet;
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
    
    static private User createSampleUser(int number) {
        User u = new User();
        u.setUid("ricky" + number);
        u.setCn("Ricardo Martin Camarero");
        u.setPassword(u.getUid());
        if (number %2 == 1) {
            // odd has description
            u.setDescription("description" + number);
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
    
    @Test
    public void testSearchDsmlDescription() throws SpmlException {
        Set<String> set = new HashSet<>(Arrays.asList(new String[]{"ricky2", "ricky4", "ricky6"}));
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlAttributes()
                        .dsmlFilter(
                                FilterBuilder.not(
                                        FilterBuilder.present("description")
                                )
                        )
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        String iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // second chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        Assert.assertTrue(set.isEmpty());
        // close iterator
        ResponseAccessor cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertTrue(cira.isSuccess());
    }
    
    @Test
    public void testDsmlSearchAllAndIterate() throws SpmlException {
        // search all the users just requesting the uid
        Set<String> set = new HashSet<>(Arrays.asList(new String[]{"ricky1", "ricky2", "ricky3", "ricky4", "ricky5", "ricky6"}));
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlAttributes("uid")
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        String iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // second chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // third chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        Assert.assertTrue(set.isEmpty());
        // close iterator
        ResponseAccessor cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertTrue(cira.isSuccess());
        // check the iterator no longer exists
        cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertFalse(cira.isSuccess());
        Assert.assertTrue(cira.isInvalidIdentifier());
    }
    
    @Test
    public void testDsmlSearchAllAndIterateAsync() throws SpmlException {
        // search all the users just requesting the uid
        Set<String> set = new HashSet<>(Arrays.asList(new String[]{"ricky1", "ricky2", "ricky3", "ricky4", "ricky5", "ricky6"}));
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .asynchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlAttributes("uid")
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isPending());
        String requestId = sra.getRequestId();
        sra = waitUntilExecuted(requestId, true).asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        String iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // second chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // third chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        Assert.assertTrue(set.isEmpty());
        // close iterator
        ResponseAccessor cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertTrue(cira.isSuccess());
        // check the iterator no longer exists
        cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertFalse(cira.isSuccess());
        Assert.assertTrue(cira.isInvalidIdentifier());
    }
    
    @Test
    public void testSearchDsmlUser1() throws SpmlException {
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlAttributes()
                        .dsmlFilter(
                                FilterBuilder.or(
                                        FilterBuilder.and(
                                                FilterBuilder.approxMatch("role", "User"),
                                                FilterBuilder.greaterOrEqual("description", "description1"),
                                                FilterBuilder.lessOrEqual("uid", "ricky1"),
                                                FilterBuilder.contains("uid", "ricky"),
                                                FilterBuilder.endsWith("uid", "ky1"),
                                                FilterBuilder.startsWith("uid", "ricky"),
                                                FilterBuilder.present("role")
                                        ),
                                        FilterBuilder.equals("description", "description1")
                                )
                        )
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertEquals("ricky1", sra.getPsoId());
        Assert.assertTrue(sra.getDsmlAttributes() == null || sra.getDsmlAttributes().length == 0);
        Assert.assertFalse(sra.hasNext());
    }
    
    @Test
    public void testSearchXsdRole() throws SpmlException {
        Set<String> set = new HashSet<>(Arrays.asList(new String[]{"ricky2", "ricky4", "ricky6"}));
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[role='Admin']")
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        String iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // second chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        Assert.assertTrue(set.isEmpty());
        // close iterator
        ResponseAccessor cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertTrue(cira.isSuccess());
    }
    
    
    @Test
    public void testXsdSearchAllAndIterate() throws SpmlException {
        // search all the users just requesting the uid
        Set<String> set = new HashSet<>(Arrays.asList(new String[]{"ricky1", "ricky2", "ricky3", "ricky4", "ricky5", "ricky6"}));
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .synchronous()
                .requestId()
                .returnIdentifier()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user")
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        String iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // second chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNotNull(sra.getIteratorId());
        iterId = sra.getIteratorId();
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        // third chunk
        sra = RequestBuilder.builderForIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertNotNull(set.remove(sra.getPsoId()));
        Assert.assertFalse(sra.hasNext());
        Assert.assertTrue(set.isEmpty());
        // close iterator
        ResponseAccessor cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertTrue(cira.isSuccess());
        // check the iterator no longer exists
        cira = RequestBuilder.builderForCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client);
        Assert.assertFalse(cira.isSuccess());
        Assert.assertTrue(cira.isInvalidIdentifier());
    }
    
    @Test
    public void testSearchXsdComplexSearch() throws SpmlException {
        SearchResponseAccessor sra = RequestBuilder.builderForSearch()
                .synchronous()
                .requestId()
                .returnEverything()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user[uid='ricky2' and (description='description1' or role='User')]")
                )
                .send(client)
                .asSearch();
        Assert.assertTrue(sra.isSuccess());
        Assert.assertNull(sra.getIteratorId());
        Assert.assertTrue(sra.hasNext());
        sra.next();
        Assert.assertEquals("ricky2", sra.getPsoId());
        Assert.assertNotNull(sra.getXsdObject(User.class));
        User u = (User) sra.getXsdObject(User.class);
        Assert.assertEquals(u, createSampleUser(2));
        Assert.assertFalse(sra.hasNext());
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        server = new HttpServer("/rpcrouter2", 8000, 1, true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        server.start();
        client = new SOAPClient("http://localhost:8000/rpcrouter2", true,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
        // create a user
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

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(5);
        client.close();
    }
}
