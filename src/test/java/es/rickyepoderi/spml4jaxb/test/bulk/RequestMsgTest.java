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
package es.rickyepoderi.spml4jaxb.test.bulk;

import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BulkModifyRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.builder.BulkDeleteRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.BulkModifyRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.FilterBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.SearchQueryBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
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
                es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class);
    }
    
    private RequestAccessor parse(JAXBElement<RequestType> in) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        marshaller.marshal(in, bos);
        marshaller.marshal(in, System.err);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<RequestType> out = (JAXBElement<RequestType>) unmarshaller.unmarshal(bis);
        return RequestAccessor.accessorForRequest(out.getValue());
    }
    
    //
    // BULK MODIFY
    //
    
    @Test
    public void testDsmlBulkModify() throws JAXBException {
        SearchQueryBuilder queryBuilder = RequestBuilder.builderForQuery()
                .basePsoId("base-id")
                .basePsoTargetId("base-target-id")
                .scopeOneLevel()
                .targetId("target-id")
                .dsmlFilter(
                        FilterBuilder.and(
                                FilterBuilder.equals("name-equals", "value-equals"),
                                FilterBuilder.greaterOrEqual("name-ge", "value-ge"),
                                FilterBuilder.lessOrEqual("name-le", "value-le"),
                                FilterBuilder.or(
                                        FilterBuilder.present("name-present"),
                                        FilterBuilder.approxMatch("name-aprox", "value-aprox"),
                                        FilterBuilder.startsWith("name-sw", "value-sw"),
                                        FilterBuilder.endsWith("name-ew", "value-ew")
                                ),
                                FilterBuilder.not(FilterBuilder.contains("name-contains", "value-contains"))
                        )
                );
        BulkModifyRequestBuilder builder = RequestBuilder.builderForBulkModify()
                .synchronous()
                .requestId("bulk-modify-id")
                .query(queryBuilder)
                .dsmlAdd("name-add", "add-value1", "add-value2")
                .dsmlDelete("name-delete", "delete-value1")
                .dsmlReplace("name-replace", "replace-value1", "replace-value2", "replace-value3");
        JAXBElement el = builder.build();
        BulkModifyRequestAccessor req = parse(el).asBulkModify();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertEquals("bulk-modify-id", req.getRequestId());
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
        SearchQueryAccessor query = req.getQuery();
        Assert.assertNotNull(query);
        Assert.assertEquals("base-id", query.getBasePsoId());
        Assert.assertEquals("base-target-id", query.getBasePsoTargetId());
        Assert.assertEquals("target-id", query.getTargetId());
        Assert.assertTrue(query.isScopeOneLevel());
        FilterAccessor filter = query.getQueryFilter();
        FilterAccessor origFilter = builder.asAccessor().asBulkModify().getQuery().getQueryFilter();
        Assert.assertEquals(filter.toString(), origFilter.toString());
    }
    
    @Test
    public void testXsdBulkModify() throws JAXBException, SpmlException, ParserConfigurationException {
        SearchQueryBuilder queryBuilder = RequestBuilder.builderForQuery()
                .basePsoId("base-id")
                .basePsoTargetId("base-target-id")
                .scopeOneLevel()
                .targetId("target-id")
                .dsmlFilter(
                        FilterBuilder.or(
                                FilterBuilder.equals("name-equals", "value-equals"),
                                FilterBuilder.not(FilterBuilder.greaterOrEqual("name-ge", "value-ge")),
                                FilterBuilder.lessOrEqual("name-le", "value-le"),
                                FilterBuilder.and(
                                        FilterBuilder.present("name-present"),
                                        FilterBuilder.approxMatch("name-aprox", "value-aprox"),
                                        FilterBuilder.startsWith("name-sw", "value-sw"),
                                        FilterBuilder.endsWith("name-ew", "value-ew")
                                ),
                                FilterBuilder.not(FilterBuilder.contains("name-contains", "value-contains"))
                        )
                );
        BulkModifyRequestBuilder builder = RequestBuilder.builderForBulkModify()
                .synchronous()
                .requestId("bulk-modify-id")
                .query(queryBuilder)
                .xsdAdd("/user", "<usr:role xmlns:usr=\"urn:ddbb-spml-dsml:user\">role3</usr:role>")
                .xsdDelete("/user[role='role1']")
                .xsdReplace("/usr:user/usr:cn", "<usr:cn xmlns:usr=\"urn:ddbb-spml-dsml:user\">another cn</usr:cn>");
        JAXBElement el = builder.build();
        BulkModifyRequestAccessor req = parse(el).asBulkModify();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertEquals("bulk-modify-id", req.getRequestId());
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
                Assert.assertEquals(req.getModificationXPath(mod), "/user[role='role1']");
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
        SearchQueryAccessor query = req.getQuery();
        Assert.assertNotNull(query);
        Assert.assertEquals("base-id", query.getBasePsoId());
        Assert.assertEquals("base-target-id", query.getBasePsoTargetId());
        Assert.assertEquals("target-id", query.getTargetId());
        Assert.assertTrue(query.isScopeOneLevel());
        FilterAccessor filter = query.getQueryFilter();
        FilterAccessor origFilter = builder.asAccessor().asBulkModify().getQuery().getQueryFilter();
        Assert.assertEquals(filter.toString(), origFilter.toString());
    }
    
    //
    // BULK DELETE
    //
    
    @Test
    public void testBulkDelete() throws JAXBException {
        SearchQueryBuilder queryBuilder = RequestBuilder.builderForQuery()
                .basePsoId("base-id")
                .basePsoTargetId("base-target-id")
                .scopeOneLevel()
                .targetId("target-id")
                .dsmlFilter(
                        FilterBuilder.and(
                                FilterBuilder.equals("name-equals", "value-equals"),
                                FilterBuilder.greaterOrEqual("name-ge", "value-ge"),
                                FilterBuilder.lessOrEqual("name-le", "value-le"),
                                FilterBuilder.present("name-present"),
                                FilterBuilder.and(
                                        FilterBuilder.approxMatch("name-aprox", "value-aprox"),
                                        FilterBuilder.startsWith("name-sw", "value-sw"),
                                        FilterBuilder.endsWith("name-ew", "value-ew")
                                ),
                                FilterBuilder.not(FilterBuilder.contains("name-contains", "value-contains"))
                        )
                );
        BulkDeleteRequestBuilder builder = RequestBuilder.builderForBulkDelete()
                .synchronous()
                .requestId("bulk-delete-id")
                .query(queryBuilder);
        JAXBElement el = builder.build();
        BulkDeleteRequestAccessor req = parse(el).asBulkDelete();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertEquals("bulk-delete-id", req.getRequestId());
        SearchQueryAccessor query = req.getQuery();
        Assert.assertNotNull(query);
        Assert.assertEquals("base-id", query.getBasePsoId());
        Assert.assertEquals("base-target-id", query.getBasePsoTargetId());
        Assert.assertEquals("target-id", query.getTargetId());
        Assert.assertTrue(query.isScopeOneLevel());
        FilterAccessor filter = query.getQueryFilter();
        FilterAccessor origFilter = builder.asAccessor().asBulkDelete().getQuery().getQueryFilter();
        Assert.assertEquals(filter.toString(), origFilter.toString());
    }
    
}
