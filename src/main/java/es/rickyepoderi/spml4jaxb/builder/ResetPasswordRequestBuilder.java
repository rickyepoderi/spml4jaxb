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
import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 ResetPassword operation request. The ResetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * without sending a new password, the system chooses one password and that
 * is returned in the response. The request just needs the PSO (object to reset),
 * so no specific properties are needed.</p>
 * 
 * @author ricky
 */
public class ResetPasswordRequestBuilder extends RequestBuilder<ResetPasswordRequestType, ResetPasswordRequestBuilder, 
        ResetPasswordRequestAccessor, ResetPasswordResponseAccessor> {

    /**
     * Constructor for an empty reset password request builder.
     */
    protected ResetPasswordRequestBuilder() {
        super(new ResetPasswordRequestType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResetPasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createResetPasswordRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResetPasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asResetPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResetPasswordRequestBuilder fromRequest(ResetPasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResetPasswordResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asResetPassword();
    }
    
}