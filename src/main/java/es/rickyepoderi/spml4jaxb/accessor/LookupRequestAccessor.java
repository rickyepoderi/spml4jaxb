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
 *
 * @author ricky
 */
public class LookupRequestAccessor extends RequestAccessor<LookupRequestType, LookupRequestAccessor, LookupRequestBuilder> {

    protected LookupRequestAccessor() {
        this(new LookupRequestType());
    }
    
    protected LookupRequestAccessor(LookupRequestType request) {
        super(request, request.getPsoID(), request.getReturnData());
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForLookup();
    }
    
    @Override
    public LookupRequestBuilder toBuilder() {
        return RequestBuilder.builderForLookup().fromRequest(this.request);
    }

    @Override
    public LookupRequestAccessor asAccessor(LookupRequestType request) {
        return new LookupRequestAccessor(request);
    }
}
