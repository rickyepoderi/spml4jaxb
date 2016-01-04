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
import es.rickyepoderi.spml4jaxb.accessor.SetPasswordRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SetPasswordResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import es.rickyepoderi.spml4jaxb.user.UserNotFoundException;

/**
 *
 * @author ricky
 */
public class SetPasswordExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    
    public SetPasswordExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asSetPassword();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        SetPasswordResponseBuilder builder = ResponseBuilder.builderForSetPassword().requestId(item.getId());
        SetPasswordRequestAccessor req = item.getRequestAccessor().asSetPassword();
        try {
            User user = um.read(req.getPsoId());
            if (req.getPassword() == null) {
                builder.failure().malformedRequest().errorMessage("The new password cannot be null.");
            } else {
                if (req.getCurrentPassword() != null) {
                    // validate previous password
                    if (um.validatePassword(user.getUid(), req.getCurrentPassword())) {
                        // ok => change it
                        um.setPassword(user.getUid(), req.getPassword());
                    } else {
                        builder.failure().customError().errorMessage("Invalid current password.");
                    }
                } else {
                    // just change it
                    um.setPassword(user.getUid(), req.getPassword());
                }
                builder.success();
            }
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
