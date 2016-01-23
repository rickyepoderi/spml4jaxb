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

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesCloseIteratorRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesCloseIteratorResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.updates.ResultsIteratorType;
import es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class UpdatesCloseIteratorRequestBuilder extends RequestBuilder<CloseIteratorRequestType, UpdatesCloseIteratorRequestBuilder, 
        UpdatesCloseIteratorRequestAccessor, UpdatesCloseIteratorResponseAccessor> {

    public UpdatesCloseIteratorRequestBuilder() {
        super(new CloseIteratorRequestType());
    }

    public UpdatesCloseIteratorRequestBuilder iteratorId(String id) {
        ResultsIteratorType iter = new ResultsIteratorType();
        iter.setID(id);
        request.setIterator(iter);
        return this;
    }

    @Override
    public JAXBElement<CloseIteratorRequestType> build() {
        return getUpdatesObjectFactory().createCloseIteratorRequest(request);
    }
    
    @Override
    public UpdatesCloseIteratorRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asUpdatesCloseIterator();
    }

    @Override
    public UpdatesCloseIteratorRequestBuilder fromRequest(CloseIteratorRequestType request) {
        this.request = request;
        return this;
    }

    @Override
    public UpdatesCloseIteratorResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendInternal(client).asUpdatesCloseIterator();
    }
    
}
