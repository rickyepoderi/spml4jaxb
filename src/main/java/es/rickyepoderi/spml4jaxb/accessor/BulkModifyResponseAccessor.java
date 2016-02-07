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

import es.rickyepoderi.spml4jaxb.builder.BulkModifyResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>Accessor for the SPMLv2 Bulk Modify operation response. The bulk modify
 * operation is defined inside the bulk capability (capability to perform
 * massive deletes or modifies based on a search condition). The bulk
 * modify performs the modify of a bunch of objects that honors a 
 * specific search criteria using always the same modifications. The bulk
 * response is just a base response with no specific data.</p>
 * 
 * @author ricky
 */
public class BulkModifyResponseAccessor extends BaseResponseAccessor<ResponseType, BulkModifyResponseAccessor, BulkModifyResponseBuilder> {

    /**
     * Constructor for a bulk modify response accessor.
     */
    protected BulkModifyResponseAccessor() {
        this(new ResponseType());
    }
    
    /**
     * Constructor for a bulk modify response accessor using the internal type.
     * @param response The internal bulk modify response type as obtained by JAXB
     */
    protected BulkModifyResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyResponseBuilder toBuilder() {
        return ResponseBuilder.builderForBulkModify().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyResponseAccessor asAccessor(ResponseType response) {
        return new BulkModifyResponseAccessor(response);
    }
    
}
