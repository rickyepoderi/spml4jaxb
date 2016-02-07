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
import es.rickyepoderi.spml4jaxb.accessor.StatusRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Status operation request. The status
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
public class StatusRequestBuilder extends RequestBuilder<StatusRequestType, StatusRequestBuilder, 
        StatusRequestAccessor, StatusResponseAccessor> {

    /**
     * Constructor for a new status request builder.
     */
    protected StatusRequestBuilder() {
        super(new StatusRequestType());
    }
    
    /**
     * Setter for the asynch request id we are trying to know the status.
     * @param asyncRequestId The previous request id that is consulted
     * @return The same builder
     */
    public StatusRequestBuilder asyncRequestId(String asyncRequestId) {
        request.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    /**
     * Setter for the return result property. If true means that the requestor
     * wants to receive full results of previous operation, if false just the
     * status (still pending, success or failure).
     * 
     * @param returnResults The new value for the internal property
     * @return The same builder
     */
    public StatusRequestBuilder returnResults(boolean returnResults) {
        request.setReturnResults(returnResults);
        return this;
    }
    
    /**
     * Setter for the return results to true.
     * @return The same builder
     */
    public StatusRequestBuilder returnResults() {
        request.setReturnResults(true);
        return this;
    }
    
    /**
     * Setter for the return results to false.
     * @return The same builder
     */
    public StatusRequestBuilder noReturnResults() {
        request.setReturnResults(false);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<StatusRequestType> build() {
        return getAsyncObjectFactory().createStatusRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public StatusRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusRequestBuilder fromRequest(StatusRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asStatus();
    }
    
}