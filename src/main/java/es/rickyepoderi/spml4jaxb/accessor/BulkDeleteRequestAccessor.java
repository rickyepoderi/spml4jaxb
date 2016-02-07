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

import es.rickyepoderi.spml4jaxb.builder.BulkDeleteRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkDeleteRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;

/**
 * <p>Accessor for the SPMLv2 Bulk Delete operation request. The bulk delete
 * operation is defined inside the bulk capability (capability to perform
 * massive deletes or operations based on a search condition). The bulk
 * delete performs the delete of a bunch of objects that honors a 
 * specific search criteria. The search criteria used is the same as
 * defined in the search capability.</p>
 * 
 * @author ricky
 */
public class BulkDeleteRequestAccessor extends BaseRequestAccessor<BulkDeleteRequestType, 
        BulkDeleteRequestAccessor, BulkDeleteRequestBuilder> {

    /**
     * Constructor for an empty bulk delete accessor.
     */
    protected BulkDeleteRequestAccessor() {
        this(new BulkDeleteRequestType());
    }
    
    /**
     * Constructor for a bulk delete based on the internal JAXB type.
     * @param request The bulk delete request type as obtained by JAXB from 
     *                the standard definition XSD file
     */
    protected BulkDeleteRequestAccessor(BulkDeleteRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Getter for the recursive property. The bulk delete has a property in the
     * standard that specifies to delete recursively if the object that
     * matches the criteria is parent of more objects.
     * @return true if recursive is defined, false if not, null if not defined
     */
    public Boolean isRecursive() {
        return request.isRecursive();
    }

    /**
     * Getter for the query accessor. The query accessor is the same than the
     * one used in the search capability.
     * @return The query accessor to access the query to perform
     */
    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (request != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForBulkDelete();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteRequestBuilder toBuilder() {
        return RequestBuilder.builderForBulkDelete().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteRequestAccessor asAccessor(BulkDeleteRequestType request) {
        return new BulkDeleteRequestAccessor(request);
    }
    
}
