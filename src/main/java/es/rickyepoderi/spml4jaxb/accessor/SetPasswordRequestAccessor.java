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
import es.rickyepoderi.spml4jaxb.builder.SetPasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.SetPasswordRequestType;

/**
 *
 * @author ricky
 */
public class SetPasswordRequestAccessor extends BaseRequestAccessor<SetPasswordRequestType, SetPasswordRequestAccessor, SetPasswordRequestBuilder> {

    protected SetPasswordRequestAccessor() {
        this(new SetPasswordRequestType());
    }
    
    protected SetPasswordRequestAccessor(SetPasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    public String getPassword() {
        return request.getPassword();
    }
    
    public String getCurrentPassword() {
        return request.getCurrentPassword();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForSetPassword();
    }
    
    @Override
    public SetPasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForSetPassword().fromRequest(this.request);
    }

    @Override
    public SetPasswordRequestAccessor asAccessor(SetPasswordRequestType request) {
        return new SetPasswordRequestAccessor(request);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  password: ").append(getPassword() == null? "null":"********").append(nl);
        sb.append("  currentPassword: ").append(getCurrentPassword() == null? "null":"********").append(nl);
        return sb.toString();
    }
}
