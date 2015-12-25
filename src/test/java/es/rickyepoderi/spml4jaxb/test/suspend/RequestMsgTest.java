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
package es.rickyepoderi.spml4jaxb.test.suspend;

import es.rickyepoderi.spml4jaxb.accessor.ActiveRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResumeRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SuspendRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Date;
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
                es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory.class,
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
    // SUSPEND
    //
    
    @Test
    public void testSuspend() throws JAXBException, SpmlException {
        Date date = new Date();
        JAXBElement el = RequestBuilder.builderForSuspend()
                .asynchronous()
                .requestId("suspendid")
                .psoId("psoid")
                .psoTargetId("targetid")
                .effectiveDate(date)
                .build();
        SuspendRequestAccessor req = parse(el).asSuspend();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertEquals("suspendid", req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
        Assert.assertEquals(date, req.getEffectiveDate());
    }
    
    //
    // RESUME
    //
    
    @Test
    public void testResume() throws JAXBException, SpmlException {
        Date date = new Date();
        JAXBElement el = RequestBuilder.builderForResume()
                .asynchronous()
                .requestId("resumeid")
                .psoId("psoid")
                .psoTargetId("targetid")
                .effectiveDate(date)
                .build();
        ResumeRequestAccessor req = parse(el).asResume();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertEquals("resumeid", req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
        Assert.assertEquals(date, req.getEffectiveDate());
    }
    
    //
    // ACTIVE
    //
    
    @Test
    public void testActive() throws JAXBException, SpmlException {
        JAXBElement el = RequestBuilder.builderForActive()
                .asynchronous()
                .requestId("activeid")
                .psoId("psoid")
                .psoTargetId("targetid")
                .build();
        ActiveRequestAccessor req = parse(el).asActive();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertEquals("activeid", req.getRequestId());
        Assert.assertEquals("psoid", req.getPsoId());
        Assert.assertEquals("targetid", req.getPsoTargetId());
    }
   
}
