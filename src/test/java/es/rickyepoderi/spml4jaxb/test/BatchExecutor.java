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

import es.rickyepoderi.spml4jaxb.accessor.BatchRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.BatchResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class BatchExecutor extends AsyncSpmlBaseExecutor {

    private Map<Class, SpmlExecutor> mapper = null;
    
    public BatchExecutor(WorkQueue queue, Map<Class, SpmlExecutor> mapper) {
        super(queue);
        this.mapper = mapper;
    }

    @Override
    public BaseRequestAccessor specificAccessor(RequestAccessor request) {
        return request.asBatch();
    }
    
    private boolean checkValidRequest(Class clazz) {
        return !Arrays.asList(new Class[] {
            es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType.class,
            es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType.class
        }).contains(clazz);
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        BatchResponseBuilder builder = ResponseBuilder.builderForBatch().requestId(item.getId());
        BatchRequestAccessor req = item.getRequestAccessor().asBatch();
        if (req.getNestedRequests().length == 0) {
            // invalid data
            builder.failure().malformedRequest().errorMessage("A batch request should have some nested requests.");
        } else {
            builder.success();
            // check if processing id parallel or sequential
            if (req.isProcessingSequential()) {
                // process the operation one by one by myself
                for (RequestAccessor nested : req.getNestedRequests()) {
                    ResponseBuilder res = null;
                    if (checkValidRequest(nested.getRequestClass())) {
                        SpmlExecutor executor = mapper.get(nested.getRequestClass());
                        // execute synchronously always one by one
                        String id = nested.getRequestId() == null ? UUID.randomUUID().toString() : nested.getRequestId();
                        WorkQueue.WorkItem nestedItem = new WorkQueue.WorkItem(id,
                                ((AsyncSpmlBaseExecutor) executor).specificAccessor(nested));
                        res = ((AsyncSpmlBaseExecutor) executor).realExecute(nestedItem);
                    } else {
                        res = nested.responseBuilder().failure().malformedRequest().errorMessage("Request cannot be batched!");
                    }
                    builder.nestedResponse(res);
                    if (res.asAccessor().isFailure()) {
                        builder.failure();
                        if (req.isOnErrorExit()) {
                            // break if sequential and exit on error
                            break;
                        }
                    }
                }
            } else {
                // parallel processing => add all of them to the queue for processing in parallel
                List<String> ids = new ArrayList<>(req.getNestedRequests().length);
                for (RequestAccessor nested : req.getNestedRequests()) {
                    String id = nested.getRequestId() == null ? UUID.randomUUID().toString() : nested.getRequestId();
                    AsyncSpmlBaseExecutor executor = (AsyncSpmlBaseExecutor) mapper.get(nested.getRequestClass());
                    queue.insert(id, executor.specificAccessor(nested));
                    ids.add(id);
                }
                // now wait for all the internal requests to finish
                boolean finished = false;
                while (!finished) {
                    try {Thread.sleep(500L);} catch(InterruptedException e) {}
                    finished = true;
                    for (String id: ids) {
                        WorkQueue.WorkItem nestedItem = queue.status(id);
                        if (nestedItem.isPending()) {
                            finished = false;
                            break;
                        }
                    }
                }
                // all the inner tasks are finished
                builder.success();
                for (String id : ids) {
                    WorkQueue.WorkItem nestedItem = queue.status(id);
                    builder.nestedResponse(nestedItem.getResponseAccessor().toBuilder());
                    if (nestedItem.getResponseAccessor().isFailure()) {
                        builder.failure();
                    }
                }
            }
        }
        queue.finish(item, builder);
        return builder;
    }
    
}
