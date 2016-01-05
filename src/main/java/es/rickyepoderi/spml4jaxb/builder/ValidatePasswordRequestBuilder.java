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

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ValidatePasswordRequestBuilder extends RequestBuilder<ValidatePasswordRequestType, ValidatePasswordRequestBuilder, ValidatePasswordRequestAccessor> {

    protected ValidatePasswordRequestBuilder() {
        super(new ValidatePasswordRequestType());
    }
    
    public ValidatePasswordRequestBuilder password(String password) {
        request.setPassword(password);
        return this;
    }

    @Override
    public JAXBElement<ValidatePasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createValidatePasswordRequest(request);
    }
    
    @Override
    public ValidatePasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return RequestAccessor.accessorForRequest(request).asValidatePassword();
    }

    @Override
    public ValidatePasswordRequestBuilder fromRequest(ValidatePasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }
    
}
