/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.LookupRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class LookupRequestBuilder extends RequestBuilder<LookupRequestType, LookupRequestBuilder>{

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
    public RequestAccessor asAccessor() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return super.asAccessor().asLookup();
    }
    
}
