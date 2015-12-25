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
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkDeleteRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class BulkDeleteRequestBuilder extends RequestBuilder<BulkDeleteRequestType, BulkDeleteRequestBuilder> {

    public BulkDeleteRequestBuilder() {
        super(new BulkDeleteRequestType());
    }
    
    public BulkDeleteRequestBuilder recursive(boolean recursive) {
        request.setRecursive(recursive);
        return this;
    }
    
    public BulkDeleteRequestBuilder recursive() {
        return recursive(true);
    }
    
    public BulkDeleteRequestBuilder nonRecursive() {
        return recursive(false);
    }
    
    public BulkDeleteRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }

    @Override
    public JAXBElement<BulkDeleteRequestType> build() {
        return getBulkObjectFactory().createBulkDeleteRequest(request);
    }
    
    @Override
    public RequestAccessor asAccessor() {
        return super.asAccessor().asBulkDelete();
    }
    
}
