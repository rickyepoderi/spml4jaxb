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

import es.rickyepoderi.spml4jaxb.accessor.IterateRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.ResultsIteratorType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class IterateRequestBuilder extends RequestBuilder<IterateRequestType, IterateRequestBuilder, IterateRequestAccessor> {

    public IterateRequestBuilder() {
        super(new IterateRequestType());
    }
    
    public IterateRequestBuilder iteratorId(String id) {
        ResultsIteratorType iter = new ResultsIteratorType();
        iter.setID(id);
        request.setIterator(iter);
        return this;
    }

    @Override
    public JAXBElement<IterateRequestType> build() {
        return getSearchObjectFactory().createIterateRequest(request);
    }
    
    @Override
    public IterateRequestAccessor asAccessor() {
        return RequestAccessor.accessorForRequest(request).asIterate();
    }

    @Override
    public IterateRequestBuilder fromRequest(IterateRequestType request) {
        this.request = request;
        return this;
    }
    
}
