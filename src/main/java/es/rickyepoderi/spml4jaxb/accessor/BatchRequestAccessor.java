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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.OnErrorType;
import es.rickyepoderi.spml4jaxb.msg.batch.ProcessingType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class BatchRequestAccessor extends RequestAccessor<BatchRequestType> {

    protected BatchRequestAccessor(BatchRequestType request) {
        super(request, null, null);
    }
    
    public boolean isProcessingParallel() {
        return ProcessingType.PARALLEL.equals(request.getProcessing());
    }
    
    public boolean isProcessingSequential() {
        return ProcessingType.SEQUENTIAL.equals(request.getProcessing());
    }
    
    public boolean isOnErrorResume() {
        return OnErrorType.RESUME.equals(request.getOnError());
    }
    
    public boolean isOnErrorExit() {
        return OnErrorType.EXIT.equals(request.getOnError());
    }
    
    public RequestAccessor[] getNestedRequests() {
        List<RequestAccessor> res = new ArrayList<>();
        List<Object> l = request.getAny();
        for (Object o : l) {
            if (o instanceof RequestType) {
                res.add(RequestAccessor.accessorForRequest((RequestType) o));
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof RequestType) {
                    res.add(RequestAccessor.accessorForRequest((RequestType) el.getValue()));
                }
            }
        }
        return res.toArray(new RequestAccessor[0]);
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForBatch();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        for (RequestAccessor req: getNestedRequests()) {
            sb.append(req);
        }
        return sb.toString();
    }
    
}
