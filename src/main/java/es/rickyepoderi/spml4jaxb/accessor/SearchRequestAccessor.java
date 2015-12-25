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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;

/**
 *
 * @author ricky
 */
public class SearchRequestAccessor extends RequestAccessor<SearchRequestType> {

    public SearchRequestAccessor(SearchRequestType request) {
        super(request, null, request.getReturnData());
    }
    
    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (query != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
    public String[] getIncludeDataForCapability() {
        return request.getIncludeDataForCapability().toArray(new String[0]);
    }
    
    public int getMaxSelect() {
        return (request.getMaxSelect() == null)? 0 : request.getMaxSelect();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForSearch();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  includeData: ").append(getIncludeDataForCapability()).append(nl);
        sb.append("  maxSelect: ").append(getMaxSelect()).append(nl);
        sb.append("  query: ").append(getQuery());
        return sb.toString();
    }
    
}
