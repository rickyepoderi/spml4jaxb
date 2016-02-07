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

import es.rickyepoderi.spml4jaxb.accessor.IterateRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.ResultsIteratorType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 IterateRequest response request. The iterate
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
 * management. This request just manages the ID of the iterator the client want
 * to continue (retrieve the next page of results). The iterate request
 * has no iterate response, the response is also a Search response.</p>
 * 
 * @author ricky
 */
public class IterateRequestBuilder extends RequestBuilder<IterateRequestType, IterateRequestBuilder, IterateRequestAccessor, SearchResponseAccessor> {

    /**
     * Constructor for an empty iterate request builder.
     */
    public IterateRequestBuilder() {
        super(new IterateRequestType());
    }
    
    /**
     * Setter for the iterate id to continue, get the next page.
     * @param id The new iterator id to continue
     * @return The same builder
     */
    public IterateRequestBuilder iteratorId(String id) {
        ResultsIteratorType iter = new ResultsIteratorType();
        iter.setID(id);
        request.setIterator(iter);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<IterateRequestType> build() {
        return getSearchObjectFactory().createIterateRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IterateRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asIterate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IterateRequestBuilder fromRequest(IterateRequestType request) {
        this.request = request;
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
