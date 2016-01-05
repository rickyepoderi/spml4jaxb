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
 *
 * @author ricky
 */
public class CancelResponseAccessor extends ResponseAccessor<CancelResponseType, CancelResponseBuilder> {

    protected CancelResponseAccessor(CancelResponseType response) {
        super(response, null);
    }
    
    public String getAsyncRequestId() {
        return response.getAsyncRequestID();
    }
    
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(response.getAsyncRequestID());
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  asyncRequestId: ").append(getAsyncRequestId()).append(nl);
        return sb.toString();
    }
    
    @Override
    public CancelResponseBuilder toBuilder() {
        return ResponseBuilder.builderForCancel().fromResponse(this.response);
    }
}
