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

import es.rickyepoderi.spml4jaxb.builder.ExpirePasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.ExpirePasswordRequestType;

/**
 *
 * @author ricky
 */
public class ExpirePasswordRequestAccessor extends BaseRequestAccessor<ExpirePasswordRequestType, ExpirePasswordRequestAccessor, ExpirePasswordRequestBuilder> {

    protected ExpirePasswordRequestAccessor() {
        this(new ExpirePasswordRequestType());
    }
    
    protected ExpirePasswordRequestAccessor(ExpirePasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    public int getRemainingLoggings() {
        return request.getRemainingLogins();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForExpirePassword();
    }
    
    @Override
    public ExpirePasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForExpirePassword().fromRequest(this.request);
    }

    @Override
    public ExpirePasswordRequestAccessor asAccessor(ExpirePasswordRequestType request) {
        return new ExpirePasswordRequestAccessor(request);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  remainingLogins: ").append(getRemainingLoggings()).append(nl);
        return sb.toString();
    }
    
}
