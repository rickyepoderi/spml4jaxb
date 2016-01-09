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
package es.rickyepoderi.spml4jaxb.test.updates;

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BulkModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.DeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResumeResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SuspendResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesCloseIteratorResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.FilterBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.msg.search.ScopeType;
import es.rickyepoderi.spml4jaxb.test.HttpServer;
import es.rickyepoderi.spml4jaxb.user.User;
import java.util.Date;
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
    public void testUpdatesCoreSync() throws SpmlException {
        Date start = new Date();
        // create a user and perform create -> update -> delete
        // add
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
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        checkUser(u, ara.getDsmlAttributesMap());
        // update
        u.setDescription(null);
        u.setPassword("123password");
        u.getRole().remove("Admin");
        u.getRole().add("Other");
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .dsmlDelete("description", "me")
                .dsmlReplace("password", u.getPassword())
                .dsmlDelete("role", "Admin")
                .dsmlAdd("role", "Other")
                .send(client)
                .asModify();
        Assert.assertTrue(mra.isSuccess());
        checkUser(u, mra.getDsmlAttributesMap());
        // delete
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        // perform the updates using since and core profile => three updates
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNotNull(ura.getIteratorId());
        String iterId = ura.getIteratorId();
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindAdd());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
        // second chunk
        ura = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
        // close the iterator
        UpdatesCloseIteratorResponseAccessor cira = RequestBuilder.builderForUpdatesCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdatesCloseIterator();
        Assert.assertTrue(cira.isSuccess());
    }
    
    @Test
    public void testUpdatesCoreAsync() throws SpmlException {
        Date start = new Date();
        // create a user and perform create -> update -> delete
        // add
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
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        checkUser(u, ara.getDsmlAttributesMap());
        // update
        u.setDescription(null);
        u.setPassword("123password");
        u.getRole().remove("Admin");
        u.getRole().add("Other");
        ModifyResponseAccessor mra = RequestBuilder.builderForModify()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .dsmlDelete("description", "me")
                .dsmlReplace("password", u.getPassword())
                .dsmlDelete("role", "Admin")
                .dsmlAdd("role", "Other")
                .send(client)
                .asModify();
        Assert.assertTrue(mra.isSuccess());
        checkUser(u, mra.getDsmlAttributesMap());
        // delete
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetDsmlId)
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        // perform the updates using since and core profile => three updates
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .asynchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isPending());
        ura = waitUntilExecuted(ura.getRequestId(), true).asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNotNull(ura.getIteratorId());
        String iterId = ura.getIteratorId();
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindAdd());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
        // second chunk
        ura = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_CORE_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
        // close the iterator
        UpdatesCloseIteratorResponseAccessor cira = RequestBuilder.builderForUpdatesCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdatesCloseIterator();
        Assert.assertTrue(cira.isSuccess());
    }
    
    @Test
    public void testUpdatesPasswordSync() throws SpmlException {
        Date start = new Date();
        // create a user and perform setPassword, resetPassword
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(u)
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // set a new password
        String prevPasswd = u.getPassword();
        u.setPassword("ricky321!");
        SetPasswordResponseAccessor spra = RequestBuilder.builderForSetPassword()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .currentPassword(prevPasswd)
                .password(u.getPassword())
                .send(client)
                .asSetPassword();
        Assert.assertTrue(spra.isSuccess());
        // reset the password
        ResetPasswordResponseAccessor rpra = RequestBuilder.builderForResetPassword()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asResetPassword();
        Assert.assertTrue(rpra.isSuccess());
        // delete 
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        // look for the two updates
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user")
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
    }
    
    @Test
    public void testUpdatesPasswordAsync() throws SpmlException {
        Date start = new Date();
        // create a user and perform setPassword, resetPassword
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(u)
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // set a new password
        String prevPasswd = u.getPassword();
        u.setPassword("ricky321!");
        SetPasswordResponseAccessor spra = RequestBuilder.builderForSetPassword()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .currentPassword(prevPasswd)
                .password(u.getPassword())
                .send(client)
                .asSetPassword();
        Assert.assertTrue(spra.isSuccess());
        // reset the password
        ResetPasswordResponseAccessor rpra = RequestBuilder.builderForResetPassword()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asResetPassword();
        Assert.assertTrue(rpra.isSuccess());
        // delete 
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        // look for the two updates
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .asynchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user")
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isPending());
        ura = waitUntilExecuted(ura.getRequestId(), true).asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
    }
    
    @Test
    public void testUpdatesSuspendSync() throws SpmlException {
        Date start = new Date();
        // create a user and perform suspend, resume
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(u)
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // suspend
        SuspendResponseAccessor sra = RequestBuilder.builderForSuspend()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asSuspend();
        Assert.assertTrue(sra.isSuccess());
        // resume
        ResumeResponseAccessor rra = RequestBuilder.builderForResume()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asResume();
        Assert.assertTrue(rra.isSuccess());
        // delete 
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        // look for the two updates
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user")
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
    }
    
    @Test
    public void testUpdatesSuspendAsync() throws SpmlException {
        Date start = new Date();
        // create a user and perform suspend, resume
        User u = createSampleUser();
        AddResponseAccessor ara = RequestBuilder.builderForAdd()
                .synchronous()
                .requestId()
                .targetId(targetXsdId)
                .xsdObject(u)
                .send(client)
                .asAdd();
        Assert.assertTrue(ara.isSuccess());
        Assert.assertEquals(u, ara.getXsdObject(User.class));
        // suspend
        SuspendResponseAccessor sra = RequestBuilder.builderForSuspend()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asSuspend();
        Assert.assertTrue(sra.isSuccess());
        // resume
        ResumeResponseAccessor rra = RequestBuilder.builderForResume()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asResume();
        Assert.assertTrue(rra.isSuccess());
        // delete 
        DeleteResponseAccessor dra = RequestBuilder.builderForDelete()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId(targetXsdId)
                .send(client)
                .asDelete();
        Assert.assertTrue(dra.isSuccess());
        // look for the two updates
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .asynchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetXsdId)
                        .xsdXPathSelection("//user")
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isPending());
        ura = waitUntilExecuted(ura.getRequestId(), true).asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindCability());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky");
        Assert.assertFalse(ura.hasNext());
    }
    
    @Test
    public void testUpdatesBulkDeleteSync() throws SpmlException {
        Date start = new Date();
        // create a user and perform suspend, resume
        for (int i = 1; i <= 3; i++) {
            Assert.assertTrue(RequestBuilder.builderForAdd()
                    .requestId()
                    .synchronous()
                    .returnIdentifier()
                    .targetId(targetXsdId)
                    .xsdObject(createSampleUser(i))
                    .send(client)
                    .asAdd()
                    .isSuccess());
        }
        // perform bulk delete
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.startsWith("uid", "ricky")
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        // look for the three deleted users
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNotNull(ura.getIteratorId());
        String iterId = ura.getIteratorId();
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky1");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky2");
        Assert.assertFalse(ura.hasNext());
        // second chunk
        ura = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky3");
        Assert.assertFalse(ura.hasNext());
        // close the iterator
        UpdatesCloseIteratorResponseAccessor cira = RequestBuilder.builderForUpdatesCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdatesCloseIterator();
        Assert.assertTrue(cira.isSuccess());
    }
    
    @Test
    public void testUpdatesBulkDeleteAsync() throws SpmlException {
        Date start = new Date();
        // create a user and perform suspend, resume
        for (int i = 1; i <= 3; i++) {
            Assert.assertTrue(RequestBuilder.builderForAdd()
                    .requestId()
                    .synchronous()
                    .returnIdentifier()
                    .targetId(targetXsdId)
                    .xsdObject(createSampleUser(i))
                    .send(client)
                    .asAdd()
                    .isSuccess());
        }
        // perform bulk delete
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.startsWith("uid", "ricky")
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
        // look for the three deleted users
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .asynchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isPending());
        ura = waitUntilExecuted(ura.getRequestId(), true).asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNotNull(ura.getIteratorId());
        String iterId = ura.getIteratorId();
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky1");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky2");
        Assert.assertFalse(ura.hasNext());
        // second chunk
        ura = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindDelete());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky3");
        Assert.assertFalse(ura.hasNext());
        // close the iterator
        UpdatesCloseIteratorResponseAccessor cira = RequestBuilder.builderForUpdatesCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdatesCloseIterator();
        Assert.assertTrue(cira.isSuccess());
    }
    
    @Test
    public void testUpdatesBulkModifySync() throws SpmlException {
        Date start = new Date();
        // create a user and perform suspend, resume
        for (int i = 1; i <= 3; i++) {
            Assert.assertTrue(RequestBuilder.builderForAdd()
                    .requestId()
                    .synchronous()
                    .returnIdentifier()
                    .targetId(targetXsdId)
                    .xsdObject(createSampleUser(i))
                    .send(client)
                    .asAdd()
                    .isSuccess());
        }
        // perform bulk modify
        BulkModifyResponseAccessor bmra = RequestBuilder.builderForBulkModify()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.startsWith("uid", "ricky")
                        )
                )
                .dsmlReplace("description", "new desc")
                .dsmlReplace("password", "ricky321!")
                .send(client)
                .asBulkModify();
        Assert.assertTrue(bmra.isSuccess());
        // look for the three deleted users
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .synchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNotNull(ura.getIteratorId());
        String iterId = ura.getIteratorId();
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky1");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky2");
        Assert.assertFalse(ura.hasNext());
        // second chunk
        ura = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky3");
        Assert.assertFalse(ura.hasNext());
        // close the iterator
        UpdatesCloseIteratorResponseAccessor cira = RequestBuilder.builderForUpdatesCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdatesCloseIterator();
        Assert.assertTrue(cira.isSuccess());
        // delete the users for the next test
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.startsWith("uid", "ricky")
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
    }
    
    @Test
    public void testUpdatesBulkModifyAsync() throws SpmlException {
        Date start = new Date();
        // create a user and perform suspend, resume
        for (int i = 1; i <= 3; i++) {
            Assert.assertTrue(RequestBuilder.builderForAdd()
                    .requestId()
                    .synchronous()
                    .returnIdentifier()
                    .targetId(targetXsdId)
                    .xsdObject(createSampleUser(i))
                    .send(client)
                    .asAdd()
                    .isSuccess());
        }
        // perform bulk modify
        BulkModifyResponseAccessor bmra = RequestBuilder.builderForBulkModify()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.startsWith("uid", "ricky")
                        )
                )
                .dsmlReplace("description", "new desc")
                .dsmlReplace("password", "ricky321!")
                .send(client)
                .asBulkModify();
        Assert.assertTrue(bmra.isSuccess());
        // look for the three deleted users
        UpdatesResponseAccessor ura = RequestBuilder.builderForUpdates()
                .asynchronous()
                .requestId()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                )
                .updatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI)
                .updatedSince(start)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isPending());
        ura = waitUntilExecuted(ura.getRequestId(), true).asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNotNull(ura.getIteratorId());
        String iterId = ura.getIteratorId();
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky1");
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky2");
        Assert.assertFalse(ura.hasNext());
        // second chunk
        ura = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdates();
        Assert.assertTrue(ura.isSuccess());
        Assert.assertNull(ura.getIteratorId());
        Assert.assertTrue(ura.hasNext());
        ura.next();
        Assert.assertNotNull(ura.isUpdateKindModify());
        Assert.assertNotNull(ura.isUpdatedByCapability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI));
        Assert.assertEquals(ura.getUpdatePsoId(), "ricky3");
        Assert.assertFalse(ura.hasNext());
        // close the iterator
        UpdatesCloseIteratorResponseAccessor cira = RequestBuilder.builderForUpdatesCloseIterator()
                .synchronous()
                .requestId()
                .iteratorId(iterId)
                .send(client)
                .asUpdatesCloseIterator();
        Assert.assertTrue(cira.isSuccess());
        // delete the users for the next test
        BulkDeleteResponseAccessor bdra = RequestBuilder.builderForBulkDelete()
                .requestId()
                .synchronous()
                .query(RequestBuilder.builderForQuery()
                        .scope(ScopeType.SUB_TREE)
                        .targetId(targetDsmlId)
                        .dsmlFilter(
                                FilterBuilder.startsWith("uid", "ricky")
                        )
                )
                .send(client)
                .asBulkDelete();
        Assert.assertTrue(bdra.isSuccess());
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
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        server.shutdown(5);
        client.close();
    }
}
