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
import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.PsoIdentifierAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SchemaAccessor;
import es.rickyepoderi.spml4jaxb.accessor.TargetAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.AttributeDefinitionType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectClassDefinitionType;
import es.rickyepoderi.spml4jaxb.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

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
    
    //
    // SOME ERRORS
    //

    @Test
    public void testResponseMalformedRequest() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForDelete()
                .requestId("requestid")
                .failure()
                .malformedRequest()
                .errorMessage("error1")
                .errorMessage("error2")
                .build();
        BaseResponseAccessor res = parse(el);
        Assert.assertTrue(res.isFailure());
        Assert.assertTrue(res.isMalformedRequest());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertEquals(res.getErrorMessages().length, 2);
        Assert.assertEquals(res.getErrorMessages()[0], "error1");
        Assert.assertEquals(res.getErrorMessages()[1], "error2");
    }
    
    @Test
    public void testResponseUnsupportedOperation() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForModify()
                .requestId("requestid")
                .failure()
                .unsupportedOperation()
                .errorMessage("error1")
                .errorMessage("error2")
                .build();
        ModifyResponseAccessor res = parse(el).asModify();
        Assert.assertTrue(res.isFailure());
        Assert.assertTrue(res.isUnsupportedOperation());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertEquals(res.getErrorMessages().length, 2);
        Assert.assertEquals(res.getErrorMessages()[0], "error1");
        Assert.assertEquals(res.getErrorMessages()[1], "error2");
    }
    
    @Test
    public void testResponseUnsupportedIdentifierType() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForAdd()
                .requestId("requestid")
                .failure()
                .unsupportedIdentifierType()
                .errorMessage("error1")
                .build();
        AddResponseAccessor res = parse(el).asAdd();
        Assert.assertTrue(res.isFailure());
        Assert.assertTrue(res.isUnsupportedIdentifierType());
        Assert.assertEquals(res.getRequestId(), "requestid");
        Assert.assertEquals(res.getErrorMessages().length, 1);
        Assert.assertEquals(res.getErrorMessages()[0], "error1");
    }
    
    //
    // PENDING
    //
    
    @Test
    public void testResponsePending() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForAdd()
                .pending()
                .build();
        AddResponseAccessor res = parse(el).asAdd();
        Assert.assertTrue(res.isPending());
        Assert.assertNull(res.getError());
        Assert.assertNull(res.getRequestId());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    //
    // LIST TARGETS
    //
    
    @Test
    public void testResponseDsmlListTargets() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForListTargets()
                .requestId("request-listtargets")
                .success()
                .target(ResponseBuilder.builderForTarget()
                        .profileDsml()
                        .targetId("dsml-target")
                        .capabilityAsync()
                        .capabilityBatch()
                        .capabilityBulk()
                        .capabilityPassword("entity")
                        .capabilitySearch()
                        .capabilitySuspend("entity")
                        .capabilityUpdates()
                        .schema(ResponseBuilder.builderForSchema()
                                .supportedSchemaEntity("user")
                                .attributeDefinition("uid", "User name")
                                .attributeDefinition("password", "password for the user")
                                .attributeDefinition("cn", "Common name")
                                .attributeDefinition("description", "description")
                                .attributeDefinition("role", "roles of the user", true)
                                .objectclassDefinition("user")
                                .objectclassDefinitionAttrDefRequired("user", "uid", "password", "cn")
                                .objectclassDefinitionAttrDef("user", "description", "role")
                        )
                ).build();
        ListTargetsResponseAccessor res = parse(el).asListTargets();
        Assert.assertEquals(res.getRequestId(), "request-listtargets");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        TargetAccessor[] targets = res.getTargets();
        Assert.assertEquals(targets.length, 1);
        TargetAccessor target = targets[0];
        Assert.assertTrue(target.isDsml());
        Assert.assertEquals(target.getTargetId(), "dsml-target");
        SchemaAccessor[] schemas = target.getSchemas();
        Assert.assertEquals(schemas.length, 1);
        SchemaAccessor schema = schemas[0];
        Assert.assertEquals(schema.getSupportedEntityNames(), new HashSet(Arrays.asList(new String[]{"user"})));
        AttributeDefinitionType[] attrs = schema.getDsmlAttributeDefinitions();
        Assert.assertEquals(attrs.length, 5);
        ObjectClassDefinitionType[] objs = schema.getDsmlObjectClassDefinition();
        Assert.assertEquals(objs.length, 1);
        Assert.assertEquals(objs[0].getMemberAttributes().getAttributeDefinitionReference().size(), 5);
        Assert.assertTrue(target.hasAsyncCapability());
        Assert.assertTrue(target.hasBatchCapability());
        Assert.assertTrue(target.hasBulkCapability());
        Assert.assertTrue(target.hasSearchCapability());
        Assert.assertTrue(target.hasUpdatesCapability());
        Assert.assertTrue(target.hasSuspendCapabilityFor("entity"));
        Assert.assertTrue(target.hasPasswordCapabilityFor("entity"));
    }
    
    @Test
    public void testResponseXsdListTargets() throws JAXBException, SpmlException {
        JAXBElement el = ResponseBuilder.builderForListTargets()
                .requestId("request-listtargets")
                .success()
                .target(ResponseBuilder.builderForTarget()
                        .profileXsd()
                        .targetId("xsd-target")
                        .capabilityAsync()
                        .capabilitySearch()
                        .capabilityPassword("entity")
                        .schema(ResponseBuilder.builderForSchema()
                                .supportedSchemaEntity("user")
                                .xsdSchema(new ByteArrayInputStream(
                                                ("<?xml version=\"1.0\"?>"
                                                + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://www.w3.org/2001/XMLSchema\" xmlns:spml=\"urn:oasis:names:tc:SPML:2:0\" elementFormDefault=\"qualified\" targetNamespace=\"urn:ddbb-spml-dsml:user\" version=\"1.0\">"
                                                + "  <element name=\"user\">"
                                                + "    <complexType>"
                                                + "      <sequence>"
                                                + "        <element maxOccurs=\"1\" minOccurs=\"1\" name=\"uid\" type=\"string\"/>"
                                                + "        <element maxOccurs=\"1\" minOccurs=\"0\" name=\"password\" type=\"string\"/>"
                                                + "        <element maxOccurs=\"1\" minOccurs=\"1\" name=\"cn\" type=\"string\"/>"
                                                + "        <element maxOccurs=\"1\" minOccurs=\"0\" name=\"description\" type=\"string\"/>"
                                                + "        <element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"role\" type=\"string\"/>"
                                                + "      </sequence>"
                                                + "    </complexType>"
                                                + "  </element>"
                                                + "</xs:schema>").getBytes(StandardCharsets.UTF_8)))
                        )
                ).build();
        ListTargetsResponseAccessor res = parse(el).asListTargets();
        Assert.assertEquals(res.getRequestId(), "request-listtargets");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        TargetAccessor[] targets = res.getTargets();
        Assert.assertEquals(targets.length, 1);
        TargetAccessor target = targets[0];
        Assert.assertTrue(target.isXsd());
        Assert.assertEquals(target.getTargetId(), "xsd-target");
        SchemaAccessor[] schemas = target.getSchemas();
        Assert.assertEquals(schemas.length, 1);
        SchemaAccessor schema = schemas[0];
        Assert.assertEquals(schema.getSupportedEntityNames(), new HashSet(Arrays.asList(new String[]{"user"})));
        Document doc = schema.getXsdSchemaDocument();
        Assert.assertNotNull(doc);
        Assert.assertEquals(doc.getDocumentElement().getNamespaceURI(), "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals(doc.getDocumentElement().getLocalName(), "schema");
        Assert.assertTrue(target.hasAsyncCapability());
        Assert.assertFalse(target.hasBatchCapability());
        Assert.assertFalse(target.hasBulkCapability());
        Assert.assertTrue(target.hasSearchCapability());
        Assert.assertFalse(target.hasUpdatesCapability());
        Assert.assertFalse(target.hasSuspendCapabilityFor("entity"));
        Assert.assertTrue(target.hasPasswordCapabilityFor("entity"));
    }
    
    //
    // DELETE
    //
    
    @Test
    public void testResponseDelete() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForDelete()
                .requestId("request-delete")
                .success()
                .build();
        BaseResponseAccessor res = parse(el);
        Assert.assertEquals(res.getRequestId(), "request-delete");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
    }
    
    //
    // LOOKUP
    //
    
    @Test
    public void testResponseDsmlLookup() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForLookup()
                .requestId("response-lookup")
                .success()
                .psoIdentifier(ResponseBuilder.builderForPsoIdentifier()
                        .id("testlookupid-1")
                        .targetId("testtargetid-1")
                        .container(ResponseBuilder.builderForPsoIdentifier()
                                .id("testlookupid-2")
                                .targetId("testtargetid-2")
                                .container(ResponseBuilder.builderForPsoIdentifier()
                                        .id("testlookupid-3")
                                        .targetId("testtargetid-3")
                                )
                        )
                )
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .dsmlAttribute("name3", "value31", "value32")
                .build();
        LookupResponseAccessor res = parse(el).asLookup();
        Assert.assertEquals(res.getRequestId(), "response-lookup");
        PsoIdentifierAccessor psoId = res.getPsoAccessor();
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
    public void testResponseXsdLookup() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = ResponseBuilder.builderForLookup()
                .requestId("request-lookup")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .xsdObject(u)
                .build();
        LookupResponseAccessor res = parse(el).asLookup();
        Assert.assertEquals(res.getRequestId(), "request-lookup");
        Assert.assertEquals(res.getPsoId(), "psoid");
        Assert.assertEquals(res.getPsoTargetId(), "targetid");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        User other = (User) res.getXsdObject(User.class);
        Assert.assertEquals(other, u);
    }
    
    //
    // ADD
    //
    
    @Test
    public void testResponseDsmlAdd() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForAdd()
                .requestId("add-lookup")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .dsmlAttribute("name3", "value31", "value32")
                .build();
        AddResponseAccessor res = parse(el).asAdd();
        Assert.assertEquals(res.getRequestId(), "add-lookup");
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
    public void testResponseXsdAdd() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = ResponseBuilder.builderForAdd()
                .requestId("request-lookup")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .xsdObject(u)
                .build();
        AddResponseAccessor res = parse(el).asAdd();
        Assert.assertEquals(res.getRequestId(), "request-lookup");
        Assert.assertEquals(res.getPsoId(), "psoid");
        Assert.assertEquals(res.getPsoTargetId(), "targetid");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        User other = (User) res.getXsdObject(User.class);
        Assert.assertEquals(other, u);
    }
    
    //
    // MODIFY
    //
    
    @Test
    public void testResponseDsmlModify() throws JAXBException {
        JAXBElement el = ResponseBuilder.builderForModify()
                .requestId("add-lookup")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .dsmlAttribute("name3", "value31", "value32")
                .build();
        ModifyResponseAccessor res = parse(el).asModify();
        Assert.assertEquals(res.getRequestId(), "add-lookup");
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
    public void testResponseXsdModify() throws JAXBException {
        User u = new User();
        u.setUid("uid");
        u.setCn("cn");
        u.setPassword("password");
        u.getRole().add("role1");
        u.getRole().add("role2");
        JAXBElement el = ResponseBuilder.builderForModify()
                .requestId("request-lookup")
                .success()
                .psoId("psoid")
                .psoTargetId("targetid")
                .xsdObject(u)
                .build();
        ModifyResponseAccessor res = parse(el).asModify();
        Assert.assertEquals(res.getRequestId(), "request-lookup");
        Assert.assertEquals(res.getPsoId(), "psoid");
        Assert.assertEquals(res.getPsoTargetId(), "targetid");
        Assert.assertNull(res.getError());
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals(res.getErrorMessages().length, 0);
        User other = (User) res.getXsdObject(User.class);
        Assert.assertEquals(other, u);
    }
    

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
