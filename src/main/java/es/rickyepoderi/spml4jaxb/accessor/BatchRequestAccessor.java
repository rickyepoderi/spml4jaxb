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

import es.rickyepoderi.spml4jaxb.builder.BatchRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.OnErrorType;
import es.rickyepoderi.spml4jaxb.msg.batch.ProcessingType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 * <p>Accessor for the SPMLv2 Batch operation request. The batch operation
 * is defined inside the batch capability (capability to send several operations
 * in batch mode in a single request, it is a bunch of requests in one). The
 * request packs some other requests in a single batch operation. Only
 * some operations are allowed to be batched (see OASIS specification for
 * more details).</p>
 * 
 * @author ricky
 */
public class BatchRequestAccessor extends BaseRequestAccessor<BatchRequestType, BatchRequestAccessor, BatchRequestBuilder> {

    /**
     * Constructor for a empty batch request accessor.
     */
    protected BatchRequestAccessor() {
        this(new BatchRequestType());
    }
    
    /**
     * Constructor for a batch request accessor based on the internal type.
     * @param request The batch request type obtained by JAXB from the standard SPMLv2 XSD files
     */
    protected BatchRequestAccessor(BatchRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Returns if the operation should be executed in parallel. The standard
     * defines two ways of execution: parallel or sequential.
     * @return true if the request is defined as parallel execution, false otherwise
     */
    public boolean isProcessingParallel() {
        return ProcessingType.PARALLEL.equals(request.getProcessing());
    }
    
    /**
     * Returns if the operation should be executed sequentially. The standard
     * defines two ways of execution: parallel or sequential.
     * @return true if the request is defined as sequential execution, false otherwise
     */
    public boolean isProcessingSequential() {
        return ProcessingType.SEQUENTIAL.equals(request.getProcessing());
    }
    
    /**
     * Returns if the batch requests should continue (resume) on error. The SPMLv2
     * standard defines that the server should continue executing the batch if some
     * error is obtained in any batched operation.
     * 
     * @return true if the execution should resume on error, false otherwise
     */
    public boolean isOnErrorResume() {
        return OnErrorType.RESUME.equals(request.getOnError());
    }
    
    /**
     * Returns if the batch requests should exit on error. The SPMLv2
     * standard defines that the server should exit after a failed operation
     * if executed sequentially, in parallel mode the standard does not 
     * enforce any behavior.
     * 
     * @return true if the execution should exit on error, false otherwise
     */
    public boolean isOnErrorExit() {
        return OnErrorType.EXIT.equals(request.getOnError());
    }
    
    /**
     * Method that returns the nested requests to be executed in batch mode.
     * It returns an array of an unknown accessors.
     * 
     * @return The array of accessors or an empty array
     */
    public RequestAccessor[] getNestedRequests() {
        List<RequestAccessor> res = new ArrayList<>();
        List<Object> l = request.getAny();
        for (Object o : l) {
            if (o instanceof RequestType) {
                res.add(BaseRequestAccessor.accessorForRequest((RequestType) o));
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof RequestType) {
                    res.add(BaseRequestAccessor.accessorForRequest((RequestType) el.getValue()));
                }
            }
        }
        return res.toArray(new RequestAccessor[0]);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForBatch();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BatchRequestBuilder toBuilder() {
        return RequestBuilder.builderForBatch().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BatchRequestAccessor asAccessor(BatchRequestType request) {
        return new BatchRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        for (BaseRequestAccessor req: getNestedRequests()) {
            sb.append(req);
        }
        return sb.toString();
    }
    
}
