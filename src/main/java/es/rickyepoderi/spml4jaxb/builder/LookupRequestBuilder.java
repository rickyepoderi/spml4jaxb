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

import es.rickyepoderi.spml4jaxb.accessor.LookupRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.core.LookupRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the Lookup request. The Lookup operation is a function in the core
 * capability as defined in SPMLv2. This method retrieves a new object from
 * the target. The request builder just needs the PSO identifier to retrieve
 * and some other data like how to return the data. Those methods are derived
 * from the Base parent class.</p>
 *
 * @author ricky
 */
public class LookupRequestBuilder extends RequestBuilder<LookupRequestType, LookupRequestBuilder, LookupRequestAccessor, LookupResponseAccessor>{

    /**
     * Constructor for an empty lookup request builder.
     */
    protected LookupRequestBuilder() {
        super(new LookupRequestType());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<LookupRequestType> build() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return getCoreObjectFactory().createLookupRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LookupRequestAccessor asAccessor() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return BaseRequestAccessor.accessorForRequest(request).asLookup();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LookupRequestBuilder fromRequest(LookupRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        this.returnData = request.getReturnData();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LookupResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asLookup();
    }
    
}
