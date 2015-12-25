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
package es.rickyepoderi.spml4jaxb.test.password;

import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
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
    
    private JAXBContext ctx;
    
    public ResponseMsgTest() throws JAXBException {
        ctx = JAXBContext.newInstance(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory.class,
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
    // SET PASSWORD
    //
    
    @Test
    public void testSetPassword() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForSetPassword()
                .requestId("request-set-password")
                .success()
                .build();
        ResponseAccessor res = parse(el);
        Assert.assertEquals(res.getRequestId(), "request-set-password");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    //
    // EXPIRE PASSWORD
    //
    
    @Test
    public void testExpirePassword() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForExpirePassword()
                .requestId("request-expire-password")
                .success()
                .build();
        ResponseAccessor res = parse(el);
        Assert.assertEquals(res.getRequestId(), "request-expire-password");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    //
    // RESET PASSWORD
    //
    
    @Test
    public void testResetPassword() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForResetPassword()
                .requestId("request-reset-password")
                .password("password")
                .success()
                .build();
        ResetPasswordResponseAccessor res = parse(el).asResetPassword();
        Assert.assertEquals(res.getRequestId(), "request-reset-password");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertEquals(res.getPassword(), "password");
    }
    
    //
    // VALIDATE PASSWORD
    //
    
    @Test
    public void testValidatePasswordOk() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForValidatePassword()
                .requestId("request-reset-password")
                .valid()
                .success()
                .build();
        ValidatePasswordResponseAccessor res = parse(el).asValidatePassword();
        Assert.assertEquals(res.getRequestId(), "request-reset-password");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertTrue(res.isValid());
    }
    
    @Test
    public void testValidatePasswordKo() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForValidatePassword()
                .requestId("request-reset-password")
                .notValid()
                .success()
                .build();
        ValidatePasswordResponseAccessor res = parse(el).asValidatePassword();
        Assert.assertEquals(res.getRequestId(), "request-reset-password");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertFalse(res.isValid());
    }
    
}
