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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.UpdatesRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType;
import java.util.Date;

/**
 * <p>Accessor for the SPMLv2 Updates request. The updates
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
public class UpdatesRequestAccessor extends BaseRequestAccessor<UpdatesRequestType, 
        UpdatesRequestAccessor, UpdatesRequestBuilder> {

    /**
     * Constructor for a new updates request accessor.
     */
    protected UpdatesRequestAccessor() {
        this(new UpdatesRequestType());
    }
    
    /**
     * Constructor for a updates operation using the internal type.
     * @param request The updates type as defined in the standard and parsed by JAXB
     */
    protected UpdatesRequestAccessor(UpdatesRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Setter for the query criteria for filter updates over objects that
     * matches the critera. A <em>query</em> describes criteria that (the 
     * provider must use to) select objects on a target. The provider will 
     * return only updates that affect objects that match these criteria. 
     * 
     * @return The query accessor to parse the search criteria
     */
    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (query != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
    /**
     * Getter of the capabilities that the updates operation is requested.
     * The updates operation can contain a list of URNs of an XML namespace that 
     * uniquely identifies a capability. The provider should return updates that 
     * reflect changes to capability-specific data, updates produced by those 
     * capabilities.
     * 
     * @return The array of capabilities in the request or empty array
     */
    public String[] getUpdatedByCapability() {
        return request.getUpdatedByCapability().toArray(new String[0]);
    }
    
    /**
     * Getter for the updated since property. The provider will return only 
     * updates with a timestamp greater than this value.
     * 
     * @return The date for the updated since or null
     */
    public Date getUpdatedSince() {
        if (request.getUpdatedSince() != null) {
            return request.getUpdatedSince().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }
    
    /**
     * Getter for the token in the request. The request may have a <em>token</em> 
     * attribute.  That token value must match a value that the provider 
     * returned to the requestor as the value of the token attribute in a previous 
     * updates response for the same target.  Any token value should match the 
     * provider's most recent token for the same target. The idea is the provider
     * can use that <em>token</em> as a mark to not repeat or to start from
     * that point.
     * 
     * @return The token in the request
     */
    public String getToken() {
        return request.getToken();
    }
    
    /**
     * Getter for the max select property. The value of the <em>maxSelect</em> 
     * attribute specifies the maximum number of updates the provider should select.
     * 
     * @return The max select number in the request or 0 (means no maximum)
     */
    public int getMaxSelect() {
        return (request.getMaxSelect() == null)? 0 : request.getMaxSelect();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForUpdates();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesRequestBuilder toBuilder() {
        return RequestBuilder.builderForUpdates().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesRequestAccessor asAccessor(UpdatesRequestType request) {
        return new UpdatesRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  updatedByCapability: ").append(getUpdatedByCapability()).append(nl);
        sb.append("  token: ").append(getToken()).append(nl);
        sb.append("  updatedSince: ").append(getUpdatedSince()).append(nl);
        sb.append("  maxSelect: ").append(getMaxSelect()).append(nl);
        sb.append("  query: ").append(getQuery());
        return sb.toString();
    }
    
}