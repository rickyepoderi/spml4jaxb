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
import es.rickyepoderi.spml4jaxb.accessor.SearchRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Search request. The search
 * operation is defined inside the search capability (capability to perform
 * general searches over the objects contained in the repository). A search
 * (as defined by the standard) can sent the result objects in several 
 * pages (the idea in the standard is limiting the number of objects returned
 * in a single response). For that the search operation can return an 
 * iterator, this iterator is a mark that the client can use to request
 * the following pages of the same search.</p>
 * 
 * <p>The search operation returns the first page and an iterator id. The client 
 * requests the second page using the Iterate request with the previous received 
 * id. This procedure is repeated as many times as pages needed for that search. 
 * Finally the CloseIterator is used to close the current search. This operation 
 * is used by the server to free any resources related to the iterator / search
 * management.</p>
 * 
 * <p>The search request has a special query builder to create the filter
 * in the DSML profile or the XPATH expression in the XSD profile. Besides
 * it has some other properties like the maximum of elements to return.
 * Remember that returnData management is inherited from the base builder.</p>
 * 
 * @author ricky
 */
public class SearchRequestBuilder extends RequestBuilder<SearchRequestType, SearchRequestBuilder, 
        SearchRequestAccessor, SearchResponseAccessor> {

    /**
     * Constructor for a new search request builder.
     */
    public SearchRequestBuilder() {
        super(new SearchRequestType());
    }
    
    /**
     * Setter for the query of the search request using the query buidler. The
     * query builder will be built and assigned as the query of the resquest.
     * @param query The query builder completely filled to set
     * @return The same builder
     */
    public SearchRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }
    
    /**
     * Setter for the capabilities assigned in the request. Each <em>includeDataForCapability</em>
     * element specifies a capability for which the provider should return 
     * capability-specific data (unless the <em>returnData</em> attribute specifies that 
     * the provider should return no capability-specific data at all).
     * @param capability The list of capabilities requested
     * @return The same buidler
     */
    public SearchRequestBuilder includeDataForCapability(String... capability) {
        request.getIncludeDataForCapability().addAll(Arrays.asList(capability));
        return this;
    }
    
    /**
     * Setter for the max select property. The value of the <em>maxSelect</em> 
     * attribute specifies the maximum number of objects the provider should select.
     * @param size The maximum number of objects to return
     * @return The same buidler
     */
    public SearchRequestBuilder maxSelect(int size) {
        request.setMaxSelect(size);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<SearchRequestType> build() {
        request.setReturnData(returnData);
        return getSearchObjectFactory().createSearchRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRequestAccessor asAccessor() {
        request.setReturnData(returnData);
        return BaseRequestAccessor.accessorForRequest(request).asSearch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchRequestBuilder fromRequest(SearchRequestType request) {
        this.request = request;
        this.returnData = request.getReturnData();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asSearch();
    }
    
}
