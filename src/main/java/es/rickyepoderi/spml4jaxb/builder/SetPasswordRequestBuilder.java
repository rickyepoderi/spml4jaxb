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
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.password.SetPasswordRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 SetPassword operation request. The SetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * of an object (user) passing the password. The request just needs the PSO 
 * (object to set) and the password (current password can also be sent).</p>
 * 
 * @author ricky
 */
public class SetPasswordRequestBuilder extends RequestBuilder<SetPasswordRequestType, SetPasswordRequestBuilder, 
        SetPasswordRequestAccessor, SetPasswordResponseAccessor> {

    /**
     * Constructor for a new set password request builder.
     */
    protected SetPasswordRequestBuilder() {
        super(new SetPasswordRequestType());
    }
    
    /**
     * Setter for the password field.
     * 
     * @param password The new password to send
     * @return The same builder
     */
    public SetPasswordRequestBuilder password(String password) {
        request.setPassword(password);
        return this;
    }
    
    /**
     * Setter for the current password field.
     * 
     * @param password The current password to send
     * @return The same builder
     */
    public SetPasswordRequestBuilder currentPassword(String password) {
        request.setCurrentPassword(password);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<SetPasswordRequestType> build() {
        request.setPsoID(pso);
        return getPasswordObjectFactory().createSetPasswordRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asSetPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordRequestBuilder fromRequest(SetPasswordRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asSetPassword();
    }
}