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

import es.rickyepoderi.spml4jaxb.accessor.ExpirePasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 ExpirePassword operation response. The ExpirePassword
 * operation is defined inside the password capability (capability to perform
 * password management). The ExpirePassword method is used to expire a user
 * password. The response has no extra properties.</p>
 * 
 * @author ricky
 */
public class ExpirePasswordResponseBuilder extends ResponseBuilder<ResponseType, ExpirePasswordResponseBuilder, ExpirePasswordResponseAccessor> {

    /**
     * Constructor for an empty ExpirePassword response builder.
     */
    protected ExpirePasswordResponseBuilder() {
        super(new ResponseType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
        return getPasswordObjectFactory().createExpirePasswordResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ExpirePasswordResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asExpirePassword();
    }
    
}
