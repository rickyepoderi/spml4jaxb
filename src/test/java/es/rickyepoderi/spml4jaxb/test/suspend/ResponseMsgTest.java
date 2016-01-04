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
                es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory.class,
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
    // SUSPEND
    //

    @Test
    public void testSuspend() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForSuspend()
                .requestId("requestid")
                .success()
                .build();
        ResponseAccessor res = parse(el);
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    //
    // RESUME
    //
    
    @Test
    public void testResume() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForResume()
                .requestId("requestid")
                .success()
                .build();
        ResponseAccessor res = parse(el);
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    //
    // ACTIVE
    //
    
    @Test
    public void testActiveOk() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForActive()
                .requestId("requestid")
                .active()
                .success()
                .build();
        ActiveResponseAccessor res = parse(el).asActive();
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertTrue(res.isActive());
    }
    
    @Test
    public void testActiveKo() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForActive()
                .requestId("requestid")
                .notActive()
                .success()
                .build();
        ActiveResponseAccessor res = parse(el).asActive();
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertFalse(res.isActive());
    }
    
}
