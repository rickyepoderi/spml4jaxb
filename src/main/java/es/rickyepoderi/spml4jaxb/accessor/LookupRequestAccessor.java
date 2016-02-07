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

import es.rickyepoderi.spml4jaxb.builder.LookupRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.LookupRequestType;

/**
 * <p>Accessor for the Lookup request. The Lookup operation is a function in the core
 * capability as defined in SPMLv2. This method retrieves a new object from
 * the target. The request accessor just needs the PSO identifier to retrieve
 * and some other data like how to return the data. Those methods are derived
 * from the Base parent class.</p>
 * 
 * @author ricky
 */
public class LookupRequestAccessor extends BaseRequestAccessor<LookupRequestType, LookupRequestAccessor, LookupRequestBuilder> {

    /**
     * Constructor for an empty lookup request accessor.
     */
    protected LookupRequestAccessor() {
        this(new LookupRequestType());
    }
    
    /**
     * Constructor for a lookup request accessor giving the internal type.
     * @param request The lookup request type as obtainer from JAXB.
     */
    protected LookupRequestAccessor(LookupRequestType request) {
        super(request, request.getPsoID(), request.getReturnData());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForLookup();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LookupRequestBuilder toBuilder() {
        return RequestBuilder.builderForLookup().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LookupRequestAccessor asAccessor(LookupRequestType request) {
        return new LookupRequestAccessor(request);
    }
}
