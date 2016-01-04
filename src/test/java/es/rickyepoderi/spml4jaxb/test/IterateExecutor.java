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

import es.rickyepoderi.spml4jaxb.accessor.IterateRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SearchResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import es.rickyepoderi.spml4jaxb.test.SearchExecutor.SearchWrapper;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class IterateExecutor implements SpmlExecutor {

    private WorkQueue queue = null;
    private int iteratorSize = -1;
    
    public IterateExecutor(WorkQueue queue, int iteratorSize) {
        this.iteratorSize = iteratorSize;
        this.queue = queue;
    }
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        IterateRequestAccessor req = request.asIterate();
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        SearchResponseBuilder builder = ResponseBuilder.builderForSearch();
        if (req.getExecutionMode() == null || req.isSynchronous()) {
            String iterId = req.getIteratorId();
            if (iterId != null) {
                SearchWrapper wrap = SearchExecutor.getSearch(iterId);
                if (wrap != null) {
                    for (int i = wrap.number; i < wrap.number + iteratorSize && i < wrap.psos.length; i++) {
                        builder.pso(wrap.psos[i]).nextPso();
                    }
                    wrap.number = wrap.number + iteratorSize;
                    if (wrap.number < wrap.psos.length) {
                        builder.iteratorId(iterId);
                    }
                    builder.success();
                } else {
                    builder.failure().invalidIdentifier()
                        .errorMessage("The iterator id is not saved.");
                }
            } else {
                builder.failure().invalidIdentifier()
                        .errorMessage("The iterator id should not be null.");
            }
        } else {
            builder.failure()
                    .unsupportedExecutionMode()
                    .errorMessage("Iterate should be synchronous by the standard");
        }
        queue.finish(new WorkQueue.WorkItem(id, req), builder);
        return builder;
    }
    
}
