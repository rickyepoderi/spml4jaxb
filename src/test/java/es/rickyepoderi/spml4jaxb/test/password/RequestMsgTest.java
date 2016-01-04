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

import es.rickyepoderi.spml4jaxb.accessor.ExpirePasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
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
                es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory.class,
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
    
    //
    // SET PASSWORD
    //
    
    @Test
    public void testSetPassword() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForSetPassword()
                .synchronous()
                .requestId()
                .psoId("psoid")
                .psoTargetId("targetid")
                .password("password")
                .currentPassword("currentpassword")
                .build();
        SetPasswordRequestAccessor req = parse(el).asSetPassword();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
        Assert.assertEquals("password", req.getPassword());
        Assert.assertEquals("currentpassword", req.getCurrentPassword());
    }
    
    //
    // EXPIRE PASSWORD
    //
    
    @Test
    public void testExpirePassword() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForExpirePassword()
                .synchronous()
                .requestId()
                .psoId("psoid")
                .psoTargetId("targetid")
                .remainingLoggings(0)
                .build();
        ExpirePasswordRequestAccessor req = parse(el).asExpirePassword();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
        Assert.assertEquals(0, req.getRemainingLoggings());
    }
    
    @Test
    public void testExpirePassword2() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForExpirePassword()
                .synchronous()
                .requestId()
                .psoId("psoid")
                .psoTargetId("targetid")
                .build();
        ExpirePasswordRequestAccessor req = parse(el).asExpirePassword();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
        Assert.assertEquals(1, req.getRemainingLoggings());
    }
    
    //
    // RESET PASSWORD
    //
    
    @Test
    public void testResetPassword() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForResetPassword()
                .synchronous()
                .requestId()
                .psoId("psoid")
                .psoTargetId("targetid")
                .build();
        ResetPasswordRequestAccessor req = parse(el).asResetPassword();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
    }
    
    //
    // VALIDATE PASSWORD
    //
    
    @Test
    public void testValidatePassword() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForValidatePassword()
                .synchronous()
                .requestId()
                .psoId("psoid")
                .psoTargetId("targetid")
                .password("password")
                .build();
        ValidatePasswordRequestAccessor req = parse(el).asValidatePassword();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
        Assert.assertEquals("password", req.getPassword());
    }
    
}
