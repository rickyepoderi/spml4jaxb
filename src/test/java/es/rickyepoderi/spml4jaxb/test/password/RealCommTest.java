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
package es.rickyepoderi.spml4jaxb.test.password;

import es.rickyepoderi.spml4jaxb.accessor.ExpirePasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordResponseAccessor;
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
    public void testSetPasswordAsync() throws SpmlException {
        // change the password
        String previousPassword = user.getPassword();
        user.setPassword("passsetasync123");
        SetPasswordResponseAccessor spra = RequestBuilder.builderForSetPassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .currentPassword(previousPassword)
                .password(user.getPassword())
                .send(client)
                .asSetPassword();
        Assert.assertTrue(spra.isPending());
        String requestId = spra.getRequestId();
        spra = waitUntilExecuted(requestId, true).asSetPassword();
        Assert.assertTrue(spra.isSuccess());
        // validate the new password
        ValidatePasswordResponseAccessor vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertTrue(vpra.isValid());
        // previous password is wrong
        vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(previousPassword)
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
    }
    
    @Test
    public void testResetPasswordAsync() throws SpmlException {
        // change the password
        String previousPassword = user.getPassword();
        user.setPassword("passasync123");
        ResetPasswordResponseAccessor rpra = RequestBuilder.builderForResetPassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asResetPassword();
        Assert.assertTrue(rpra.isPending());
        String requestId = rpra.getRequestId();
        rpra = waitUntilExecuted(requestId, true).asResetPassword();
        Assert.assertTrue(rpra.isSuccess());
        Assert.assertNotNull(rpra.getPassword());
        user.setPassword(rpra.getPassword());
        // validate the new password
        ValidatePasswordResponseAccessor vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertTrue(vpra.isValid());
        // previous password is wrong
        vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(previousPassword)
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
    }
    
    @Test
    public void testExpirePasswordAsync() throws SpmlException {
        // expire the password
        ExpirePasswordResponseAccessor epra = RequestBuilder.builderForExpirePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .remainingLoggings(0)
                .send(client)
                .asExpirePassword();
        Assert.assertTrue(epra.isPending());
        String requestId = epra.getRequestId();
        epra = waitUntilExecuted(requestId, true).asExpirePassword();
        Assert.assertTrue(epra.isSuccess());
        // good password does not work
        ValidatePasswordResponseAccessor vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
        // expire with one attempt
        epra = RequestBuilder.builderForExpirePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .remainingLoggings(1)
                .send(client)
                .asExpirePassword();
        Assert.assertTrue(epra.isPending());
        requestId = epra.getRequestId();
        epra = waitUntilExecuted(requestId, true).asExpirePassword();
        Assert.assertTrue(epra.isSuccess());
        // validate first should be ok
        vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertTrue(vpra.isValid());
        // and second ko
        vpra = RequestBuilder.builderForValidatePassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isPending());
        requestId = vpra.getRequestId();
        vpra = waitUntilExecuted(requestId, true).asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
        // reset the password to disable expiration
        ResetPasswordResponseAccessor rpra = RequestBuilder.builderForResetPassword()
                .asynchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asResetPassword();
        Assert.assertTrue(rpra.isPending());
        requestId = rpra.getRequestId();
        rpra = waitUntilExecuted(requestId, true).asResetPassword();
        Assert.assertTrue(rpra.isSuccess());
        Assert.assertNotNull(rpra.getPassword());
        user.setPassword(rpra.getPassword());
    }
    
    @Test
    public void testSetPassword() throws SpmlException {
        // change the password
        String previousPassword = user.getPassword();
        user.setPassword("password-set123");
        SetPasswordResponseAccessor spra = RequestBuilder.builderForSetPassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .currentPassword(previousPassword)
                .password(user.getPassword())
                .send(client)
                .asSetPassword();
        Assert.assertTrue(spra.isSuccess());
        // validate the new password
        ValidatePasswordResponseAccessor vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertTrue(vpra.isValid());
        // previous password is wrong
        vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(previousPassword)
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
    }
    
    @Test
    public void testResetPassword() throws SpmlException {
        // reset the password
        String previousPassword = user.getPassword();
        ResetPasswordResponseAccessor rpra = RequestBuilder.builderForResetPassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asResetPassword();
        Assert.assertTrue(rpra.isSuccess());
        Assert.assertNotNull(rpra.getPassword());
        user.setPassword(rpra.getPassword());
        // validate the new password
        ValidatePasswordResponseAccessor vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertTrue(vpra.isValid());
        // previous password is wrong
        vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(previousPassword)
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
    }
    
    @Test
    public void testExpirePassword() throws SpmlException {
        // expire the password
        ExpirePasswordResponseAccessor epra = RequestBuilder.builderForExpirePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .remainingLoggings(0)
                .send(client)
                .asExpirePassword();
        Assert.assertTrue(epra.isSuccess());
        // good password does not work
         ValidatePasswordResponseAccessor vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
        // expire with one attempt
        epra = RequestBuilder.builderForExpirePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .remainingLoggings(1)
                .send(client)
                .asExpirePassword();
        Assert.assertTrue(epra.isSuccess());
        vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertTrue(vpra.isValid());
        vpra = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .password(user.getPassword())
                .send(client)
                .asValidatePassword();
        Assert.assertTrue(vpra.isSuccess());
        Assert.assertFalse(vpra.isValid());
        // reset the password to disable expiration
        ResetPasswordResponseAccessor rpra = RequestBuilder.builderForResetPassword()
                .synchronous()
                .requestId()
                .psoId(user.getUid())
                .psoTargetId(targetId)
                .send(client)
                .asResetPassword();
        Assert.assertTrue(rpra.isSuccess());
        Assert.assertNotNull(rpra.getPassword());
        user.setPassword(rpra.getPassword());
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
