/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;

/**
 *
 * @author ricky
 */
public class CancelRequestAccessor extends RequestAccessor<CancelRequestType> {

    protected CancelRequestAccessor(CancelRequestType request) {
        super(request, null, null);
    }
    
    public String getAsyncRequestId() {
        return request.getAsyncRequestID();
    }
    
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(request.getAsyncRequestID());
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForCancel();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  asyncRequestId: ").append(getAsyncRequestId()).append(nl);
        return sb.toString();
    }
    
}
