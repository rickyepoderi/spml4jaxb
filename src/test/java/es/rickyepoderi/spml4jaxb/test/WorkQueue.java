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
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author ricky
 */
public class WorkQueue {
    
    protected static final Logger log = Logger.getLogger(WorkQueue.class.getName());
    
    static public enum WorkItemStatus {PENDING, FINISHED, CANCELED};
    
    static public class WorkItem {
        private BaseRequestAccessor request;
        private BaseResponseAccessor response;
        private String id;
        private Date timestamp;
        private WorkItemStatus status;
        
        public WorkItem(String id, BaseRequestAccessor request) {
            this.id = id;
            this.request = request;
            this.status = WorkItemStatus.PENDING;
            this.response = null;
            this.timestamp = null;
        }
        
        public String getId() {
            return id;
        }
        
        public boolean isPending() {
            return WorkItemStatus.PENDING.equals(this.status);
        }
        
        public boolean isFinished() {
            return WorkItemStatus.FINISHED.equals(this.status);
        }
        
        public boolean isCanceled() {
            return WorkItemStatus.CANCELED.equals(this.status);
        }
        
        public WorkItemStatus getStatus() {
            return this.status;
        }
        
        public void finish(JAXBContext ctx, Connection conn, ResponseBuilder res) {
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE WORKITEM SET STATUS=?,RESPONSE=? WHERE ID=?");
                    StringWriter sw = new StringWriter()) {
                this.response = res.asAccessor();
                log.log(Level.FINE, "Finish: {0}", this.response);
                this.timestamp = new Date();
                this.status = WorkItemStatus.FINISHED;
                pstmt.setByte(1, (byte) this.status.ordinal());
                Marshaller marshaller = ctx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(res.build(), sw);
                pstmt.setString(2, sw.toString());
                pstmt.setString(3, this.id);
                int n = pstmt.executeUpdate();
                if (n == 0) {
                    // it was a executed synchronously and means an insert
                    this.insert(ctx, conn);
                }
                conn.commit();
            } catch (IOException | JAXBException | SQLException e) {
                try {conn.rollback();} catch (SQLException ex) {};
                throw new IllegalStateException(e);
            }
        }
        
        public void cancel(Connection conn) throws SQLException {
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE WORKITEM SET STATUS=? WHERE ID=?")) {
                this.timestamp = new Date();
                this.status = WorkItemStatus.CANCELED;
                pstmt.setByte(1, (byte) this.status.ordinal());
                pstmt.setString(2, this.id);
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                try {conn.rollback();} catch (SQLException ex) {};
                throw e;
            }
        }
        
        public BaseResponseAccessor getResponseAccessor() {
            return response;
        }
        
        public BaseRequestAccessor getRequestAccessor() {
            return request;
        }
        
        public Date getTimestamp() {
            return timestamp;
        }
        
        public void insert(JAXBContext ctx, Connection conn) {
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO WORKITEM(ID,STATUS,REQUEST,CLASS,RESPONSE) VALUES(?,?,?,?,?)");
                    StringWriter sw = new StringWriter()) {
                pstmt.setString(1, this.id);
                pstmt.setByte(2, (byte) this.status.ordinal());
                // marshall request
                Marshaller marshaller = ctx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(this.request.toBuilder().build(), sw);
                sw.flush();
                pstmt.setString(3, sw.toString());
                // set the class
                pstmt.setString(4, this.request.getClass().getName());
                // marshall response
                if (this.response != null) {
                    sw.getBuffer().setLength(0);
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(this.response.toBuilder().build(), sw);
                    sw.flush();
                    pstmt.setString(5, sw.toString());
                } else {
                    pstmt.setString(5, null);
                }
                pstmt.executeUpdate();
                conn.commit();
            } catch (IOException | JAXBException | SQLException e) {
                try {conn.rollback();} catch (SQLException ex) {};
                throw new IllegalStateException(e);
            }
        }
        
        public static WorkItem find(JAXBContext ctx, Connection conn, String id, 
                WorkItemStatus status) {
            WorkItem item = null;
            String sql = "SELECT REQUEST,RESPONSE,STATUS,CLASS FROM WORKITEM WHERE ID=?";
            if (status != null) {
                sql += " AND STATUS=?";
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                if (status != null) {
                    pstmt.setByte(2, (byte) status.ordinal());
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String clazzName = rs.getString("CLASS");
                        log.log(Level.FINE, "Request class: {0}", clazzName);
                        Class clazz = Class.forName(clazzName);
                        Unmarshaller unmarshaller = ctx.createUnmarshaller();
                        JAXBElement<RequestType> resquestel = (JAXBElement<RequestType>) unmarshaller.unmarshal(
                                new StringReader(rs.getString("REQUEST")));
                        RequestType request = resquestel.getValue();
                        BaseRequestAccessor requestAccessor = BaseRequestAccessor.accessorForRequest(request, clazz);
                        item = new WorkItem(id, requestAccessor);
                        item.status = WorkItemStatus.values()[rs.getByte("STATUS")];
                        String res = rs.getString("RESPONSE");
                        if (res != null) {
                            JAXBElement<ResponseType> responseel = (JAXBElement<ResponseType>) unmarshaller.unmarshal(new StringReader(res));
                            BaseResponseAccessor responseAccessor = requestAccessor.responseBuilder().asAccessor();
                            item.response = BaseResponseAccessor.accessorForResponse(responseel.getValue(), responseAccessor.getClass());
                        }
                    }
                }
                conn.commit();
                return item;
            } catch (JAXBException | SQLException | ClassNotFoundException e) {
                try {conn.rollback();} catch (SQLException ex) {};
                throw new IllegalStateException(e);
            }
        }
    }
    
    private DataSource ds = null;
    private JAXBContext ctx = null;
    private Deque<String> pending;
    
    public WorkQueue(JAXBContext ctx, DataSource ds) throws SQLException {
        this.pending = new ArrayDeque<>();
        this.ds = ds;
        this.ctx = ctx;
        this.initialize();
    }
    
    final public void initialize() throws SQLException {
        try (Connection conn = this.ds.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement("CREATE TABLE WORKITEM("
                    + "ID VARCHAR(50) NOT NULL PRIMARY KEY,"
                    + "DATE TIMESTAMP,"
                    + "STATUS TINYINT NOT NULL,"
                    + "REQUEST CLOB NOT NULL,"
                    + "CLASS VARCHAR(100) NOT NULL,"
                    + "RESPONSE CLOB)")) {
                pstmt.executeUpdate();
                conn.commit();
            }
        }
    }
    
    public synchronized String insert(String id, BaseRequestAccessor req) {
        String requestId = req.getRequestId();
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }
        WorkItem item = new WorkItem(id, req);
        try {
            try (Connection conn = this.ds.getConnection()) {
                pending.addLast(item.getId());
                item.insert(ctx, conn);
                log.log(Level.FINE, "Status: {0}", this);
                this.notifyAll();
                return requestId;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public WorkItem retrieve() {
        return retrieve(0L);
    }
    
    public synchronized WorkItem retrieve(long time) {
        log.log(Level.FINE, "retrieve size: {0}", pending.size());
        WorkItem req = null;
        String id = pending.pollFirst();
        if (id == null) {
            try {
                this.wait(time);
            } catch (InterruptedException e) {
            }
            id = pending.pollFirst();
        }
        if (id != null) {
            try (Connection conn = this.ds.getConnection()) {
                req = WorkItem.find(ctx, conn, id, WorkItemStatus.PENDING);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
        log.log(Level.FINE, "Status: {0}", this);
        return req;
    }
    
    public synchronized void finish(WorkItem item, ResponseBuilder res) {
        try (Connection conn = this.ds.getConnection()) {
            item.finish(ctx, conn, res);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public synchronized WorkItem status(String id) {
        try (Connection conn = this.ds.getConnection()) {
            return WorkItem.find(ctx, conn, id, null);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public synchronized WorkItem cancel(String id) {
        WorkItem item = this.status(id);
        if (item != null && item.isPending() && pending.remove(id)) {
            log.log(Level.FINE, "Status: {0}", this);
            try (Connection conn = this.ds.getConnection()) {
                item.cancel(conn);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
            return item;
        }
        return null;
    }
    
    
    @Override
    public String toString() {
        return new StringBuilder().append(pending.size()).append(" in pending").toString();
    }
    
}
