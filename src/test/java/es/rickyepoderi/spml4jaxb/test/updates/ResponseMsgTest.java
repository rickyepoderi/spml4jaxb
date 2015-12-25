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
package es.rickyepoderi.spml4jaxb.test.updates;

import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateKindType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
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
                es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private ResponseAccessor parse(JAXBElement<ResponseType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        marshaller.marshal(in, System.err);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<ResponseType> out = (JAXBElement<ResponseType>) unmarshaller.unmarshal(bis);
        return ResponseAccessor.accessorForResponse(out.getValue());
    }
    
    //
    // SUSPEND
    //

    @Test
    public void testUpdates() throws JAXBException {
        Date date1 = new Date(System.currentTimeMillis() - 86400000 * 2);
        Date date2 = new Date(System.currentTimeMillis() - 86400000);
        Date date3 = new Date(System.currentTimeMillis());
        JAXBElement el = ResponseBuilder.builderForUpdates()
                .requestId("updatesid")
                .updatePsoId("psoid1")
                .updatePsoTargetId("targetid1")
                .updateByCapability("capability1")
                .updateKind(UpdateKindType.ADD)
                .updateTimestamp(date1)
                .nextUpdate()
                .updatePsoId("psoid2")
                .updatePsoTargetId("targetid2")
                .updateByCapability("capability2")
                .updateKind(UpdateKindType.DELETE)
                .updateTimestamp(date2)
                .nextUpdate()
                .updatePsoId("psoid3")
                .updatePsoTargetId("targetid3")
                .updateByCapability("capability3")
                .updateKind(UpdateKindType.CAPABILITY)
                .updateTimestamp(date3)
                .iteratorId("iterator-id")
                .success()
                .build();
        UpdatesResponseAccessor res = parse(el).asUpdates();
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "updatesid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertEquals("iterator-id", res.getIteratorId());
        // first element
        res.start();
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getUpdatePsoId(), "psoid1");
        Assert.assertEquals(res.getUpdateTargetId(), "targetid1");
        Assert.assertEquals(res.getUpdateByCapability(), "capability1");
        Assert.assertTrue(res.isUpdateKindAdd());
        Assert.assertEquals(res.getUpdateTimestamp(), date1);
        // second element
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getUpdatePsoId(), "psoid2");
        Assert.assertEquals(res.getUpdateTargetId(), "targetid2");
        Assert.assertEquals(res.getUpdateByCapability(), "capability2");
        Assert.assertTrue(res.isUpdateKindDelete());
        Assert.assertEquals(res.getUpdateTimestamp(), date2);
        // third element
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getUpdatePsoId(), "psoid3");
        Assert.assertEquals(res.getUpdateTargetId(), "targetid3");
        Assert.assertEquals(res.getUpdateByCapability(), "capability3");
        Assert.assertTrue(res.isUpdateKindCability());
        Assert.assertEquals(res.getUpdateTimestamp(), date3);
        // end
        Assert.assertFalse(res.hasNext());
        // test iterator / iterable
        int i = 1;
        for (UpdatesResponseAccessor iter: res) {
            Assert.assertEquals(res.getUpdatePsoId(), "psoid" + i);
            i++;
        }
        Assert.assertEquals(4, i);
    }
    
    @Test
    public void testCloseIterator() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForUpdatesCloseIterator()
                .requestId("requestid")
                .success()
                .build();
        ResponseAccessor res = parse(el);
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    
}
