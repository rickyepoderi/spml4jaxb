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

import es.rickyepoderi.spml4jaxb.accessor.ExpirePasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ExpirePasswordResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import es.rickyepoderi.spml4jaxb.user.UserNotFoundException;

/**
 *
 * @author ricky
 */
public class ExpirePasswordExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um = null;
    
    public ExpirePasswordExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asExpirePassword();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        ExpirePasswordResponseBuilder builder = ResponseBuilder.builderForExpirePassword().requestId(item.getId());
        ExpirePasswordRequestAccessor req = item.getRequestAccessor().asExpirePassword();
        try {
            um.expirePassword(req.getPsoId(), req.getRemainingLoggings());
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
        queue.finish(item, builder);
        return builder;
    }
    
}
