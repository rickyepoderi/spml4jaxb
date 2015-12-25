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
import es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.updates.ResultsIteratorType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class UpdatesIterateRequestBuilder extends RequestBuilder<IterateRequestType, UpdatesIterateRequestBuilder> {

    public UpdatesIterateRequestBuilder() {
        super(new IterateRequestType());
    }

    public UpdatesIterateRequestBuilder iteratorId(String id) {
        ResultsIteratorType iter = new ResultsIteratorType();
        iter.setID(id);
        request.setIterator(iter);
        return this;
    }

    @Override
    public JAXBElement<IterateRequestType> build() {
        return getUpdatesObjectFactory().createIterateRequest(request);
    }
    
    @Override
    public RequestAccessor asAccessor() {
        return super.asAccessor().asUpdatesIterate();
    }
    
}
