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

import es.rickyepoderi.spml4jaxb.accessor.ExpirePasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ExpirePasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.password.ExpirePasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ExpirePasswordRequestBuilder extends RequestBuilder<ExpirePasswordRequestType, ExpirePasswordRequestBuilder, 
        ExpirePasswordRequestAccessor, ExpirePasswordResponseAccessor> {

    protected ExpirePasswordRequestBuilder() {
        super(new ExpirePasswordRequestType());
    }
    
    public ExpirePasswordRequestBuilder remainingLoggings(int remainingLoggings) {
        request.setRemainingLogins(remainingLoggings);
        return this;
    }

    @Override
    public JAXBElement<ExpirePasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createExpirePasswordRequest(request);
    }
    
    @Override
    public ExpirePasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asExpirePassword();
    }

    @Override
    public ExpirePasswordRequestBuilder fromRequest(ExpirePasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    @Override
    public ExpirePasswordResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendInternal(client).asExpirePassword();
    }
    
}
