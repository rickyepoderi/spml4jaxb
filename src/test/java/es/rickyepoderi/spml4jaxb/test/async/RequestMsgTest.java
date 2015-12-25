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
package es.rickyepoderi.spml4jaxb.test.async;

import es.rickyepoderi.spml4jaxb.accessor.CancelRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusRequestAccessor;
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
    // CANCEL
    //
    @Test
    public void testCancel() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForCancel()
                .synchronous()
                .requestId()
                .asyncRequestId("async-request-id")
                .build();
        CancelRequestAccessor req = parse(el).asCancel();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertTrue(req.isAsyncRequestId("async-request-id"));
    }


    //
    // STATUS
    //
    
    @Test
    public void testStatusReturn() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForStatus()
                .synchronous()
                .requestId()
                .asyncRequestId("async-request-id")
                .returnResults()
                .build();
        StatusRequestAccessor req = parse(el).asStatus();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertTrue(req.isAsyncRequestId("async-request-id"));
        Assert.assertTrue(req.isReturnResults());
    }
    
    @Test
    public void testStatusNoReturn() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForStatus()
                .synchronous()
                .requestId()
                .asyncRequestId("async-request-id")
                .build();
        StatusRequestAccessor req = parse(el).asStatus();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertNotNull(req.getRequestId());
        Assert.assertTrue(req.isAsyncRequestId("async-request-id"));
        Assert.assertFalse(req.isReturnResults());
    }
}
