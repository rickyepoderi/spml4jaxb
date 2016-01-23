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

import es.rickyepoderi.spml4jaxb.accessor.CancelRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.CancelResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class CancelRequestBuilder extends RequestBuilder<CancelRequestType, CancelRequestBuilder, CancelRequestAccessor, CancelResponseAccessor> {

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
    public CancelRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asCancel();
    }

    @Override
    public CancelRequestBuilder fromRequest(CancelRequestType request) {
        this.request = request;
        return this;
    }

    @Override
    public CancelResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendInternal(client).asCancel();
    }
    
}
