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

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResetPasswordResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import es.rickyepoderi.spml4jaxb.user.UserNotFoundException;

/**
 *
 * @author ricky
 */
public class ResetPasswordExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um = null;
    
    public ResetPasswordExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public BaseRequestAccessor specificAccessor(RequestAccessor request) {
        return request.asResetPassword();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        ResetPasswordResponseBuilder builder = ResponseBuilder.builderForResetPassword().requestId(item.getId());
        ResetPasswordRequestAccessor req = item.getRequestAccessor().asResetPassword();
        try {
            String password = um.resetPassword(req.getPsoId());
            builder.success().password(password);
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
