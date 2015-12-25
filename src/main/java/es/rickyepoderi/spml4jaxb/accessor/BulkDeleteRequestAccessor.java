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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.msg.bulk.BulkDeleteRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;

/**
 *
 * @author ricky
 */
public class BulkDeleteRequestAccessor extends RequestAccessor<BulkDeleteRequestType> {

    public BulkDeleteRequestAccessor(BulkDeleteRequestType request) {
        super(request, null, null);
    }
    
    public Boolean isRecursive() {
        return request.isRecursive();
    }

    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (request != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
}
