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
import es.rickyepoderi.spml4jaxb.builder.ResetPasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordRequestType;

/**
 * <p>Accessor for the SPMLv2 ResetPassword operation request. The ResetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * without sending a new password, the system chooses one password and that
 * is returned in the response. The request just needs the PSO (object to reset),
 * so no specific properties are needed.</p>
 * 
 * @author ricky
 */
public class ResetPasswordRequestAccessor extends BaseRequestAccessor<ResetPasswordRequestType, 
        ResetPasswordRequestAccessor, ResetPasswordRequestBuilder> {

    /**
     * Constructor for an empty reset password request accessor.
     */
    protected ResetPasswordRequestAccessor() {
        this(new ResetPasswordRequestType());
    }
    
    /**
     * Constructor for a reset password request accessor given the internal type.
     * @param request The internal ResetPassword type as obtained from JAXB
     */
    protected ResetPasswordRequestAccessor(ResetPasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForResetPassword();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResetPasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForResetPassword().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResetPasswordRequestAccessor asAccessor(ResetPasswordRequestType request) {
        return new ResetPasswordRequestAccessor(request);
    }
    
}