/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
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
                es.rickyepoderi.spml4jaxb.test.clone.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private ResponseAccessor parse(JAXBElement<ResponseType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<ResponseType> out = (JAXBElement<ResponseType>) unmarshaller.unmarshal(bis);
        return BaseResponseAccessor.accessorForResponse(out.getValue());
    }
    
    @Test
    public void testResponseDsmlClone() throws JAXBException {
        JAXBElement el = CloneResponseBuilder.builderForClone()
                .requestId("clone")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .dsmlAttribute("name3", "value31", "value32")
                .build();
        CloneResponseAccessor res = parse(el).asAccessor(CloneResponseAccessor.emptyResponseAccessor());
        Assert.assertEquals(res.getRequestId(), "clone");
        Assert.assertEquals(res.getPsoId(), "psoid");
        Assert.assertEquals(res.getPsoTargetId(), "targetid");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        DsmlAttr[] attrs = res.getDsmlAttributes();
        Assert.assertEquals(attrs.length, 3);
        Assert.assertEquals(attrs[0].getName(), "name1");
        Assert.assertEquals(attrs[0].getValue(), Arrays.asList(new String[]{"value11", "value12", "value13"}));
        Assert.assertEquals(attrs[1].getName(), "name2");
        Assert.assertEquals(attrs[1].getValue(), Arrays.asList(new String[]{"value21"}));
        Assert.assertEquals(attrs[2].getName(), "name3");
        Assert.assertEquals(attrs[2].getValue(), Arrays.asList(new String[]{"value31", "value32"}));
    }
    
    @Test
    public void testResponseXsdClone() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = CloneResponseBuilder.builderForClone()
                .requestId("clone")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .xsdObject(u)
                .build();
        CloneResponseAccessor res = parse(el).asAccessor(CloneResponseAccessor.emptyResponseAccessor());
        Assert.assertEquals(res.getRequestId(), "clone");
        Assert.assertEquals(res.getPsoId(), "psoid");
        Assert.assertEquals(res.getPsoTargetId(), "targetid");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        User other = (User) res.getXsdObject(User.class);
        Assert.assertEquals(other, u);
    }
}
