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

import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesCloseIteratorRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesIterateRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.FilterBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.SearchQueryBuilder;
import es.rickyepoderi.spml4jaxb.builder.UpdatesRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Arrays;
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
                es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory.class,
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
    // SEARCH
    //
    
    @Test
    public void testUpdatesDsml() throws JAXBException {
        SearchQueryBuilder searchQueryBuilder = RequestBuilder.builderForQuery()
                .basePsoId("base-id")
                .basePsoTargetId("base-target-id")
                .scopeOneLevel()
                .targetId("target-id")
                .dsmlAttributes("attribute1", "attribute2", "attribute3")
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
        Date since = new Date();
        UpdatesRequestBuilder searchRequestBuilder = RequestBuilder.builderForUpdates()
                .synchronous()
                .requestId("updatesid")
                .updatedByCapability("capability1", "capability2")
                .updatedSince(since)
                .token("token")
                .maxSelect(100)
                .query(searchQueryBuilder);
        JAXBElement el = searchRequestBuilder.build();
        UpdatesRequestAccessor req = parse(el).asUpdates();
        Assert.assertEquals(req.getRequestId(), "updatesid");
        Assert.assertTrue(req.isSynchronous());
        Assert.assertEquals(Arrays.asList(new String[]{"capability1", "capability2"}), Arrays.asList(req.getUpdatedByCapability()));
        Assert.assertEquals(100, req.getMaxSelect());
        Assert.assertEquals("token", req.getToken());
        Assert.assertEquals(since, req.getUpdatedSince());
        SearchQueryAccessor query = req.getQuery();
        Assert.assertNotNull(query);
        Assert.assertEquals("base-id", query.getBasePsoId());
        Assert.assertEquals("base-target-id", query.getBasePsoTargetId());
        Assert.assertEquals("target-id", query.getTargetId());
        Assert.assertTrue(query.isScopeOneLevel());
        Assert.assertEquals(Arrays.asList(new String[]{"attribute1", "attribute2", "attribute3"}), Arrays.asList(query.getDsmlAttributes()));
        FilterAccessor filter = query.getQueryFilter();
        FilterAccessor origFilter = searchRequestBuilder.asAccessor().asUpdates().getQuery().getQueryFilter();
        Assert.assertEquals(filter.toString(), origFilter.toString());
    }
    
    @Test
    public void testUpdatesXsd() throws JAXBException {
        Date since = new Date();
        JAXBElement el = RequestBuilder.builderForUpdates()
                .asynchronous()
                .requestId("updatesid")
                .updatedByCapability("capability1", "capability2")
                .updatedSince(since)
                .token("token")
                .maxSelect(100)
                .query(RequestBuilder.builderForQuery()
                        .basePsoId("base-id")
                        .basePsoTargetId("base-target-id")
                        .scopeSubTree()
                        .targetId("target-id")
                        .xsdXPathSelection("//user/email='lala@example.com'")
                ).build();
        UpdatesRequestAccessor req = parse(el).asUpdates();
        Assert.assertEquals(req.getRequestId(), "updatesid");
        Assert.assertEquals(100, req.getMaxSelect());
        Assert.assertEquals("token", req.getToken());
        Assert.assertEquals(since, req.getUpdatedSince());
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertEquals(Arrays.asList(new String[]{"capability1", "capability2"}), Arrays.asList(req.getUpdatedByCapability()));
        SearchQueryAccessor query = req.getQuery();
        Assert.assertNotNull(query);
        Assert.assertEquals("base-id", query.getBasePsoId());
        Assert.assertEquals("base-target-id", query.getBasePsoTargetId());
        Assert.assertEquals("target-id", query.getTargetId());
        Assert.assertTrue(query.isScopeSubTree());
        Assert.assertEquals(query.getXsdXPathSelection(), "//user/email='lala@example.com'");
    }
    
    //
    // ITERATE
    //
    
    @Test
    public void testIterate() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForUpdatesIterate()
                .synchronous()
                .requestId("iteratorid")
                .iteratorId("iterid")
                .build();
        UpdatesIterateRequestAccessor req = parse(el).asUpdatesIterate();
        Assert.assertTrue(req.isSynchronous());
        Assert.assertEquals("iteratorid", req.getRequestId());
        Assert.assertEquals("iterid", req.getIteratorId());
    }
    
    //
    // CLOSE ITERATOR
    //
    
    @Test
    public void testCloseIterator() throws JAXBException {
        JAXBElement el = RequestBuilder.builderForUpdatesCloseIterator()
                .asynchronous()
                .requestId("close-ierator-id")
                .iteratorId("iterid")
                .build();
        UpdatesCloseIteratorRequestAccessor req = parse(el).asUpdatesCloseIterator();
        Assert.assertTrue(req.isAsynchronous());
        Assert.assertEquals("close-ierator-id", req.getRequestId());
        Assert.assertEquals("iterid", req.getIteratorId());
    }
   
}
