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
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ValidatePasswordRequestBuilder extends RequestBuilder<ValidatePasswordRequestType, ValidatePasswordRequestBuilder> {

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
    public RequestAccessor asAccessor() {
        request.setPsoID(pso);
        return super.asAccessor().asValidatePassword();
    }

    @Override
    public ValidatePasswordRequestBuilder fromRequest(ValidatePasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }
    
}
