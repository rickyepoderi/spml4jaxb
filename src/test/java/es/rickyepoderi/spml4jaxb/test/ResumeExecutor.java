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
import es.rickyepoderi.spml4jaxb.accessor.ResumeRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResumeResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import es.rickyepoderi.spml4jaxb.user.UserNotFoundException;
import java.util.Date;

/**
 *
 * @author ricky
 */
public class ResumeExecutor extends AsyncSpmlBaseExecutor {
    
    private UserManager um;
    
    public ResumeExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asResume();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        ResumeResponseBuilder builder = ResponseBuilder.builderForResume().requestId(item.getId());
        ResumeRequestAccessor req = item.getRequestAccessor().asResume();
        try {
            Date effectiveDate = new Date();
            if (req.getEffectiveDate() != null) {
                effectiveDate = req.getEffectiveDate();
            }
            um.enable(req.getPsoId(), effectiveDate);
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
