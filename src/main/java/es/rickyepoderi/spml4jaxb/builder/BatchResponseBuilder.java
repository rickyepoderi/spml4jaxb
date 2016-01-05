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

import es.rickyepoderi.spml4jaxb.accessor.BatchResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchResponseType;
import javax.xml.bind.JAXBElement;


/**
 *
 * @author ricky
 */
public class BatchResponseBuilder extends ResponseBuilder<BatchResponseType, BatchResponseBuilder, BatchResponseAccessor> {

    public BatchResponseBuilder() {
        super(new BatchResponseType());
    }
    
    public BatchResponseBuilder nestedResponse(ResponseBuilder res) {
        response.getAny().add(res.build());
        return this;
    }

    @Override
    public JAXBElement<BatchResponseType> build() {
        return getBatchObjectFactory().createBatchResponse(response);
    }
    
    @Override
    public BatchResponseAccessor asAccessor() {
        return ResponseAccessor.accessorForResponse(response).asBatch();
    }
    
}
