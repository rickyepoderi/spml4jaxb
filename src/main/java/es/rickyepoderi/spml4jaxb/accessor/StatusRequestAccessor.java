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
 *
 * @author ricky
 */
public class StatusRequestAccessor extends BaseRequestAccessor<StatusRequestType, StatusRequestAccessor, StatusRequestBuilder> {
    
    protected StatusRequestAccessor() {
        this(new StatusRequestType());
    }
    
    protected StatusRequestAccessor(StatusRequestType request) {
        super(request, null, null);
    }
    
    public String getAsyncRequestId() {
        return request.getAsyncRequestID();
    }
    
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(request.getAsyncRequestID());
    }
    
    public boolean isReturnResults() {
        return request.isReturnResults();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForStatus();
    }
    
    @Override
    public StatusRequestBuilder toBuilder() {
        return RequestBuilder.builderForStatus().fromRequest(this.request);
    }

    @Override
    public StatusRequestAccessor asAccessor(StatusRequestType request) {
        return new StatusRequestAccessor(request);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  asyncRequestId: ").append(getAsyncRequestId()).append(nl);
        sb.append("  returnResults: ").append(isReturnResults()).append(nl);
        return sb.toString();
    }
}
