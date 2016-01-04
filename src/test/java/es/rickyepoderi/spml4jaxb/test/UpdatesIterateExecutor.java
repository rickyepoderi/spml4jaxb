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
import es.rickyepoderi.spml4jaxb.accessor.UpdatesIterateRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.UpdatesResponseBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import static es.rickyepoderi.spml4jaxb.test.UpdatesExecutor.userTypeToKind;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class UpdatesIterateExecutor implements SpmlExecutor {
    
    private WorkQueue queue = null;
    private int iteratorSize = -1;
    
    public UpdatesIterateExecutor(WorkQueue queue, int iteratorSize) {
        this.queue = queue;
        this.iteratorSize = iteratorSize;
    }
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        UpdatesIterateRequestAccessor req = request.asUpdatesIterate();
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        UpdatesResponseBuilder builder = ResponseBuilder.builderForUpdates();
        if (req.getExecutionMode() == null || req.isSynchronous()) {
            String iterId = req.getIteratorId();
            if (iterId != null) {
                UpdatesExecutor.UpdatesWrapper wrap = UpdatesExecutor.getUpdate(iterId);
                if (wrap != null) {
                    for (int i = wrap.number; i < wrap.number + iteratorSize && i < wrap.entries.length; i++) {
                        UserManager.AuditEntry e = wrap.entries[i];
                        builder.updatePsoTargetId(wrap.targetId);
                        builder.updatePsoId(e.getUid());
                        builder.updateTimestamp(e.getTime());
                        builder.updateKind(userTypeToKind(e.getType()));
                        builder.updateByCapability(UpdatesExecutor.userTypeToCapability(e.getType()));
                    }
                    wrap.number = wrap.number + iteratorSize;
                    if (wrap.number < wrap.entries.length) {
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
