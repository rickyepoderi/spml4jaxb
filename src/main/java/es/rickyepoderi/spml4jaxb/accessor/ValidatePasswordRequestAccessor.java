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
import es.rickyepoderi.spml4jaxb.builder.ValidatePasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;

/**
 * <p>Accessor for the SPMLv2 ValidatePassword operation request. The validate password
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to verify the value of an object
 * that supports the password capability (user). The request just needs the PSO 
 * (object to validate) and the password to be checked.</p>
 * 
 * @author ricky
 */
public class ValidatePasswordRequestAccessor extends BaseRequestAccessor<ValidatePasswordRequestType, ValidatePasswordRequestAccessor, ValidatePasswordRequestBuilder> {

    /**
     * Constructor for a new validate password request accessor.
     */
    protected ValidatePasswordRequestAccessor() {
        this(new ValidatePasswordRequestType());
    }
    
    /**
     * Constructor for a validate password request accessor using the internal type.
     * @param request The validate password request type as defined in the standard
     */
    protected ValidatePasswordRequestAccessor(ValidatePasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    /**
     * Getter for the password to check.
     * @return The password set in the request
     */
    public String getPassword() {
        return request.getPassword();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForValidatePassword();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForValidatePassword().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordRequestAccessor asAccessor(ValidatePasswordRequestType request) {
        return new ValidatePasswordRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  password: ").append(getPassword() == null? "null":"********").append(nl);
        return sb.toString();
    }
}
