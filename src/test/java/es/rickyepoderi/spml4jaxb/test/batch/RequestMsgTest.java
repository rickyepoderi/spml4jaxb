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

import es.rickyepoderi.spml4jaxb.accessor.AddRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BatchRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SuspendRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.AddRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.SetPasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.SuspendRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class RequestMsgTest {
    
    private JAXBContext ctx = null;
    
    public RequestMsgTest() throws JAXBException {
        ctx = JAXBContext.newInstance(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private RequestAccessor parse(JAXBElement<RequestType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<RequestType> out = (JAXBElement<RequestType>) unmarshaller.unmarshal(bis);
        return RequestAccessor.accessorForRequest(out.getValue());
    }
    
    @Test
    public void testBatch() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setDescription("description");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        AddRequestBuilder addBuilder = RequestBuilder.builderForAdd()
                .requestId()
                .synchronous()
                .returnEverything()
                .psoId(u.getUid())
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .xsdObject(u);
        SetPasswordRequestBuilder setPassBuilder = RequestBuilder.builderForSetPassword()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId("testtargetid")
                .password(u.getPassword() + "1")
                .currentPassword(u.getPassword());
        SuspendRequestBuilder suspendBuilder = RequestBuilder.builderForSuspend()
                .synchronous()
                .requestId()
                .psoId(u.getUid())
                .psoTargetId("testtargetid");
        JAXBElement el = RequestBuilder.builderForBatch()
                .synchronous()
                .requestId()
                .onErrorResume()
                .processingParallel()
                .nestedRequest(addBuilder)
                .nestedRequest(setPassBuilder)
                .nestedRequest(suspendBuilder)
                .build();
        BatchRequestAccessor req = parse(el).asBatch();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isProcessingParallel());
        Assert.assertTrue(req.isOnErrorResume());
        RequestAccessor[] nested = req.getNestedRequests();
        Assert.assertEquals(nested.length, 3);
        // add
        Assert.assertNotNull(nested[0].asAdd());
        AddRequestAccessor add = nested[0].asAdd();
        Assert.assertTrue(add.isSynchronous());
        Assert.assertTrue(add.isReturnEverything());
        Assert.assertEquals(u.getUid(), add.getPsoId());
        Assert.assertEquals("testtargetid", add.getPsoTargetId());
        Assert.assertEquals("testcontainerid", add.getContainerId());
        Assert.assertEquals("testcontainertargetid", add.getContainerTargetId());
        User other = (User) add.getXsdObject(User.class);
        Assert.assertEquals(u, other);
        // password
        Assert.assertNotNull(nested[1].asSetPassword());
        SetPasswordRequestAccessor pass = nested[1].asSetPassword();
        Assert.assertTrue(pass.isSynchronous());
        Assert.assertNotNull(pass.getRequestId());
        Assert.assertEquals(u.getUid(), pass.getPsoId());
        Assert.assertEquals("testtargetid", pass.getPsoTargetId());
        Assert.assertEquals(u.getPassword() + "1", pass.getPassword());
        Assert.assertEquals(u.getPassword(), pass.getCurrentPassword());
        // suspend
        Assert.assertNotNull(nested[2].asSuspend());
        SuspendRequestAccessor sus = nested[2].asSuspend();
        Assert.assertTrue(sus.isSynchronous());
        Assert.assertEquals(u.getUid(), sus.getPsoId());
        Assert.assertEquals("testtargetid", sus.getPsoTargetId());
    }
}
