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
import es.rickyepoderi.spml4jaxb.builder.UpdatesCloseIteratorRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType;

/**
 * <p>Accessor for the SPMLv2 Updates CloseIterator request. The CloseIterator
 * operation is defined inside the updates capability. The updates capability 
 * allow a requestor to obtain in a scalable manner every recorded update (i.e., 
 * modification to an object) that matches specified selection criteria. The
 * updates operates in the same manner than search, it return the update 
 * records in pages. So again the same technique is used, the updates return
 * the first page of records and, if there are more to come, it appends a
 * iterator identifier in the response. With that id the requestor can
 * request the next page (Iterate operation). If it is needed a third page
 * another iterator id is used in the second page. Finally this CloseIterator operation
 * in this capability frees any resources in the server related to the updates
 * operation.</p>
 * 
 * <p>The request just need the iterator identifier to free.</p>
 * 
 * @author ricky
 */
public class UpdatesCloseIteratorRequestAccessor extends BaseRequestAccessor<CloseIteratorRequestType, 
        UpdatesCloseIteratorRequestAccessor, UpdatesCloseIteratorRequestBuilder> {

    /**
     * Constructor for a new updates close iterator request accessor.
     */
    protected UpdatesCloseIteratorRequestAccessor() {
        this(new CloseIteratorRequestType());
    }
    
    /**
     * Constructor for a updates close iterator request accessor passing the internal type.
     * @param request The close iterator type as defined in the standard
     */
    protected UpdatesCloseIteratorRequestAccessor(CloseIteratorRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Getter for the iterator to close / free in the request.
     * 
     * @return The iterator identifier or null
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
        return ResponseBuilder.builderForUpdatesCloseIterator();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorRequestBuilder toBuilder() {
        return RequestBuilder.builderForUpdatesCloseIterator().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorRequestAccessor asAccessor(CloseIteratorRequestType request) {
        return new UpdatesCloseIteratorRequestAccessor(request);
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