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

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public abstract class AsyncSpmlBaseExecutor implements SpmlExecutor, AsyncSpmlExecutor {

    protected WorkQueue queue;
    
    public AsyncSpmlBaseExecutor(WorkQueue queue) {
        this.queue = queue;
    }
    
    @Override
    public abstract ResponseBuilder realExecute(WorkQueue.WorkItem item);

    public abstract RequestAccessor specificAccessor(RequestAccessor request);
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        RequestAccessor req = specificAccessor(request);
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        ResponseBuilder builder = null;
        if (req.isSynchronous()) {
            // do it right now
            builder = realExecute(new WorkQueue.WorkItem(id, req));
        } else {
            builder = req.responseBuilder().requestId(id).pending();
            queue.insert(id, req);
        }
        return builder;
    }
    
    
}
