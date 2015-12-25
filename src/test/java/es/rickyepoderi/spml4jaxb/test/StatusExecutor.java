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
package es.rickyepoderi.spml4jaxb.test;

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.StatusRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.StatusResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class StatusExecutor implements SpmlExecutor {

    private WorkQueue queue;
    
    public StatusExecutor(WorkQueue queue) {
        this.queue = queue;
    }
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        StatusRequestAccessor req = request.asStatus();
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        StatusResponseBuilder builder = ResponseBuilder.builderForStatus().requestId(id);
        if (req.getExecutionMode() == null || req.isSynchronous()) {
            if (req.getAsyncRequestId() == null) {
                return builder.failure().invalidIdentifier().errorMessage("The asyncRequestId is null");
            } else {
                WorkQueue.WorkItem item = queue.status(req.getAsyncRequestId());
                if (item == null) {
                    return builder.failure().noSuchRequest().errorMessage("The asyncRequestId does not exists.");
                } else {
                    builder.asyncRequestId(req.getAsyncRequestId());
                    if (item.isFinish()) {
                        if (req.isReturnResults()) {
                            // add all the results inside the nested response
                            builder.nestedResponse(item.getResponseBuilder());
                        } else {
                            // just add a response with the status
                            ResponseBuilder nestedBuilder = item.getRequestAccessor().responseBuilder()
                                    .requestId(item.getId()).status(item.getResponseBuilder().asAccessor().getStatus());
                            return builder.nestedResponse(nestedBuilder).success();
                        }
                        return builder.success();
                    } else {
                        // pending => create the operation is still in pending
                        ResponseBuilder nestedBuilder = item.getRequestAccessor().responseBuilder()
                                .requestId(item.getId()).pending();
                        return builder.nestedResponse(nestedBuilder).success();
                    }
                }
            }
        } else {
            return builder.failure()
                    .unsupportedExecutionMode()
                    .errorMessage("Status should be synchronous by the standard");
        }
    }
    
}
