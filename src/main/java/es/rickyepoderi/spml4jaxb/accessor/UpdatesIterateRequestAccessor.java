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
import es.rickyepoderi.spml4jaxb.builder.UpdatesIterateRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType;

/**
 * <p>Accessor for the SPMLv2 Updates Iterate request. The Iterate
 * operation is defined inside the updates capability. The updates capability 
 * allow a requestor to obtain in a scalable manner every recorded update (i.e., 
 * modification to an object) that matches specified selection criteria. The
 * updates operates in the same manner than search, it return the update 
 * records in pages. So again the same technique is used, the updates return
 * the first page of records and, if there are more to come, it appends a
 * iterator identifier in the response. With that id the requestor can
 * request the next page (this Iterate operation). If it is needed a third page
 * another iterator id is used in the second page. Finally the CloseIterator 
 * frees any resources in the server related to the updates operation.</p>
 * 
 * <p>The request just need the iterator identifier to send to get the next 
 * page of updates.</p>
 * 
 * <p>This operation just has the request part because the response is just
 * the same than an updates response.</p>
 * 
 * @author ricky
 */
public class UpdatesIterateRequestAccessor extends BaseRequestAccessor<IterateRequestType, UpdatesIterateRequestAccessor, UpdatesIterateRequestBuilder> {

    /**
     * Constructor of a new updates iterate request accessor.
     */
    protected UpdatesIterateRequestAccessor() {
        this(new IterateRequestType());
    }
    
    /**
     * Constructor for an updates iterate request passing the internal type.
     * @param request The internal updates iterate request type as defined in the standard
     */
    protected UpdatesIterateRequestAccessor(IterateRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Getter for the iterator in the request.
     * 
     * @return The iterator id 
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
        return ResponseBuilder.builderForUpdates();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesIterateRequestBuilder toBuilder() {
        return RequestBuilder.builderForUpdatesIterate().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesIterateRequestAccessor asAccessor(IterateRequestType request) {
        return new UpdatesIterateRequestAccessor(request);
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