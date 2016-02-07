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

import es.rickyepoderi.spml4jaxb.builder.CancelResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.async.CancelResponseType;

/**
 * <p>Accessor for the SPMLv2 Cancel operation response. The cancel modify
 * operation is defined inside the asynchronous capability (capability to
 * manage other previous methods sent asynchronously). The cancel operation
 * is used to stop/cancel a previous asynch request. The cancel response 
 * also manages an extra request id that corresponds to the previous
 * asynch operation to cancel. The status of the operation marks if
 * the previous operation could be canceled or not.</p>
 * 
 * @author ricky
 */
public class CancelResponseAccessor extends BaseResponseAccessor<CancelResponseType, CancelResponseAccessor, CancelResponseBuilder> {

    /**
     * Constructor for an empty cancel response accessor.
     */
    protected CancelResponseAccessor() {
        this(new CancelResponseType());
    }
    
    /**
     * Constructor for a cancel resposne accessor given the internal type.
     * @param response The internal cancel response type obtained by JAXB
     */
    protected CancelResponseAccessor(CancelResponseType response) {
        super(response, null);
    }
    
    /**
     * Getter for the request id of the previous operation that wanted to be
     * canceled.
     * @return The request id of the previous asynch operation to cancel
     */
    public String getAsyncRequestId() {
        return response.getAsyncRequestID();
    }
    
    /**
     * Checker for the request id of the asynch operation to cancel.
     * 
     * @param asyncRequestId The request id to compare with the request one
     * @return true if they are the same, false otherwise
     */
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(response.getAsyncRequestID());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  asyncRequestId: ").append(getAsyncRequestId()).append(nl);
        return sb.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CancelResponseBuilder toBuilder() {
        return ResponseBuilder.builderForCancel().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CancelResponseAccessor asAccessor(CancelResponseType response) {
        return new CancelResponseAccessor(response);
    }
}
