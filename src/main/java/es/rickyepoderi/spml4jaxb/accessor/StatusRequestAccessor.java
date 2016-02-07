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
import es.rickyepoderi.spml4jaxb.builder.StatusRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType;

/**
 * <p>Accessor for the SPMLv2 Status operation request. The status
 * operation is defined inside the asynchronous capability (capability to
 * manage other previous methods sent asynchronously). The status operation
 * is used to retrieve the status of a previous asynchronous operation. 
 * The status response gives the current status (pending, success or failure)
 * and, in case the operation is finished, the response of that request. 
 * So this operation is used to retrieve the response of another operation
 * that was started before asynchronously. In the request some specific
 * properties are managed.</p>
 * 
 * @author ricky
 */
public class StatusRequestAccessor extends BaseRequestAccessor<StatusRequestType, StatusRequestAccessor, StatusRequestBuilder> {
    
    /**
     * Constructor for a new empty status request accessor.
     */
    protected StatusRequestAccessor() {
        this(new StatusRequestType());
    }
    
    /**
     * Constructor for an accessor passing the internal type.
     * @param request The internal status request type as defined in the standard (class generated via JAXB)
     */
    protected StatusRequestAccessor(StatusRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Getter for the async request identifier. This identifier is the
     * identifier of the request we are asking for.
     * @return The previously asynch id
     */
    public String getAsyncRequestId() {
        return request.getAsyncRequestID();
    }
    
    /**
     * Checker for the asynch id. It checks if the internal asynch request id
     * is equals to the one passed as an argument.
     * 
     * @param asyncRequestId The asynch request to compare
     * @return true if the passed if is equals to the internal one, false otherwise
     */
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(request.getAsyncRequestID());
    }
    
    /**
     * Setter for the return results property. The status operation can be sent
     * to just return the status of the asynch operation (pending, failure or
     * success) or to return full results.
     * 
     * @return the value of the internal return results property
     */
    public boolean isReturnResults() {
        return request.isReturnResults();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForStatus();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public StatusRequestBuilder toBuilder() {
        return RequestBuilder.builderForStatus().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusRequestAccessor asAccessor(StatusRequestType request) {
        return new StatusRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  asyncRequestId: ").append(getAsyncRequestId()).append(nl);
        sb.append("  returnResults: ").append(isReturnResults()).append(nl);
        return sb.toString();
    }
}
