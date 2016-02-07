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

import es.rickyepoderi.spml4jaxb.accessor.CancelResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.async.CancelResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Cancel operation response. The cancel modify
 * operation is defined inside the asynchronous capability (capability to
 * manage other previous methods sent asynchronously). The cancel operation
 * is used to stop/cancel a previous asynch request. The cancel response 
 * also manages an extra request id that corresponds to the previous
 * asynch operation to cancel. The status of the operation marks if
 * the previous operation could be canceled or not.</p>
 * 
 * @author ricky
 */
public class CancelResponseBuilder extends ResponseBuilder<CancelResponseType, CancelResponseBuilder, CancelResponseAccessor> {

    /**
     * Constructor for an empty response builder.
     */
    protected CancelResponseBuilder() {
        super(new CancelResponseType());
    }
    
    /**
     * Setter for the previous asynch request id to cancel.
     * @param asyncRequestId The request id of the previous asynch operation to cancel
     * @return The same builder
     */
    public CancelResponseBuilder asyncRequestId(String asyncRequestId) {
        response.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<CancelResponseType> build() {
        return getAsyncObjectFactory().createCancelResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CancelResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asCancel();
    }
    
}
