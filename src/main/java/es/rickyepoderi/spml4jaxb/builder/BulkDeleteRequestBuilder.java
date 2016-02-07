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

import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkDeleteRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Bulk Delete operation request. The bulk delete
 * operation is defined inside the bulk capability (capability to perform
 * massive deletes or operations based on a search condition). The bulk
 * delete performs the delete of a bunch of objects that honors a 
 * specific search criteria. The search criteria used is the same as
 * defined in the search capability.</p>
 * 
 * @author ricky
 */
public class BulkDeleteRequestBuilder extends RequestBuilder<BulkDeleteRequestType, BulkDeleteRequestBuilder, BulkDeleteRequestAccessor, BulkDeleteResponseAccessor> {

    /**
     * empty builder for a bulk delete request.
     */
    public BulkDeleteRequestBuilder() {
        super(new BulkDeleteRequestType());
    }
    
    /**
     * Setter for the recursive property. The SPMLv2 standard defines a 
     * recursive parameter that specifies to delete recursively when a parent
     * honors the criteria.
     * 
     * @param recursive value for the recursive parameter in the request
     * @return The same builder
     */
    public BulkDeleteRequestBuilder recursive(boolean recursive) {
        request.setRecursive(recursive);
        return this;
    }
    
    /**
     * Setter of the recursive property to true.
     * @return The same builder
     */
    public BulkDeleteRequestBuilder recursive() {
        return recursive(true);
    }
    
    /**
     * Setter of the recursive property to false.
     * @return The same builder
     */
    public BulkDeleteRequestBuilder nonRecursive() {
        return recursive(false);
    }
    
    /**
     * Construct a search query using the specific search builder.
     * @param query The query builder to builder the search query
     * @return The same buidler
     */
    public BulkDeleteRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<BulkDeleteRequestType> build() {
        return getBulkObjectFactory().createBulkDeleteRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asBulkDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteRequestBuilder fromRequest(BulkDeleteRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asBulkDelete();
    }
    
}
