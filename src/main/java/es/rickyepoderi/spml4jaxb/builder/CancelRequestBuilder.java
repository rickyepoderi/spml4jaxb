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
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class CancelRequestBuilder extends RequestBuilder<CancelRequestType, CancelRequestBuilder> {

    protected CancelRequestBuilder() {
        super(new CancelRequestType());
    }
    
    public CancelRequestBuilder asyncRequestId(String asyncRequestId) {
        request.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    @Override
    public JAXBElement<CancelRequestType> build() {
        return getAsyncObjectFactory().createCancelRequest(request);
    }
    
    @Override
    public RequestAccessor asAccessor() {
        return super.asAccessor().asCancel();
    }
    
}
