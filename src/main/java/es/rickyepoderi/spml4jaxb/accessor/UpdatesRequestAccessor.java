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
 *
 * @author ricky
 */
public class UpdatesRequestAccessor extends BaseRequestAccessor<UpdatesRequestType, UpdatesRequestAccessor, UpdatesRequestBuilder> {

    protected UpdatesRequestAccessor() {
        this(new UpdatesRequestType());
    }
    
    protected UpdatesRequestAccessor(UpdatesRequestType request) {
        super(request, null, null);
    }
    
    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (query != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
    public String[] getUpdatedByCapability() {
        return request.getUpdatedByCapability().toArray(new String[0]);
    }
    
    public Date getUpdatedSince() {
        if (request.getUpdatedSince() != null) {
            return request.getUpdatedSince().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }
    
    public String getToken() {
        return request.getToken();
    }
    
    public int getMaxSelect() {
        return (request.getMaxSelect() == null)? 0 : request.getMaxSelect();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForUpdates();
    }
    
    @Override
    public UpdatesRequestBuilder toBuilder() {
        return RequestBuilder.builderForUpdates().fromRequest(this.request);
    }

    @Override
    public UpdatesRequestAccessor asAccessor(UpdatesRequestType request) {
        return new UpdatesRequestAccessor(request);
    }
    
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
