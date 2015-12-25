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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.OnErrorType;
import es.rickyepoderi.spml4jaxb.msg.batch.ProcessingType;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;
import javax.xml.bind.JAXBElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ricky
 */
public class BatchRequestBuilder extends RequestBuilder<BatchRequestType, BatchRequestBuilder> {

    public static final Set<Class> NON_BATCHABLE_REQUESTS
            = new HashSet<>(Arrays.asList(new Class[]{
                ListTargetsRequestType.class,
                BatchRequestType.class,
                SearchRequestType.class,
                IterateRequestType.class,
                CloseIteratorRequestType.class,
                CancelRequestType.class,
                StatusRequestType.class,
                //UpdatesRequestType.class,
                //IterateRequestType.class,
                //CloseIteratorRequestType.class
            }));
    
    protected BatchRequestBuilder() {
        super(new BatchRequestType());
    }
    
    public BatchRequestBuilder processing(ProcessingType type) {
        request.setProcessing(type);
        return this;
    }
    
    public BatchRequestBuilder processingParallel() {
        return processing(ProcessingType.PARALLEL);
    }
    
    public BatchRequestBuilder processingSequential() {
        return processing(ProcessingType.SEQUENTIAL);
    }
    
    public BatchRequestBuilder onError(OnErrorType type) {
        request.setOnError(type);
        return this;
    }
    
    public BatchRequestBuilder onErrorResume() {
        return onError(OnErrorType.RESUME);
    }
    
    public BatchRequestBuilder onErrorExit() {
        return onError(OnErrorType.EXIT);
    }
    
    /**
     * A batch request has another requests inside it. But there are some
     * requests that cannot be batched: 3.6.3.1.1 batchRequest (normative)
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
     * @param req
     * @return 
     */
    public BatchRequestBuilder nestedResponse(RequestBuilder req) {
        Class requestClass = req.asAccessor().getRequestClass();
        if (NON_BATCHABLE_REQUESTS.contains(requestClass)) {
            throw new IllegalAccessError("This request is not batchable.");
        }
        request.getAny().add(req.build());
        return this;
    }

    @Override
    public JAXBElement<BatchRequestType> build() {
        return getBatchObjectFactory().createBatchRequest(request);
    }
    
    @Override
    public RequestAccessor asAccessor() {
        return super.asAccessor().asBatch();
    }
    
}
