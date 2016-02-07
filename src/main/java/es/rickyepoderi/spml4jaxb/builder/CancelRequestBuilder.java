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

import es.rickyepoderi.spml4jaxb.accessor.CancelRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.CancelResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Cancel operation request. The cancel modify
 * operation is defined inside the asynchronous capability (capability to
 * manage other previous methods sent asynchronously). The cancel operation
 * is used to stop/cancel a previous asynch request. The cancel request
 * just manages an extra request id that corresponds to the previous
 * asynch operation to cancel.</p>
 * 
 * @author ricky
 */
public class CancelRequestBuilder extends RequestBuilder<CancelRequestType, CancelRequestBuilder, CancelRequestAccessor, CancelResponseAccessor> {

    /**
     * Constructor for an empty cancel requets builder.
     */
    protected CancelRequestBuilder() {
        super(new CancelRequestType());
    }
    
    /**
     * Setter for the previous asynch request id to cancel.
     * @param asyncRequestId The request id of the previous asynch operation to cancel
     * @return The same builder
     */
    public CancelRequestBuilder asyncRequestId(String asyncRequestId) {
        request.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<CancelRequestType> build() {
        return getAsyncObjectFactory().createCancelRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CancelRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asCancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CancelRequestBuilder fromRequest(CancelRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CancelResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asCancel();
    }
    
}
