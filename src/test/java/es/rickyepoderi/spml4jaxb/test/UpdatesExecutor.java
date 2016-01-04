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

import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.UpdatesResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateKindType;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author ricky
 */
public class UpdatesExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    private int iteratorSize = -1;
    
    private static Map<String, UpdatesWrapper> updates = new ConcurrentHashMap<>();
    
    protected class UpdatesWrapper {
        UserManager.AuditEntry[] entries;
        int number;
        String targetId;
    }
    
    static protected UpdatesWrapper getUpdate(String id) {
        return updates.get(id);
    }
    
    static protected boolean removeUpdate(String id) {
        return updates.remove(id) != null;
    }
    
    public UpdatesExecutor(UserManager um, WorkQueue queue, int iteratorSize) {
        super(queue);
        this.um = um;
        this.iteratorSize = iteratorSize;
    }
    
    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asUpdates();
    }
    
    static protected List<UserManager.AuditTypeOperation> capabilityToUserType(String[] capabilities) {
        if (capabilities == null || capabilities.length == 0) {
            return null;
        } else {
            List<UserManager.AuditTypeOperation> res = new ArrayList<>();
            if (Arrays.binarySearch(capabilities, RequestAccessor.SPML_CAPABILITY_CORE_URI) >= 0) {
                res.add(UserManager.AuditTypeOperation.CREATE);
                res.add(UserManager.AuditTypeOperation.UPDATE);
                res.add(UserManager.AuditTypeOperation.DELETE);
            } else if (Arrays.binarySearch(capabilities, RequestAccessor.SPML_CAPABILITY_PASSWORD_URI) >= 0) {
                res.add(UserManager.AuditTypeOperation.SET_PASSWORD);
                res.add(UserManager.AuditTypeOperation.RESET_PASSWORD);
                res.add(UserManager.AuditTypeOperation.EXPIRE_PASSWORD);
            } else if (Arrays.binarySearch(capabilities, RequestAccessor.SPML_CAPABILITY_SUSPEND_URI) >= 0) {
                res.add(UserManager.AuditTypeOperation.DISABLE);
                res.add(UserManager.AuditTypeOperation.ENABLE);
            } else if (Arrays.binarySearch(capabilities, RequestAccessor.SPML_CAPABILITY_BULK_URI) >= 0) {
                res.add(UserManager.AuditTypeOperation.BULK_DELETE);
                res.add(UserManager.AuditTypeOperation.BULK_MODIFY);
            }
            return res;
        }
    }
    
    static protected String userTypeToCapability(UserManager.AuditTypeOperation type) {
        if (type != null) {
            switch(type) {
                case CREATE:
                case UPDATE:
                case DELETE:
                    return RequestAccessor.SPML_CAPABILITY_CORE_URI;
                case SET_PASSWORD:
                case EXPIRE_PASSWORD:
                case RESET_PASSWORD:
                    return RequestAccessor.SPML_CAPABILITY_PASSWORD_URI;
                case DISABLE:
                case ENABLE:
                    return RequestAccessor.SPML_CAPABILITY_SUSPEND_URI;
                case BULK_MODIFY:
                case BULK_DELETE:
                    return RequestAccessor.SPML_CAPABILITY_BULK_URI;
                default:
                    return null;
            }
        } else {
            return null;
        }
    }
    
    static protected UpdateKindType userTypeToKind(UserManager.AuditTypeOperation type) {
        if (type != null) {
            switch(type) {
                case CREATE:
                    return UpdateKindType.ADD;
                case UPDATE:
                    return UpdateKindType.MODIFY;
                case DELETE:
                    return UpdateKindType.DELETE;
                case BULK_MODIFY:
                    return UpdateKindType.MODIFY;
                case BULK_DELETE:
                    return UpdateKindType.DELETE;
                default:
                    return UpdateKindType.CAPABILITY;
            }
        } else {
            return UpdateKindType.CAPABILITY;
        }
    }
    
    protected void process(UpdatesRequestAccessor req, UpdatesResponseBuilder builder
                ) throws ManagerException, XPathExpressionException {
        // perform the search based on time and types
        Date since = req.getUpdatedSince();
        List<UserManager.AuditTypeOperation> capabilities = capabilityToUserType(req.getUpdatedByCapability());
        List<UserManager.AuditEntry> entries = um.searchAudit(since, capabilities);
        System.err.println(entries.size());
        int i = 0;
        List<UserManager.AuditEntry> result = new ArrayList<>();
        String iterId = null;
        for (UserManager.AuditEntry e : entries) {
            i++;
            if (i <= iteratorSize) {
                // add to the current response
                builder.updatePsoTargetId(req.getQuery().getTargetId());
                builder.updatePsoId(e.getUid());
                builder.updateTimestamp(e.getTime());
                builder.updateKind(userTypeToKind(e.getType()));
                builder.updateByCapability(userTypeToCapability(e.getType()));
                builder.nextUpdate();
            } else if (i == iteratorSize + 1) {
                // the search should be added to the map and iterator returned
                iterId = UUID.randomUUID().toString();
                builder.iteratorId(iterId);
                result.add(e);
            } else {
                // just add to the iterator search saved
                result.add(e);
            }
        }
        if (iterId != null) {
            // add to the searched
            UpdatesWrapper wrap = new UpdatesWrapper();
            wrap.entries = result.toArray(new UserManager.AuditEntry[0]);
            wrap.number = 0;
            wrap.targetId = req.getQuery().getTargetId();
            updates.put(iterId, wrap);
        }
        builder.success();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        UpdatesResponseBuilder builder = ResponseBuilder.builderForUpdates().requestId(item.getId());
        UpdatesRequestAccessor req = item.getRequestAccessor().asUpdates();
        try {
            SearchQueryAccessor query = req.getQuery();
            if (query != null) {
                if (query.isTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                    FilterAccessor filter = query.getQueryFilter();
                    String customFilter = SearchExecutor.filter2SQL(filter);
                    if (customFilter != null && !customFilter.isEmpty()) {
                        // we cannot support object fliltering for updates
                        builder.failure().customError().errorMessage("The updates implementation does not support filtering");
                    } else {
                        process(req, builder);
                    }
                } else if (query.isTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                    String selector = query.getXsdXPathSelection();
                    String customFilter = SearchExecutor.xpath2SQL(selector);
                    if (customFilter != null && !customFilter.isEmpty()) {
                        // we cannot support object fliltering for updates
                        builder.failure().customError().errorMessage("The updates implementation does not support filtering");
                    } else {
                        process(req, builder);
                    }
                } else {
                    builder.failure().invalidIdentifier().errorMessage("Invalid search target id");
                }
            } else {
                builder.failure().malformedRequest().errorMessage("There are two targets, a search element is needed.");
            }
        } catch (ManagerException | XPathExpressionException e) {
            builder.failure().customError().errorMessage(e.getMessage());
        }
        queue.finish(item, builder);
        return builder;
    }
    
}
