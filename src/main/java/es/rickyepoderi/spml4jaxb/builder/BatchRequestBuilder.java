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

import es.rickyepoderi.spml4jaxb.accessor.BatchRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BatchResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.OnErrorType;
import es.rickyepoderi.spml4jaxb.msg.batch.ProcessingType;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType;
import javax.xml.bind.JAXBElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Builder for the SPMLv2 Batch operation request. The batch operation
 * is defined inside the batch capability (capability to send several operations
 * in batch mode in a single request, it is a bunch of requests in one). The
 * request packs some other requests in a single batch operation. Only
 * some operations are allowed to be batched (see OASIS specification for
 * more details).</p>
 * 
 * @author ricky
 */
public class BatchRequestBuilder extends RequestBuilder<BatchRequestType, BatchRequestBuilder, BatchRequestAccessor, BatchResponseAccessor> {

    /**
     * The list of operations that are not allowed in a batch request.
     */
    public static final Set<Class> NON_BATCHABLE_REQUESTS
            = new HashSet<>(Arrays.asList(new Class[]{
                ListTargetsRequestType.class,
                BatchRequestType.class,
                SearchRequestType.class,
                IterateRequestType.class,
                CloseIteratorRequestType.class,
                CancelRequestType.class,
                StatusRequestType.class,
                UpdatesRequestType.class,
                es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType.class,
                es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType.class
            }));
    
    /**
     * Constructor for an empty batch request builder.
     */
    protected BatchRequestBuilder() {
        super(new BatchRequestType());
    }
    
    /**
     * Sets the processing type for the batch response. The SPMLv2 standard
     * defines two ways of executing the batch: parallel or sequential 
     * @param type The type of precessing
     * @return The same builder
     */
    public BatchRequestBuilder processing(ProcessingType type) {
        request.setProcessing(type);
        return this;
    }
    
    /**
     * Sets the processing of the batch request as parallel.
     * @return The same builder
     */
    public BatchRequestBuilder processingParallel() {
        return processing(ProcessingType.PARALLEL);
    }
    
    /**
     * Sets the processing of the requests as sequential.
     * @return The smae builder
     */
    public BatchRequestBuilder processingSequential() {
        return processing(ProcessingType.SEQUENTIAL);
    }
    
    /**
     * Sets the behavior of the batch execution when an error is returned by one
     * of the contained operations. The standard defines two ways onErrorResume
     * or onErrorExit. The former means to continue no matter the error and the 
     * second to stop execution, nevertheless if onErrorExit and parallel 
     * conflicts with each other, so the standard does not define what to do
     * with such configuration.
     * 
     * @param type The type of behavior when an error is found
     * @return The same builder
     */
    public BatchRequestBuilder onError(OnErrorType type) {
        request.setOnError(type);
        return this;
    }
    
    /**
     * Sets the behavior to onErrorResume.
     * @return The same builder
     */
    public BatchRequestBuilder onErrorResume() {
        return onError(OnErrorType.RESUME);
    }
    
    /**
     * Sets the behavior to onErrorExit.
     * @return The same builder
     */
    public BatchRequestBuilder onErrorExit() {
        return onError(OnErrorType.EXIT);
    }
    
    /**
     * Add a nested requests is added to the batch. The requests are added 
     * to the list in the order of execution. A batch request has another 
     * requests inside it. But there are some requests that cannot be batched: 
     * 3.6.3.1.1 batchRequest (normative)
     * <ul>
     * <li>{spml:ListTargetsRequestType}</li>
     * <li>{spmlbatch:BatchRequestType}</li>
     * <li>{spmlsearch:SearchRequestType}</li>
     * <li>{spmlsearch:IterateRequestType}</li>
     * <li>{spmlsearch:CloseIteratorRequestType}</li>
     * <li>{spmlasync:CancelRequestType}</li>
     * <li>{spmlasync:StatusRequestType}</li>
     * <li>{spmlupdates:UpdatesRequestType}</li>
     * <li>{spmlupdates:IterateRequestType}</li>
     * <li>{spmlupdates:CloseIteratorRequestType}</li>
     * </ul>
     * 
     * This method throws a IllegalAccessError if a non-batchable request
     * is nested.
     * 
     * @param req The request to add at the end of the list
     * @return The same builder
     */
    public BatchRequestBuilder nestedRequest(RequestBuilder req) {
        Class requestClass = req.asAccessor().getRequestClass();
        if (NON_BATCHABLE_REQUESTS.contains(requestClass)) {
            throw new IllegalAccessError("This request is not batchable.");
        }
        request.getAny().add(req.build());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<BatchRequestType> build() {
        return getBatchObjectFactory().createBatchRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BatchRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BatchRequestBuilder fromRequest(BatchRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BatchResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asBatch();
    }
    
}
