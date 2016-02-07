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

import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Bulk Delete operation response. The bulk delete
 * operation is defined inside the bulk capability (capability to perform
 * massive deletes or operations based on a search condition). The bulk
 * delete performs the delete of a bunch of objects that honors a 
 * specific search criteria. The response is just a base response
 * with no specific data for the operation.</p>
 * 
 * @author ricky
 */
public class BulkDeleteResponseBuilder extends ResponseBuilder<ResponseType, BulkDeleteResponseBuilder, BulkDeleteResponseAccessor> {

    /**
     * Contrustor for an empty delete response builder.
     */
    protected BulkDeleteResponseBuilder() {
        super(new ResponseType());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
        return getBulkObjectFactory().createBulkDeleteResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkDeleteResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asBulkDelete();
    }
}
