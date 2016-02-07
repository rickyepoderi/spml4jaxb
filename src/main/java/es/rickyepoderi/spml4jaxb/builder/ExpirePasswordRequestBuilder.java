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
 * <p>Builder for the SPMLv2 ExpirePassword operation request. The ExpirePassword
 * operation is defined inside the password capability (capability to perform
 * password management). The ExpirePassword method is used to expire a user
 * password (the standard gives the possibility of giving some extra tries).</p>
 * 
 * @author ricky
 */
public class ExpirePasswordRequestBuilder extends RequestBuilder<ExpirePasswordRequestType, ExpirePasswordRequestBuilder, 
        ExpirePasswordRequestAccessor, ExpirePasswordResponseAccessor> {

    /**
     * Constructor for an empty ExpirePassword request builder.
     */
    protected ExpirePasswordRequestBuilder() {
        super(new ExpirePasswordRequestType());
    }
    
    /**
     * Setter for the remaining loggings in the request.
     * @param remainingLoggings The remaining loggings to set in the request
     * @return The same builder
     */
    public ExpirePasswordRequestBuilder remainingLoggings(int remainingLoggings) {
        request.setRemainingLogins(remainingLoggings);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ExpirePasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createExpirePasswordRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ExpirePasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asExpirePassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpirePasswordRequestBuilder fromRequest(ExpirePasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpirePasswordResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asExpirePassword();
    }
    
}
