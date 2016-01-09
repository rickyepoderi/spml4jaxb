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
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.password.SetPasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class SetPasswordRequestBuilder extends RequestBuilder<SetPasswordRequestType, SetPasswordRequestBuilder, SetPasswordRequestAccessor> {

    protected SetPasswordRequestBuilder() {
        super(new SetPasswordRequestType());
    }
    
    public SetPasswordRequestBuilder password(String password) {
        request.setPassword(password);
        return this;
    }
    
    public SetPasswordRequestBuilder currentPassword(String password) {
        request.setCurrentPassword(password);
        return this;
    }
    
    @Override
    public JAXBElement<SetPasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createSetPasswordRequest(request);
    }
    
    @Override
    public SetPasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asSetPassword();
    }

    @Override
    public SetPasswordRequestBuilder fromRequest(SetPasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }
}