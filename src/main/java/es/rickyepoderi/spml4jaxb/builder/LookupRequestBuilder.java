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
import es.rickyepoderi.spml4jaxb.msg.core.LookupRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class LookupRequestBuilder extends RequestBuilder<LookupRequestType, LookupRequestBuilder, LookupRequestAccessor>{

    protected LookupRequestBuilder() {
        super(new LookupRequestType());
    }
    
    @Override
    public JAXBElement<LookupRequestType> build() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return getCoreObjectFactory().createLookupRequest(request);
    }
    
    @Override
    public LookupRequestAccessor asAccessor() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return BaseRequestAccessor.accessorForRequest(request).asLookup();
    }

    @Override
    public LookupRequestBuilder fromRequest(LookupRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        this.returnData = request.getReturnData();
        return this;
    }
    
}
