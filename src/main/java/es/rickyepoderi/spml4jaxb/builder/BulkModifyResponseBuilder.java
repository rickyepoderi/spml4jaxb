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

import es.rickyepoderi.spml4jaxb.accessor.BulkModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class BulkModifyResponseBuilder extends ResponseBuilder<ResponseType, BulkModifyResponseBuilder, BulkModifyResponseAccessor> {

    public BulkModifyResponseBuilder() {
        super(new ResponseType());
    }

    @Override
    public JAXBElement<ResponseType> build() {
        return getBulkObjectFactory().createBulkModifyResponse(response);
    }
    
    @Override
    public BulkModifyResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asBulkModify();
    }
    
}
