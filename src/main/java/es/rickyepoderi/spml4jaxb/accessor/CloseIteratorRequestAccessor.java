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

import es.rickyepoderi.spml4jaxb.builder.CloseIteratorRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType;

/**
 * <p>Accessor for the SPMLv2 CloseIterator operation request. The close iterator
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
 * management. This request just manages the ID of the iterator to close/free.<p>
 * 
 * @author ricky
 */
public class CloseIteratorRequestAccessor extends BaseRequestAccessor<CloseIteratorRequestType, CloseIteratorRequestAccessor, CloseIteratorRequestBuilder> {

    /**
     * Constructor for an empty CloseIterator request accessor.
     */
    protected CloseIteratorRequestAccessor() {
        this(new CloseIteratorRequestType());
    }
    
    /**
     * Constructor for a CloseIterator request based on the internal type.
     * @param request The internal CloseIterator type generated by JAXB
     */
    protected CloseIteratorRequestAccessor(CloseIteratorRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Getter for the iterator id to close.
     * @return The iterator id to close
     */
    public String getIteratorId() {
        if (request.getIterator() != null) {
            return request.getIterator().getID();
        } else {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForCloseIterator();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CloseIteratorRequestBuilder toBuilder() {
        return RequestBuilder.builderForCloseIterator().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CloseIteratorRequestAccessor asAccessor(CloseIteratorRequestType request) {
        return new CloseIteratorRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  iteratorId: ").append(getIteratorId()).append(nl);
        return sb.toString();
    }
    
}
