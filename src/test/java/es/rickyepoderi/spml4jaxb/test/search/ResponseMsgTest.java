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
package es.rickyepoderi.spml4jaxb.test.search;

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    protected static final Logger log = Logger.getLogger(RequestMsgTest.class.getName());
    
    private JAXBContext ctx;
    
    public ResponseMsgTest() throws JAXBException {
        ctx = JAXBContext.newInstance(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private ResponseAccessor parse(JAXBElement<ResponseType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        log.log(Level.FINE, new String(bos.toByteArray()));
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<ResponseType> out = (JAXBElement<ResponseType>) unmarshaller.unmarshal(bis);
        return BaseResponseAccessor.accessorForResponse(out.getValue());
    }
    
    //
    // SUSPEND
    //

    @Test
    public void testSearchXsd() throws JAXBException {
        User u1 = new User();
        u1.setUid("uid1");
        u1.setCn("User name 1");
        u1.setPassword("uid1");
        User u2 = new User();
        u2.setUid("uid2");
        u2.setCn("User name 2");
        u2.setPassword("uid2");
        u2.setDescription("User description 2");
        JAXBElement el = ResponseBuilder.builderForSearch()
                .requestId("searchid")
                .psoId("uid1")
                .psoTargetId("target1")
                .xsdObject(u1)
                .nextPso()
                .psoId("uid2")
                .psoTargetId("target1")
                .xsdObject(u2)
                .iteratorId("iterator-id")
                .success()
                .build();
        SearchResponseAccessor res = parse(el).asSearch();
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "searchid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertEquals("iterator-id", res.getIteratorId());
        // first element
        res.start();
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getPsoId(), "uid1");
        Assert.assertEquals(res.getPsoTargetId(), "target1");
        User user = (User) res.getXsdObject(User.class);
        Assert.assertEquals(user, u1);
        // second element
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getPsoId(), "uid2");
        Assert.assertEquals(res.getPsoTargetId(), "target1");
        user = (User) res.getXsdObject(User.class);
        Assert.assertEquals(user, u2);
        // end
        Assert.assertFalse(res.hasNext());
    }
    
    @Test
    public void testSearchDsml() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForSearch()
                .requestId("searchid")
                .psoId("pso-id-1")
                .psoTargetId("pso-target-is-1")
                .dsmlAttribute("name1", "value1-11")
                .dsmlAttribute("name2", "value1-21", "value1-22")
                .dsmlAttribute("name3", "value1-31", "value1-32", "value1-33")
                .nextPso()
                .psoId("pso-id-2")
                .psoTargetId("pso-target-is-2")
                .dsmlAttribute("name1", "value2-11")
                .dsmlAttribute("name2", "value2-21", "value2-22")
                .dsmlAttribute("name3", "value2-31", "value2-32", "value2-33")
                .iteratorId("iterator-id")
                .success()
                .build();
        SearchResponseAccessor res = parse(el).asSearch();
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "searchid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        Assert.assertEquals("iterator-id", res.getIteratorId());
        // first element
        res.start();
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getPsoId(), "pso-id-1");
        Assert.assertEquals(res.getPsoTargetId(), "pso-target-is-1");
        DsmlAttr[] attrs = res.getDsmlAttributes();
        Assert.assertEquals(attrs.length, 3);
        Assert.assertEquals(attrs[0].getName(), "name1");
        Assert.assertEquals(attrs[0].getValue(), Arrays.asList(new String[]{"value1-11"}));
        Assert.assertEquals(attrs[1].getName(), "name2");
        Assert.assertEquals(attrs[1].getValue(), Arrays.asList(new String[]{"value1-21", "value1-22"}));
        Assert.assertEquals(attrs[2].getName(), "name3");
        Assert.assertEquals(attrs[2].getValue(), Arrays.asList(new String[]{"value1-31", "value1-32", "value1-33"}));
        // second element
        Assert.assertTrue(res.hasNext());
        res.next();
        Assert.assertEquals(res.getPsoId(), "pso-id-2");
        Assert.assertEquals(res.getPsoTargetId(), "pso-target-is-2");
        attrs = res.getDsmlAttributes();
        Assert.assertEquals(attrs.length, 3);
        Assert.assertEquals(attrs[0].getName(), "name1");
        Assert.assertEquals(attrs[0].getValue(), Arrays.asList(new String[]{"value2-11"}));
        Assert.assertEquals(attrs[1].getName(), "name2");
        Assert.assertEquals(attrs[1].getValue(), Arrays.asList(new String[]{"value2-21", "value2-22"}));
        Assert.assertEquals(attrs[2].getName(), "name3");
        Assert.assertEquals(attrs[2].getValue(), Arrays.asList(new String[]{"value2-31", "value2-32", "value2-33"}));
        // end
        Assert.assertFalse(res.hasNext());
        // test iterator / iterable
        int i = 1;
        for (SearchResponseAccessor iter: res) {
            Assert.assertEquals(res.getPsoId(), "pso-id-" + i);
            i++;
        }
        Assert.assertEquals(i, 3);
    }
    
    @Test
    public void testCloseIterator() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForCloseIterator()
                .requestId("requestid")
                .success()
                .build();
        BaseResponseAccessor res = parse(el);
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertNull(res.getError());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    
}
