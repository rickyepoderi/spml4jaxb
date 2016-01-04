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

import es.rickyepoderi.spml4jaxb.accessor.LookupRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.LookupResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import static es.rickyepoderi.spml4jaxb.test.AddExecutor.user2Dsml;
import es.rickyepoderi.spml4jaxb.user.InvalidUserException;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import es.rickyepoderi.spml4jaxb.user.UserNotFoundException;

/**
 *
 * @author ricky
 */
public class LookupExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    
    public LookupExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asLookup();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        LookupResponseBuilder builder = ResponseBuilder.builderForLookup().requestId(item.getId());
        LookupRequestAccessor req = item.getRequestAccessor().asLookup();
        try {
            User u = null;
            if (req.getPsoId() == null) {
                builder.failure().invalidIdentifier()
                        .errorMessage("The psoId should be provided.");
            } else if (req.isPsoTargetId(ListTargetsExecutor.DSML_TARGET_ID)
                    || req.isPsoTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                u = um.read(req.getPsoId());
                builder.psoId(u.getUid()).psoTargetId(req.getPsoTargetId());
                if (req.isPsoTargetId(ListTargetsExecutor.DSML_TARGET_ID)
                        && (req.isReturnData() || req.isReturnEverything())) {
                    builder.dsmlAttribute(user2Dsml(u));
                } else if (req.isPsoTargetId(ListTargetsExecutor.XSD_TARGET_ID)
                        && (req.isReturnData() || req.isReturnEverything())) {
                    builder.xsdObject(u);
                }
                builder.success();
            } else {
                builder.failure().invalidIdentifier()
                        .errorMessage("Invalid targetId in PSO.");
            }
        } catch (UserNotFoundException e) {
            builder.failure()
                    .noSuchIdentifier()
                    .errorMessage(e.getMessage());
        } catch (InvalidUserException e) {
            builder.failure()
                    .unsupportedIdentifierType()
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
