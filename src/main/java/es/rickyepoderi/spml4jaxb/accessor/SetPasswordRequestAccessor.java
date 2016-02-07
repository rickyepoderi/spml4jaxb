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
import es.rickyepoderi.spml4jaxb.builder.SetPasswordRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.SetPasswordRequestType;

/**
 * <p>Accessor for the SPMLv2 SetPassword operation request. The SetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * of an object (user) passing the password. The request just needs the PSO 
 * (object to set) and the password (current password can also be sent).</p>
 * 
 * @author ricky
 */
public class SetPasswordRequestAccessor extends BaseRequestAccessor<SetPasswordRequestType, SetPasswordRequestAccessor, SetPasswordRequestBuilder> {

    /**
     * Constructor for a new empty set password request accessor.
     */
    protected SetPasswordRequestAccessor() {
        this(new SetPasswordRequestType());
    }
    
    /**
     * Constructor for a set password passing the internal type.
     * @param request The internal set password request type as specified in the 
     *                standard and generated bu JAXB
     */
    protected SetPasswordRequestAccessor(SetPasswordRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    /**
     * Getter for the password.
     * @return The password in the request
     */
    public String getPassword() {
        return request.getPassword();
    }
    
    /**
     * Getter for the current password field.
     * @return The current password set in the request
     */
    public String getCurrentPassword() {
        return request.getCurrentPassword();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForSetPassword();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordRequestBuilder toBuilder() {
        return RequestBuilder.builderForSetPassword().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordRequestAccessor asAccessor(SetPasswordRequestType request) {
        return new SetPasswordRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  password: ").append(getPassword() == null? "null":"********").append(nl);
        sb.append("  currentPassword: ").append(getCurrentPassword() == null? "null":"********").append(nl);
        return sb.toString();
    }
}