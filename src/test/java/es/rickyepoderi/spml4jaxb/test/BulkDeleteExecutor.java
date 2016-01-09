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

import es.rickyepoderi.spml4jaxb.accessor.BulkDeleteRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.builder.BulkDeleteResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author ricky
 */
public class BulkDeleteExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    
    public BulkDeleteExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }

    @Override
    public BaseRequestAccessor specificAccessor(RequestAccessor request) {
        return request.asBulkDelete();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        BulkDeleteResponseBuilder builder = ResponseBuilder.builderForBulkDelete().requestId(item.getId());
        BulkDeleteRequestAccessor req = item.getRequestAccessor().asBulkDelete();
        try {
            SearchQueryAccessor query = req.getQuery();
            if (query != null) {
                if (query.isTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                    // DSML target
                    FilterAccessor filter = query.getQueryFilter();
                    int rows = um.bulkDelete(SearchExecutor.filter2SQL(filter));
                    System.err.println("Deleted rows: " + rows);
                    builder.success();
                } else if (query.isTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                    String xpathString = query.getXsdXPathSelection();
                    if (xpathString != null) {
                        int rows = um.bulkDelete(SearchExecutor.xpath2SQL(xpathString));
                        System.err.println("Deleted rows: " + rows);
                        builder.success();
                    } else {
                        builder.failure().malformedRequest().errorMessage("The XPath is compulsoty in a XSD searxh.");
                    }
                } else {
                    builder.failure().invalidIdentifier().errorMessage("Invalid search target id");
                }
            }
        } catch (ManagerException | XPathExpressionException e) {
            builder.failure().customError().errorMessage(e.getMessage());
        }
        queue.finish(item, builder);
        return builder;
    }
    
}
