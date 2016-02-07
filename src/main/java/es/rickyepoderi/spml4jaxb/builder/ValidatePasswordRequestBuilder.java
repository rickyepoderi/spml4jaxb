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
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 ValidatePassword operation request. The validate password
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to verify the value of an object
 * that supports the password capability (user). The request just needs the PSO 
 * (object to validate) and the password to be checked.</p>
 * 
 * @author ricky
 */
public class ValidatePasswordRequestBuilder extends RequestBuilder<ValidatePasswordRequestType, ValidatePasswordRequestBuilder, 
        ValidatePasswordRequestAccessor, ValidatePasswordResponseAccessor> {

    /**
     * Constructor for a new validate password request builder.
     */
    protected ValidatePasswordRequestBuilder() {
        super(new ValidatePasswordRequestType());
    }
    
    /**
     * Setter for the password to send for validating.
     * @param password The password to check
     * @return The same builder
     */
    public ValidatePasswordRequestBuilder password(String password) {
        request.setPassword(password);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ValidatePasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createValidatePasswordRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asValidatePassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordRequestBuilder fromRequest(ValidatePasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asValidatePassword();
    }
    
}