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
package es.rickyepoderi.spml4jaxb.test;

import es.rickyepoderi.spml4jaxb.accessor.CloseIteratorRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.CloseIteratorResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class CloseIteratorExecutor implements SpmlExecutor {

    private WorkQueue queue = null;
    
    public CloseIteratorExecutor(WorkQueue queue) {
        this.queue = queue;
    }
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        CloseIteratorRequestAccessor req = request.asCloseIterator();
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        CloseIteratorResponseBuilder builder = ResponseBuilder.builderForCloseIterator();
        if (req.getExecutionMode() == null || req.isSynchronous()) {
            String iterId = req.getIteratorId();
            if (iterId != null) {
                boolean removed = SearchExecutor.removeSearch(iterId);
                if (removed) {
                    builder.success();
                } else {
                    builder.failure().invalidIdentifier().errorMessage("The iterator id is not saved.");
                }
            } else {
                builder.failure().invalidIdentifier().errorMessage("The iterator id should not be null.");
            }
        } else {
            builder.failure().unsupportedExecutionMode().errorMessage("CloseIterator should be synchronous by the standard");
        }
        queue.finish(new WorkQueue.WorkItem(id, req), builder);
        return builder;
    }
    
}
