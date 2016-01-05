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
import es.rickyepoderi.spml4jaxb.builder.ValidatePasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;

/**
 *
 * @author ricky
 */
public class ValidatePasswordRequestAccessor extends RequestAccessor<ValidatePasswordRequestType, ValidatePasswordRequestAccessor, ValidatePasswordRequestBuilder> {

    protected ValidatePasswordRequestAccessor() {
        this(new ValidatePasswordRequestType());
    }
    
    protected ValidatePasswordRequestAccessor(ValidatePasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    public String getPassword() {
        return request.getPassword();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForValidatePassword();
    }
    
    @Override
    public ValidatePasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForValidatePassword().fromRequest(this.request);
    }

    @Override
    public ValidatePasswordRequestAccessor asAccessor(ValidatePasswordRequestType request) {
        return new ValidatePasswordRequestAccessor(request);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  password: ").append(getPassword() == null? "null":"********").append(nl);
        return sb.toString();
    }
}
