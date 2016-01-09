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
package es.rickyepoderi.spml4jaxb.test.core;

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.accessor.AddRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.DeleteRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.PsoIdentifierAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import es.rickyepoderi.spml4jaxb.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

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
    
    //
    // LIST TARGETS
    //
    
    @Test
    public void testDsmlListTargets() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileDsml()
                .build();
        ListTargetsRequestAccessor req = parse(el).asListTargets();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isDsml());
    }
    
    @Test
    public void testXsdListTargets() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForListTargets()
                .synchronous()
                .requestId()
                .profileXsd()
                .build();
        ListTargetsRequestAccessor req = parse(el).asListTargets();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isXsd());
    }
    
    //
    // LOOKUP
    //
    
    @Test
    public void testLookup() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForLookup()
                .asynchronous()
                .requestId("request-lookup")
                .returnIdentifier()
                .psoIdentifier(RequestBuilder.builderForPsoIdentifier()
                        .id("testlookupid-1")
                        .targetId("testtargetid-1")
                        .container(RequestBuilder.builderForPsoIdentifier()
                                .id("testlookupid-2")
                                .targetId("testtargetid-2")
                                .container(RequestBuilder.builderForPsoIdentifier()
                                        .id("testlookupid-3")
                                        .targetId("testtargetid-3")
                                )
                        )
                )
                .build();
        LookupRequestAccessor req = parse(el).asLookup();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertTrue(req.isReturnIdentifier());
        Assert.assertEquals("request-lookup", req.getRequestId());
        PsoIdentifierAccessor psoId = req.getPsoAccessor();
        Assert.assertNotNull(psoId);
        Assert.assertEquals(psoId.getId(), "testlookupid-1");
        Assert.assertEquals(psoId.getTargetId(), "testtargetid-1");
        psoId = psoId.getContainer();
        Assert.assertNotNull(psoId);
        Assert.assertEquals(psoId.getId(), "testlookupid-2");
        Assert.assertEquals(psoId.getTargetId(), "testtargetid-2");
        psoId = psoId.getContainer();
        Assert.assertNotNull(psoId);
        Assert.assertEquals(psoId.getId(), "testlookupid-3");
        Assert.assertEquals(psoId.getTargetId(), "testtargetid-3");
    }
    
    //
    // DELETE
    //
    
    @Test
    public void testDelete() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForDelete()
                .asynchronous()
                .requestId("request-delete")
                .returnIdentifier()
                .psoId("testdeleteid")
                .psoTargetId("testtargetid")
                .build();
        DeleteRequestAccessor req = parse(el).asDelete();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertEquals("request-delete", req.getRequestId());
        Assert.assertEquals("testdeleteid", req.getPsoId());
        Assert.assertEquals("testtargetid", req.getPsoTargetId());
    }
    
    //
    // ADD
    //
    
    @Test
    public void testDsmlAdd() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForAdd()
                .requestId()
                .asynchronous()
                .returnData()
                .psoId("testadd")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .build();
        AddRequestAccessor req = parse(el).asAdd();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertTrue(req.isReturnData());
        Assert.assertEquals("testadd", req.getPsoId());
        Assert.assertEquals("testtargetid", req.getPsoTargetId());
        Assert.assertEquals("testcontainerid", req.getContainerId());
        Assert.assertEquals("testcontainertargetid", req.getContainerTargetId());
        Assert.assertEquals(req.getDsmlAttributes().length, 2);
        Assert.assertEquals(req.getDsmlAttributes()[0].getName(), "name1");
        Assert.assertEquals(req.getDsmlAttributes()[0].getValue(), Arrays.asList(new String[]{"value11", "value12", "value13"}));
        Assert.assertEquals(req.getDsmlAttributes()[1].getName(), "name2");
        Assert.assertEquals(req.getDsmlAttributes()[1].getValue(), Arrays.asList(new String[]{"value21"}));
    }
    
    @Test
    public void testXsdAdd() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setDescription("description");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = RequestBuilder.builderForAdd()
                .requestId()
                .synchronous()
                .returnEverything()
                .psoId("testadd")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .xsdObject(u)
                .build();
        AddRequestAccessor req = parse(el).asAdd();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isReturnEverything());
        Assert.assertEquals("testadd", req.getPsoId());
        Assert.assertEquals("testtargetid", req.getPsoTargetId());
        Assert.assertEquals("testcontainerid", req.getContainerId());
        Assert.assertEquals("testcontainertargetid", req.getContainerTargetId());
        User other = (User) req.getXsdObject(User.class);
        Assert.assertEquals(u, other);
    }
    
    //
    // MODIFY
    //
    
    @Test
    public void testDsmlModify() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForModify()
                .synchronous()
                .requestId()
                .returnData()
                .psoId("psotestmodifyid")
                .psoTargetId("psotargetid")
                .dsmlAdd("name-add", "add-value1", "add-value2")
                .dsmlDelete("name-delete", "delete-value1")
                .dsmlReplace("name-replace", "replace-value1", "replace-value2", "replace-value3")
                .build();
        ModifyRequestAccessor req = parse(el).asModify();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isReturnData());
        Assert.assertEquals("psotestmodifyid", req.getPsoId());
        Assert.assertEquals("psotargetid", req.getPsoTargetId());
        DsmlModification[] mods = req.getDsmlModifications();
        Assert.assertEquals(mods.length, 3);
        for (DsmlModification mod : mods) {
            if (req.isModeAdd(mod)) {
                Assert.assertEquals(mod.getName(), "name-add");
                Assert.assertEquals(mod.getValue(), Arrays.asList(new String[]{"add-value1", "add-value2"}));
            } else if (req.isModeDelete(mod)) {
                Assert.assertEquals(mod.getName(), "name-delete");
                Assert.assertEquals(mod.getValue(), Arrays.asList(new String[]{"delete-value1"}));
            } else if (req.isModeReplace(mod)) {
                Assert.assertEquals(mod.getName(), "name-replace");
                Assert.assertEquals(mod.getValue(), Arrays.asList(new String[]{"replace-value1", "replace-value2", "replace-value3"}));
            } else {
                throw new IllegalStateException("Invalid mode!");
            }
        }
    }
    
    @Test
    public void testXsdModifyWholeUser() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setDescription("description");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = RequestBuilder.builderForModify()
                .asynchronous()
                .requestId()
                .returnData()
                .psoId("psotestmodifyid")
                .psoTargetId("psotargetid")
                .xsdReplace("/user", u)
                .build();
        ModifyRequestAccessor req = parse(el).asModify();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertTrue(req.isReturnData());
        Assert.assertEquals("psotestmodifyid", req.getPsoId());
        Assert.assertEquals("psotargetid", req.getPsoTargetId());
        Assert.assertEquals(req.getModifications().length, 1);
        ModificationType mod = req.getModifications()[0];
        Assert.assertEquals(req.getModificationXPath(mod), "/user");
        Assert.assertTrue(req.isModeReplace(mod));
        Object[] objs = req.getXsdModificationObject(mod, User.class);
        Assert.assertEquals(objs.length, 1);
        User other = (User) objs[0];
        Assert.assertEquals(u, other);
    }
    
    @Test
    public void testXsdModifyFragments() throws JAXBException, SpmlException, ParserConfigurationException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setDescription("description");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = RequestBuilder.builderForModify()
                .synchronous()
                .requestId("modify-requestid")
                .returnData()
                .psoId("psotestmodifyid")
                .psoTargetId("psotargetid")
                .xsdAdd("/user", "<usr:role xmlns:usr=\"urn:ddbb-spml-dsml:user\">role3</usr:role>")
                .xsdDelete("/user/role[text()='role1']")
                .xsdReplace("/usr:user/usr:cn", "<usr:cn xmlns:usr=\"urn:ddbb-spml-dsml:user\">another cn</usr:cn>")
                .build();
        ModifyRequestAccessor req = parse(el).asModify();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertTrue(req.isReturnData());
        Assert.assertEquals(req.getRequestId(), "modify-requestid");
        Assert.assertEquals("psotestmodifyid", req.getPsoId());
        Assert.assertEquals("psotargetid", req.getPsoTargetId());
        ModificationType[] mods = req.getModifications();
        Assert.assertEquals(mods.length, 3);
        for (ModificationType mod : mods) {
            if (req.isModeAdd(mod)) {
                Document[] docs = req.getXsdModificationFragment(mod);
                Assert.assertEquals(req.getModificationXPath(mod), "/user");
                Assert.assertEquals(docs.length, 1);
                Assert.assertEquals("urn:ddbb-spml-dsml:user", docs[0].getDocumentElement().getNamespaceURI());
                Assert.assertEquals("role", docs[0].getDocumentElement().getLocalName());
                Assert.assertEquals("role3", docs[0].getDocumentElement().getTextContent());
            } else if (req.isModeDelete(mod)) {
                Assert.assertEquals(req.getModificationXPath(mod), "/user/role[text()='role1']");
            } else if (req.isModeReplace(mod)) {
                Document[] docs = req.getXsdModificationFragment(mod);
                Assert.assertEquals(req.getModificationXPath(mod), "/usr:user/usr:cn");
                Assert.assertEquals(docs.length, 1);
                Assert.assertEquals("urn:ddbb-spml-dsml:user", docs[0].getDocumentElement().getNamespaceURI());
                Assert.assertEquals("cn", docs[0].getDocumentElement().getLocalName());
                Assert.assertEquals("another cn", docs[0].getDocumentElement().getTextContent());
            } else {
                throw new IllegalStateException("Invalid mode!");
            }
        }
    }
}
