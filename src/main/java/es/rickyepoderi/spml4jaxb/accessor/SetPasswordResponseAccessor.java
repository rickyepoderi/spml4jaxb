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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SetPasswordResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>Accessor for the SPMLv2 SetPassword operation response. The SetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * of an object (user) passing the password. The response has no special
 * properties.</p>
 * 
 * @author ricky
 */
public class SetPasswordResponseAccessor extends BaseResponseAccessor<ResponseType, SetPasswordResponseAccessor, SetPasswordResponseBuilder> {

    /**
     * Constructor for an empty set password response accessor.
     */
    protected SetPasswordResponseAccessor() {
        this(new ResponseType());
    }
    
    /**
     * Constructor for a set password response accessor giving the internal type.
     * @param response The internal set password response type as defined in the standard
     */
    protected SetPasswordResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordResponseBuilder toBuilder() {
        return ResponseBuilder.builderForSetPassword().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordResponseAccessor asAccessor(ResponseType response) {
        return new SetPasswordResponseAccessor(response);
    }
    
}
