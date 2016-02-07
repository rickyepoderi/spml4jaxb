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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesResponseAccessor;
import static es.rickyepoderi.spml4jaxb.builder.RequestBuilder.dataTypeFactory;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Updates request. The updates
 * operation is defined inside the updates capability. The updates capability 
 * allow a requestor to obtain in a scalable manner every recorded update (i.e., 
 * modification to an object) that matches specified selection criteria. The
 * updates operates in the same manner than search, it return the update 
 * records in pages. So again the same technique is used, the updates return
 * the first page of records and, if there are more to come, it appends a
 * iterator identifier in the response. With that id the requestor can
 * request the next page (Iterate operation). If it is needed a third page
 * another iterator id is used in the second page. Finally a CloseIterator
 * in this capability frees any resources in the server related to the updates
 * operation.</p>
 * 
 * <p>The updates operation let the requestor to specify what updates want to
 * be returned. There are several properties to select the updates wanted.
 * Besides the standard gives a non-specified token parameter the implementation
 * can use for anything (usually to mark the next updates operation and 
 * start the next operation from that point).</p>
 * 
 * @author ricky
 */
public class UpdatesRequestBuilder extends RequestBuilder<UpdatesRequestType, UpdatesRequestBuilder, 
        UpdatesRequestAccessor, UpdatesResponseAccessor> {

    /**
     * Constructor for a new updates request builder.
     */
    protected UpdatesRequestBuilder() {
        super(new UpdatesRequestType());
    }
    
    /**
     * Setter for the query criteria.  A <em>query</em> describes criteria that 
     * (the provider must use to) select objects on a target. The provider will 
     * return only updates that affect objects that match these criteria. 
     * 
     * @param query The builder fully filled to assign the query
     * @return The same builder
     */
    public UpdatesRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }
    
    /**
     * Setter for the capabilities in the request.  The updates operation can
     * contain a list of URNs of an XML namespace that uniquely identifies a 
     * capability. The provider should return updates that reflect changes to 
     * capability-specific data, updates produced by those capabilities.
     * 
     * @param capability The list of capabilities to take in consideration
     * @return The same builder
     */
    public UpdatesRequestBuilder updatedByCapability(String... capability) {
        request.getUpdatedByCapability().addAll(Arrays.asList(capability));
        return this;
    }
    
    /**
     * Setter for the updated since property using a date.  The provider will 
     * return only updates with a timestamp greater than this value.
     * 
     * @param since The date since the requestor wants the updates
     * @return The same builder
     */
    public UpdatesRequestBuilder updatedSince(Date since) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(since);
        return updatedSince(c);
    }
    
    /**
     * Setter for the updated since property using a calendar.  The provider will 
     * return only updates with a timestamp greater than this value.
     * 
     * @param since The date since the requestor wants the updates
     * @return The same builder
     */
    public UpdatesRequestBuilder updatedSince(Calendar since) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(since.getTime());
        return updatedSince(c);
    }
    
    /**
     * Setter for the updated since property using a gregorian calendar.  The provider will 
     * return only updates with a timestamp greater than this value.
     * 
     * @param since The date since the requestor wants the updates
     * @return The same builder
     */
    public UpdatesRequestBuilder updatedSince(GregorianCalendar since) {
        request.setUpdatedSince(dataTypeFactory.newXMLGregorianCalendar(since));
        return this;
    }
    
    /**
     * Setter for the token property. The request may have a <em>token</em> 
     * attribute.  That token value must match a value that the provider 
     * returned to the requestor as the value of the token attribute in a previous 
     * updates response for the same target.  Any token value should match the 
     * provider's most recent token for the same target. The idea is the provider
     * can use that <em>token</em> as a mark to not repeat or to start from
     * that point.
     * 
     * @param token The token to put in the updates request
     * @return The same builder
     */
    public UpdatesRequestBuilder token(String token) {
        request.setToken(token);
        return this;
    }
    
    /**
     * Setter for the max select property. The value of the <em>maxSelect</em> 
     * attribute specifies the maximum number of updates the provider should select.
     * 
     * @param size The size in the request
     * @return The same builder
     */
    public UpdatesRequestBuilder maxSelect(int size) {
        request.setMaxSelect(size);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<UpdatesRequestType> build() {
        return getUpdatesObjectFactory().createUpdatesRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asUpdates();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesRequestBuilder fromRequest(UpdatesRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asUpdates();
    }
    
}