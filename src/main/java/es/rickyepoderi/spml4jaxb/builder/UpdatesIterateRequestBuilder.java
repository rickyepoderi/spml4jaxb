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
import es.rickyepoderi.spml4jaxb.accessor.UpdatesIterateRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.updates.ResultsIteratorType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Updates Iterate request. The Iterate
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
public class UpdatesIterateRequestBuilder extends RequestBuilder<IterateRequestType, UpdatesIterateRequestBuilder, 
        UpdatesIterateRequestAccessor, UpdatesResponseAccessor> {

    /**
     * Constructor for a new updates iterate request builder.
     */
    public UpdatesIterateRequestBuilder() {
        super(new IterateRequestType());
    }

    /**
     * Setter of the iterator identifier to continue (to get the next page).
     * @param id The iterator identifier
     * @return The same builder
     */
    public UpdatesIterateRequestBuilder iteratorId(String id) {
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
        return getUpdatesObjectFactory().createIterateRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesIterateRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asUpdatesIterate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesIterateRequestBuilder fromRequest(IterateRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asUpdates();
    }
    
}
