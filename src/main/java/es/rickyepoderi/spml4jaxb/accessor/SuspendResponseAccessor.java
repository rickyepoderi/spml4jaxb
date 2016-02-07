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
import es.rickyepoderi.spml4jaxb.builder.SuspendResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>Accessor for the SPMLv2 Suspend operation response. The suspend
 * operation is defined inside the suspend capability (capability to
 * enable and disable objects in the target). The suspend operation
 * is used to disable an object. The suspend response has no special properties.<p>
 * 
 * @author ricky
 */
public class SuspendResponseAccessor extends BaseResponseAccessor<ResponseType, SuspendResponseAccessor, SuspendResponseBuilder> {

    /**
     * Constructor for a new suspend response accessor.
     */
    protected SuspendResponseAccessor() {
        this(new ResponseType());
    }
    
    /**
     * Constructor for a suspend response accessor using the internal type.
     * @param response The internal suspend response type as defined in the standard
     */
    protected SuspendResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendResponseBuilder toBuilder() {
        return ResponseBuilder.builderForSuspend().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendResponseAccessor asAccessor(ResponseType response) {
        return new SuspendResponseAccessor(response);
    }
    
}
