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

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchRequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class SearchRequestBuilder extends RequestBuilder<SearchRequestType, SearchRequestBuilder, SearchRequestAccessor> {

    public SearchRequestBuilder() {
        super(new SearchRequestType());
    }
    
    public SearchRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }
    
    public SearchRequestBuilder includeDataForCapability(String... capability) {
        request.getIncludeDataForCapability().addAll(Arrays.asList(capability));
        return this;
    }
    
    public SearchRequestBuilder maxSelect(int size) {
        request.setMaxSelect(size);
        return this;
    }

    @Override
    public JAXBElement<SearchRequestType> build() {
        request.setReturnData(returnData);
        return getSearchObjectFactory().createSearchRequest(request);
    }
    
    @Override
    public SearchRequestAccessor asAccessor() {
        request.setReturnData(returnData);
        return RequestAccessor.accessorForRequest(request).asSearch();
    }

    @Override
    public SearchRequestBuilder fromRequest(SearchRequestType request) {
        this.request = request;
        this.returnData = request.getReturnData();
        return this;
    }
    
}
