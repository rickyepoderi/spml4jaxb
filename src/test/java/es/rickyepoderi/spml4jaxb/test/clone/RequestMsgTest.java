/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
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
public class RequestMsgTest {
    
    private JAXBContext ctx = null;
    
    public RequestMsgTest() throws JAXBException {
        ctx = JAXBContext.newInstance(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.test.clone.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private RequestAccessor parse(JAXBElement<RequestType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<RequestType> out = (JAXBElement<RequestType>) unmarshaller.unmarshal(bis);
        return BaseRequestAccessor.accessorForRequest(out.getValue());
    }
    
    @Test
    public void testDsmlClone() throws JAXBException {
        JAXBElement el = CloneRequestBuilder.builderForClone()
                .synchronous()
                .requestId()
                .returnData()
                .psoId("testclone")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .templateId("templateid")
                .templateTargetId("templatetargetid")
                .build();
        CloneRequestAccessor req = parse(el).asAccessor(CloneRequestAccessor.emptyCloneAccessor());
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isReturnData());
        Assert.assertEquals("testclone", req.getPsoId());
        Assert.assertEquals("testtargetid", req.getPsoTargetId());
        Assert.assertEquals("testcontainerid", req.getContainerId());
        Assert.assertEquals("testcontainertargetid", req.getContainerTargetId());
        Assert.assertEquals(req.getDsmlAttributes().length, 2);
        Assert.assertEquals(req.getDsmlAttributes()[0].getName(), "name1");
        Assert.assertEquals(req.getDsmlAttributes()[0].getValue(), Arrays.asList(new String[]{"value11", "value12", "value13"}));
        Assert.assertEquals(req.getDsmlAttributes()[1].getName(), "name2");
        Assert.assertEquals(req.getDsmlAttributes()[1].getValue(), Arrays.asList(new String[]{"value21"}));
        Assert.assertEquals(req.getTemplateId(), "templateid");
        Assert.assertEquals(req.getTemplateTargetId(), "templatetargetid");
    }
    
    @Test
    public void testXsdClone() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setDescription("description");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = CloneRequestBuilder.builderForClone()
                .requestId()
                .synchronous()
                .returnEverything()
                .psoId("testclone")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .xsdObject(u)
                .templateId("templateid")
                .templateTargetId("templatetargetid")
                .build();
        CloneRequestAccessor req = parse(el).asAccessor(CloneRequestAccessor.emptyCloneAccessor());
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isReturnEverything());
        Assert.assertEquals("testclone", req.getPsoId());
        Assert.assertEquals("testtargetid", req.getPsoTargetId());
        Assert.assertEquals("testcontainerid", req.getContainerId());
        Assert.assertEquals("testcontainertargetid", req.getContainerTargetId());
        User other = (User) req.getXsdObject(User.class);
        Assert.assertEquals(u, other);
        Assert.assertEquals(req.getTemplateId(), "templateid");
        Assert.assertEquals(req.getTemplateTargetId(), "templatetargetid");
    }
}
