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

import es.rickyepoderi.spml4jaxb.accessor.CloseIteratorRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.CloseIteratorResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.ResultsIteratorType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder the SPMLv2 CloseIterator response request. The close iterator
 * operation is defined inside the search capability (capability to perform
 * general searches over the objects contained by the repository). A search
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
 * management. This request just manages the ID of the iterator to close/free.</p>
 * 
 * @author ricky
 */
public class CloseIteratorRequestBuilder extends RequestBuilder<CloseIteratorRequestType, CloseIteratorRequestBuilder, 
        CloseIteratorRequestAccessor, CloseIteratorResponseAccessor> {

    /**
     * Constructor for an empty CloseIterator builder.
     */
    public CloseIteratorRequestBuilder() {
        super(new CloseIteratorRequestType());
    }
    
    /**
     * Setter for the iterator id to close.
     * @param id The iterator id to close
     * @return The same builder
     */
    public CloseIteratorRequestBuilder iteratorId(String id) {
        ResultsIteratorType iter = new ResultsIteratorType();
        iter.setID(id);
        request.setIterator(iter);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<CloseIteratorRequestType> build() {
        return getSearchObjectFactory().createCloseIteratorRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CloseIteratorRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asCloseIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CloseIteratorRequestBuilder fromRequest(CloseIteratorRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CloseIteratorResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asCloseIterator();
    }
    
}
