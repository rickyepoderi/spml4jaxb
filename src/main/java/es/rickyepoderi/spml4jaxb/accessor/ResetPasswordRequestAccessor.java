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
import es.rickyepoderi.spml4jaxb.builder.ResetPasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordRequestType;

/**
 *
 * @author ricky
 */
public class ResetPasswordRequestAccessor extends RequestAccessor<ResetPasswordRequestType, ResetPasswordRequestBuilder> {

    protected ResetPasswordRequestAccessor(ResetPasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForResetPassword();
    }
    
    @Override
    public ResetPasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForResetPassword().fromRequest(this.request);
    }
    
}
