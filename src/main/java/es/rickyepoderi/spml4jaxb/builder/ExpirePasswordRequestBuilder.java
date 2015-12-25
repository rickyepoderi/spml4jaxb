/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
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
import es.rickyepoderi.spml4jaxb.msg.password.ExpirePasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ExpirePasswordRequestBuilder extends RequestBuilder<ExpirePasswordRequestType, ExpirePasswordRequestBuilder> {

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
    public RequestAccessor asAccessor() {
        request.setPsoID(pso);
        return super.asAccessor().asExpirePassword();
    }
    
}
