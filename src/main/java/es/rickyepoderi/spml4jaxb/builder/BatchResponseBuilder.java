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
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchResponseType;
import javax.xml.bind.JAXBElement;


/**
 * <p>Builder for the SPMLv2 Batch operation response. The batch operation
 * is defined inside the batch capability (capability to send several operations
 * in batch mode in a single request, it is a bunch of requests in one). The
 * response packs some other responses in a single batch operation. Only
 * some operations are allowed to be batched (see OASIS specification for
 * more details).</p>
 * 
 * @author ricky
 */
public class BatchResponseBuilder extends ResponseBuilder<BatchResponseType, BatchResponseBuilder, BatchResponseAccessor> {

    /**
     * Constructor for an empty batch response builder.
     */
    public BatchResponseBuilder() {
        super(new BatchResponseType());
    }
    
    /**
     * Adds the response inside the list of responses of the batch.
     * @param res The response to add at the end.
     * @return The same builder
     */
    public BatchResponseBuilder nestedResponse(ResponseBuilder res) {
        response.getAny().add(res.build());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<BatchResponseType> build() {
        return getBatchObjectFactory().createBatchResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BatchResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asBatch();
    }
    
}
