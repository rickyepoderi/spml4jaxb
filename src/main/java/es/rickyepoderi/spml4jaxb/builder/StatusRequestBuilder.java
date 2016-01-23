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
 *
 * @author ricky
 */
public class StatusRequestBuilder extends RequestBuilder<StatusRequestType, StatusRequestBuilder, 
        StatusRequestAccessor, StatusResponseAccessor> {

    protected StatusRequestBuilder() {
        super(new StatusRequestType());
    }
    
    public StatusRequestBuilder asyncRequestId(String asyncRequestId) {
        request.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    public StatusRequestBuilder returnResults(boolean returnResults) {
        request.setReturnResults(returnResults);
        return this;
    }
    
    public StatusRequestBuilder returnResults() {
        request.setReturnResults(true);
        return this;
    }
    
    public StatusRequestBuilder noReturnResults() {
        request.setReturnResults(false);
        return this;
    }
    
    @Override
    public JAXBElement<StatusRequestType> build() {
        return getAsyncObjectFactory().createStatusRequest(request);
    }
    
    @Override
    public StatusRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asStatus();
    }

    @Override
    public StatusRequestBuilder fromRequest(StatusRequestType request) {
        this.request = request;
        return this;
    }

    @Override
    public StatusResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendInternal(client).asStatus();
    }
    
}
