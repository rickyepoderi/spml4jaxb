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
 *
 * @author ricky
 */
public class BulkDeleteResponseAccessor extends ResponseAccessor<ResponseType, BulkDeleteResponseBuilder> {

    public BulkDeleteResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    @Override
    public BulkDeleteResponseBuilder toBuilder() {
        return ResponseBuilder.builderForBulkDelete().fromResponse(this.response);
    }
    
}
