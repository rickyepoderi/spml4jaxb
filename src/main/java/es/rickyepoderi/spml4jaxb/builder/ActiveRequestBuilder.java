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

import es.rickyepoderi.spml4jaxb.accessor.ActiveRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ActiveResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder fo the SPMLv2 Active operation request. The active operation
 * is defined inside the suspend capability (capability to enable, disable
 * and know the status of an object in the repository). The Active operation
 * id the one to know the status (enabled/active or disabled/disactived). The
 * request just uses the PSO ID to identify over what object the request
 * is sent.</p>
 * 
 * @author ricky
 */
public class ActiveRequestBuilder extends RequestBuilder<ActiveRequestType, ActiveRequestBuilder, ActiveRequestAccessor, ActiveResponseAccessor> {

    /**
     * Protected method to construct an empty active request buidler.
     */
    protected ActiveRequestBuilder() {
        super(new ActiveRequestType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ActiveRequestType> build() {
        request.setPsoID(pso);
        return getSuspendObjectFactory().createActiveRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveRequestBuilder fromRequest(ActiveRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asActive();
    }
}
