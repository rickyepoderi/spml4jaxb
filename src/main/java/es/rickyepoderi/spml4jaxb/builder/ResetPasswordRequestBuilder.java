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
import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ResetPasswordRequestBuilder extends RequestBuilder<ResetPasswordRequestType, ResetPasswordRequestBuilder, ResetPasswordRequestAccessor> {

    protected ResetPasswordRequestBuilder() {
        super(new ResetPasswordRequestType());
    }

    @Override
    public JAXBElement<ResetPasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createResetPasswordRequest(request);
    }
    
    @Override
    public ResetPasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asResetPassword();
    }

    @Override
    public ResetPasswordRequestBuilder fromRequest(ResetPasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }
    
}
