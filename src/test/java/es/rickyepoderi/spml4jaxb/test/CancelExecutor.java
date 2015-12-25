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

import es.rickyepoderi.spml4jaxb.accessor.CancelRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.CancelResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class CancelExecutor implements SpmlExecutor {

    private WorkQueue queue;
    
    public CancelExecutor(WorkQueue queue) {
        this.queue = queue;
    }
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        CancelRequestAccessor req = request.asCancel();
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        CancelResponseBuilder builder = ResponseBuilder.builderForCancel()
                .requestId(id).asyncRequestId(req.getAsyncRequestId());
        if (req.getExecutionMode() == null || req.isSynchronous()) {
            if (req.getAsyncRequestId() == null) {
                return builder.failure().invalidIdentifier().errorMessage("The asyncRequestId is null");
            } else {
                WorkQueue.WorkItem item = queue.cancel(req.getAsyncRequestId());
                if (item == null) {
                    return builder.failure().noSuchRequest()
                            .errorMessage("The asyncRequestId is not in the pending list.");
                } else {
                    return builder.asyncRequestId(req.getAsyncRequestId()).success();
                }
            }
        } else {
            return builder.failure()
                    .unsupportedExecutionMode()
                    .errorMessage("Cancel should be synchronous by the standard");
        }
    }
    
}
