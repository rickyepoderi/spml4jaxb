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
package es.rickyepoderi.spml4jaxb.test.async;

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.CancelResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;
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
                es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory.class,
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
    // CANCEL
    //

    @Test
    public void testResponseCancel() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForCancel()
                .requestId("requestid")
                .success()
                .asyncRequestId("async-request-id")
                .build();
        CancelResponseAccessor res = parse(el).asCancel();
        Assert.assertTrue(res.isSuccess());
        Assert.assertTrue(res.isRequestId("requestid"));
        Assert.assertTrue(res.isAsyncRequestId("async-request-id"));
    }
    
    //
    // STATUS
    //
    
    @Test
    public void testResponseStatusAdd() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForStatus()
                .requestId("requestid")
                .success()
                .asyncRequestId("async-request-id")
                .nestedResponse(
                        ResponseBuilder.builderForAdd()
                        .requestId("add-request-id")
                        .success()
                        .psoId("psoid")
                        .psoTargetId("targetid")
                )
                .build();
        StatusResponseAccessor res = parse(el).asStatus();
        Assert.assertTrue(res.isSuccess());
        Assert.assertTrue(res.isRequestId("requestid"));
        Assert.assertTrue(res.isAsyncRequestId("async-request-id"));
        Assert.assertEquals(res.getNestedResponses().length, 1);
        AddResponseAccessor add = res.getNestedResponse().asAdd();
        Assert.assertTrue(add.isSuccess());
        Assert.assertTrue(add.isRequestId("add-request-id"));
        Assert.assertEquals(add.getPsoId(), "psoid");
        Assert.assertEquals(add.getPsoTargetId(), "targetid");
    }
    
    @Test
    public void testResponseStatusLookup() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForStatus()
                .requestId("requestid")
                .success()
                .asyncRequestId("async-request-id")
                .nestedResponse(
                        ResponseBuilder.builderForLookup()
                        .requestId("lookup-request-id")
                        .success()
                        .psoId("psoid")
                        .psoTargetId("targetid")
                        .dsmlAttribute("name", "value1", "value2")
                )
                .build();
        StatusResponseAccessor res = parse(el).asStatus();
        Assert.assertTrue(res.isSuccess());
        Assert.assertTrue(res.isRequestId("requestid"));
        Assert.assertTrue(res.isAsyncRequestId("async-request-id"));
        Assert.assertEquals(res.getNestedResponses().length, 1);
        LookupResponseAccessor lookup = res.getNestedResponse().asLookup();
        Assert.assertTrue(lookup.isSuccess());
        Assert.assertTrue(lookup.isRequestId("lookup-request-id"));
        Assert.assertEquals(lookup.getPsoId(), "psoid");
        Assert.assertEquals(lookup.getPsoTargetId(), "targetid");
        Map<String, DsmlAttr> attrs = lookup.getDsmlAttributesMap();
        Assert.assertEquals(attrs.size(), 1);
        Assert.assertTrue(attrs.containsKey("name"));
        Assert.assertEquals(attrs.get("name").getValue(), Arrays.asList(new String[]{"value1", "value2"}));
    }
}
