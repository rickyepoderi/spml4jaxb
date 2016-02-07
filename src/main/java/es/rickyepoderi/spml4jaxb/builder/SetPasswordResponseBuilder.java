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

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 SetPassword operation response. The SetPassword
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to change the password
 * of an object (user) passing the password. The response has no special
 * properties.</p>
 * 
 * @author ricky
 */
public class SetPasswordResponseBuilder extends ResponseBuilder<ResponseType, SetPasswordResponseBuilder, SetPasswordResponseAccessor> {

    /**
     * Constructor for a new set password response builder.
     */
    protected SetPasswordResponseBuilder() {
        super(new ResponseType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
         return getPasswordObjectFactory().createSetPasswordResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SetPasswordResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asSetPassword();
    }
    
}
