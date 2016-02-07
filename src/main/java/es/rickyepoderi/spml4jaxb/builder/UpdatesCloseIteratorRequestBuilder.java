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
import es.rickyepoderi.spml4jaxb.accessor.UpdatesCloseIteratorRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesCloseIteratorResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.updates.ResultsIteratorType;
import es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Updates CloseIterator request. The CloseIterator
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
public class UpdatesCloseIteratorRequestBuilder extends RequestBuilder<CloseIteratorRequestType, UpdatesCloseIteratorRequestBuilder, 
        UpdatesCloseIteratorRequestAccessor, UpdatesCloseIteratorResponseAccessor> {

    /**
     * Constructor for a new close iterator request builder.
     */
    public UpdatesCloseIteratorRequestBuilder() {
        super(new CloseIteratorRequestType());
    }

    /**
     * Setter for the iterator identifier to close in the request.
     * 
     * @param id The iterator id to close
     * @return The same builder
     */
    public UpdatesCloseIteratorRequestBuilder iteratorId(String id) {
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
        return getUpdatesObjectFactory().createCloseIteratorRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asUpdatesCloseIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorRequestBuilder fromRequest(CloseIteratorRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asUpdatesCloseIterator();
    }
    
}
