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

import es.rickyepoderi.spml4jaxb.builder.ActiveRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveRequestType;

/**
 * <p>Accessor fo the SPMLv2 Active operation request. The active operation
 * is defined inside the suspend capability (capability to enable, disable
 * and know the status of an object in the repository). The Active operation
 * id the one to know the status (enabled/active or disabled/disactived). The
 * request just uses the PSO ID to identify over what object the request
 * is sent.</p>
 * 
 * @author ricky
 */
public class ActiveRequestAccessor extends BaseRequestAccessor<ActiveRequestType, ActiveRequestAccessor, ActiveRequestBuilder> {

    /**
     * Protected constructor for an empty active request accessor.
     */
    protected ActiveRequestAccessor() {
        this(new ActiveRequestType());
    }
    
    /**
     * Protected constructor for a active request object.
     * @param request The active request obtained by JAXB
     */
    protected ActiveRequestAccessor(ActiveRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForActive();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveRequestBuilder toBuilder() {
        return RequestBuilder.builderForActive().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveRequestAccessor asAccessor(ActiveRequestType request) {
        return new ActiveRequestAccessor((ActiveRequestType) request);
    }
}
