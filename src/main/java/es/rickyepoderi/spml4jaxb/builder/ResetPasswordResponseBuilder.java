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

import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 ResetPassword operation response. The ResetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * without sending a new password, the system chooses one password and that
 * is returned in the response. The response contains the new password
 * selected and assigned by the system to the user if success.</p>
 * 
 * @author ricky
 */
public class ResetPasswordResponseBuilder extends ResponseBuilder<ResetPasswordResponseType, ResetPasswordResponseBuilder, ResetPasswordResponseAccessor> {

    /**
     * Constructor for an empty reset password response builder.
     */
    protected ResetPasswordResponseBuilder() {
        super(new ResetPasswordResponseType());
    }
    
    /**
     * Setter for the password assigned by the system in the response.
     * @param password The new password of the object / user
     * @return The same builder
     */
    public ResetPasswordResponseBuilder password(String password) {
        response.setPassword(password);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResetPasswordResponseType> build() {
        return getPasswordObjectFactory().createResetPasswordResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResetPasswordResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asResetPassword();
    }
    
}