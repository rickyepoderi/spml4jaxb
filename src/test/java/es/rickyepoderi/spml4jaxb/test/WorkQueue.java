/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
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
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class WorkQueue {
    
    static public class WorkItem {
        private RequestAccessor request;
        private ResponseBuilder response;
        private String id;
        private Date timestamp;
        private boolean finish;
        
        public WorkItem(String id, RequestAccessor request) {
            this.id = id;
            this.request = request;
            this.finish = false;
            this.response = null;
        }
        
        public String getId() {
            return id;
        }
        
        public boolean isFinish() {
            return finish;
        }
        
        private void finish(ResponseBuilder res) {
            this.response = res;
            System.err.println("Finish: " + res.asAccessor());
            this.timestamp = new Date();
            this.finish = true;
        }
        
        public ResponseBuilder getResponseBuilder() {
            return response;
        }
        
        public RequestAccessor getRequestAccessor() {
            return request;
        }
        
        public Date getTimestamp() {
            return timestamp;
        }
    }
    
    Deque<WorkItem> pending;
    Map<String, WorkItem> completed;
    
    public WorkQueue() {
        this.completed = new HashMap<>();
        this.pending = new ArrayDeque<>();
    }
    
    public synchronized String insert(String id, RequestAccessor req) {
        String requestId = req.getRequestId();
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }
        WorkItem item = new WorkItem(id, req);
        pending.addLast(item);
        completed.put(requestId, item);
        System.err.println(this);
        this.notifyAll();
        return requestId;
    }
    
    public WorkItem retrieve() {
        return retrieve(0L);
    }
    
    public synchronized WorkItem retrieve(long time) {
        System.err.println("retrieve size: " + pending.size());
        WorkItem req = pending.pollFirst();
        if (req == null) {
            try {
                this.wait(time);
            } catch (InterruptedException e) {}
            req = pending.pollFirst();
        }
        System.err.println(this);
        return req;
    }
    
    public synchronized void finish(WorkItem item, ResponseBuilder res) {
        completed.put(item.getId(), item);
        if (item.isFinish()) {
            throw new IllegalAccessError(String.format("The response %s is already finished", item.getId()));
        }
        item.finish(res);
    }
    
    public synchronized WorkItem status(String id) {
        return completed.get(id);
    }
    
    public synchronized WorkItem cancel(String id) {
        WorkItem item = completed.get(id);
        if (item != null && !item.isFinish()) {
            if (pending.remove(item)) {
                completed.remove(id);
                System.err.println(this);
                return item;
            }
        }
        return null;
    }
    
    
    @Override
    public String toString() {
        return new StringBuilder().append(pending.size()).append(" in pending and ")
                .append(completed.size()).append(" in completed.").toString();
    }
   
}
