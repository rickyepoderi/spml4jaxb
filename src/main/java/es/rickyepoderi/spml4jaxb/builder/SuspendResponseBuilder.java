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
import es.rickyepoderi.spml4jaxb.accessor.SuspendResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Suspend operation response. The suspend
 * operation is defined inside the suspend capability (capability to
 * enable and disable objects in the target). The suspend operation
 * is used to disable an object. The suspend response has no special 
 * properties.<p>
 * 
 * @author ricky
 */
public class SuspendResponseBuilder extends ResponseBuilder<ResponseType, SuspendResponseBuilder, SuspendResponseAccessor> {

    /**
     * Constructor for a new suspend response builder.
     */
    protected SuspendResponseBuilder() {
        super(new ResponseType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
        return getSuspendObjectFactory().createSuspendResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asSuspend();
    }
    
}
