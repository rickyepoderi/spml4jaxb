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

import es.rickyepoderi.spml4jaxb.builder.BulkDeleteResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>Accessor for the SPMLv2 Bulk Delete operation response. The bulk delete
 * operation is defined inside the bulk capability (capability to perform
 * massive deletes or operations based on a search condition). The bulk
 * delete performs the delete of a bunch of objects that honors a 
 * specific search criteria. The response is just a base response
 * with no specific data for the operation.</p>
 * 
 * @author ricky
 */
public class BulkDeleteResponseAccessor extends BaseResponseAccessor<ResponseType, BulkDeleteResponseAccessor, BulkDeleteResponseBuilder> {

    /**
     * Constructor for an empty bulk delete response.
     */
    protected BulkDeleteResponseAccessor() {
        this(new ResponseType());
    }
    
    /**
     * Constructor for a bulk delete response given the internal type.
     * @param response The internal response type as obtained by JAXB
     */
    protected BulkDeleteResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteResponseBuilder toBuilder() {
        return ResponseBuilder.builderForBulkDelete().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteResponseAccessor asAccessor(ResponseType response) {
        return new BulkDeleteResponseAccessor(response);
    }
    
}
