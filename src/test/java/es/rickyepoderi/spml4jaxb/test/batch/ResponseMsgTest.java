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

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BatchResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.AddResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.BatchResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SetPasswordResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SuspendResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
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
public class ResponseMsgTest {
    
    private JAXBContext ctx = null;
    
    public ResponseMsgTest() throws JAXBException {
        ctx = JAXBContext.newInstance(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private ResponseAccessor parse(JAXBElement<ResponseType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<ResponseType> out = (JAXBElement<ResponseType>) unmarshaller.unmarshal(bis);
        return ResponseAccessor.accessorForResponse(out.getValue());
    }
    
    //
    // BATCH
    //
    
    @Test
    public void testBatch() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        AddResponseBuilder addBuilder = ResponseBuilder.builderForAdd()
                .requestId("add-id")
                .success()
                .psoId(u.getUid())
                .psoTargetId("targetid")
                .xsdObject(u);
        SetPasswordResponseBuilder passBuilder = ResponseBuilder.builderForSetPassword()
                .requestId("set-password-id")
                .success();
        SuspendResponseBuilder susBuilder = ResponseBuilder.builderForSuspend()
                .requestId("suspend-id")
                .success();
        BatchResponseBuilder builder = ResponseBuilder.builderForBatch()
                .requestId("batch-id")
                .nestedResponse(addBuilder)
                .nestedResponse(passBuilder)
                .nestedResponse(susBuilder)
                .success();
        JAXBElement el = builder.build();
        BatchResponseAccessor res = parse(el).asBatch();
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals("batch-id", res.getRequestId());
        Assert.assertEquals(3, res.getNestedResponses().length);
        // add
        Assert.assertNotNull(res.getNestedResponses()[0].asAdd());
        AddResponseAccessor add = res.getNestedResponses()[0].asAdd();
        Assert.assertTrue(add.isSuccess());
        Assert.assertEquals(add.getRequestId(), "add-id");
        Assert.assertEquals(add.getPsoId(), u.getUid());
        Assert.assertEquals(add.getPsoTargetId(), "targetid");
        User other = (User) add.getXsdObject(User.class);
        Assert.assertEquals(other, u);
        // password
        ResponseAccessor pass = res.getNestedResponses()[1];
        Assert.assertTrue(pass.isSuccess());
        Assert.assertEquals(pass.getRequestId(), "set-password-id");
        // suspend
        ResponseAccessor sus = res.getNestedResponses()[2];
        Assert.assertTrue(sus.isSuccess());
        Assert.assertEquals(sus.getRequestId(), "suspend-id");
    }
}
