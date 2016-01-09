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

import es.rickyepoderi.spml4jaxb.accessor.DeleteRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.DeleteResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import es.rickyepoderi.spml4jaxb.user.UserNotFoundException;

/**
 *
 * @author ricky
 */
public class DeleteExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um = null;
    
    public DeleteExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public BaseRequestAccessor specificAccessor(RequestAccessor request) {
        return request.asDelete();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        DeleteResponseBuilder builder = ResponseBuilder.builderForDelete().requestId(item.getId());
        DeleteRequestAccessor req = item.getRequestAccessor().asDelete();
        if (req.getPsoId() != null) {
            if (req.isPsoTargetId(ListTargetsExecutor.DSML_TARGET_ID)
                    || req.isPsoTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                try {
                    System.err.println("Deleting " + req.getPsoId());
                    um.delete(req.getPsoId());
                    builder.success();
                } catch (UserNotFoundException e) {
                    builder.failure()
                            .noSuchIdentifier()
                            .errorMessage(e.getMessage());
                } catch (ManagerException e) {
                    builder.failure()
                            .customError()
                            .errorMessage(e.getMessage());
                }
            } else {
                builder.failure().noSuchIdentifier()
                        .errorMessage("The psoId does not exists");
            }
        } else {
            builder.failure().invalidIdentifier()
                    .errorMessage("The psoId should be provided");
        }
        queue.finish(item, builder);
        return builder;
    }
    
}
