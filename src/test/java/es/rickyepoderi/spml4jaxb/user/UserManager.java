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
package es.rickyepoderi.spml4jaxb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.SQLIntegrityConstraintViolationException;
import org.hsqldb.jdbc.JDBCDataSource;

/**
 *
 * @author ricky
 */
public class UserManager {

    protected DatatypeFactory dataTypeFactory;
    private DataSource ds = null;

    public UserManager(DataSource ds) throws DatatypeConfigurationException {
        this.ds = ds;
        dataTypeFactory = DatatypeFactory.newInstance();
    }
    
    public UserManager() throws SQLException, DatatypeConfigurationException {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setDatabase("jdbc:hsqldb:mem:mydb");
        dataSource.setUser("SA");
        dataSource.setPassword("");
        this.ds = dataSource;
        dataTypeFactory = DatatypeFactory.newInstance();
        this.initialize();
    }
    
    public enum RoleOperation { ADD, REPLACE, DELETE, NONE }; 
    
    public enum AuditTypeOperation { CREATE, UPDATE, DELETE, SET_PASSWORD, EXPIRE_PASSWORD, 
            RESET_PASSWORD, DISABLE, ENABLE, BULK_MODIFY, BULK_DELETE};
    
    public class AuditEntry {

        private String uid;
        private Date time;
        private AuditTypeOperation type;

        public AuditEntry() {
            // noop
        }
        
        public AuditEntry(String uid) {
            this.uid = uid;
        }
        
        public AuditEntry(String uid, Date time, AuditTypeOperation type) {
            this.uid = uid;
            this.time = time;
            this.type = type;
        }
        
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public AuditTypeOperation getType() {
            return type;
        }

        public void setType(AuditTypeOperation type) {
            this.type = type;
        }
        
    }
    
    final public void initialize() throws SQLException {
        try (Connection conn = this.ds.getConnection()) {
            try (PreparedStatement pstmt1 = conn.prepareStatement("CREATE TABLE USER("
                    + "USERNAME VARCHAR(50) NOT NULL PRIMARY KEY,"
                    + "PASSWORD VARCHAR(50) NOT NULL,"
                    + "COMMON_NAME VARCHAR(256) NOT NULL,"
                    + "DESCRIPTION VARCHAR(1024),"
                    + "EXPIRED INTEGER DEFAULT -1,"
                    + "ENABLED TIMESTAMP,"
                    + "DISABLED TIMESTAMP)");
                    PreparedStatement pstmt2 = conn.prepareStatement("CREATE TABLE USER_ROLES ("
                            + "USERNAME VARCHAR(50) NOT NULL,"
                            + "ROLENAME VARCHAR(50) NOT NULL,"
                            + "PRIMARY KEY (USERNAME, ROLENAME),"
                            + "FOREIGN KEY (USERNAME) REFERENCES USER(USERNAME) ON DELETE CASCADE)");
                    PreparedStatement pstmt3 = conn.prepareStatement("CREATE TABLE AUDIT("
                            + "USERNAME VARCHAR(50) NOT NULL,"
                            + "UPDATE_TIME TIMESTAMP DEFAULT NOW,"
                            + "UPDATE_TYPE VARCHAR(50) NOT NULL,"
                            + "PRIMARY KEY (USERNAME, UPDATE_TIME, UPDATE_TYPE),"
                            + "CHECK (UPDATE_TYPE IN ('CREATE','UPDATE','DELETE','SET_PASSWORD','EXPIRE_PASSWORD',"
                            + "  'RESET_PASSWORD','DISABLE','ENABLE','BULK_MODIFY','BULK_DELETE')))")) {
                pstmt1.executeUpdate();
                pstmt2.executeUpdate();
                pstmt3.executeUpdate();
                conn.commit();
            }
        }
    }

    private void checkUser(User u) throws InvalidUserException {
        if (u.getUid() == null) {
            throw new InvalidUserException("Uid is compulsory!");
        }
        if (u.getCn() == null) {
            throw new InvalidUserException("Cn is compulsory!");
        }
        if (u.getPassword() == null) {
            throw new InvalidUserException("Password is compulsory!");
        }
    }

    public User create(User u) throws ManagerException {
        checkUser(u);
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO USER(USERNAME,PASSWORD,COMMON_NAME,DESCRIPTION,EXPIRED,ENABLED,DISABLED) VALUES(?,?,?,?,?,?,?)");
                    PreparedStatement ps2 = conn.prepareStatement(
                            "INSERT INTO USER_ROLES(USERNAME,ROLENAME) VALUES(?,?)")) {
                conn.setAutoCommit(false);
                ps.setString(1, u.getUid());
                ps.setString(2, u.getPassword());
                ps.setString(3, u.getCn());
                ps.setString(4, u.getDescription());
                ps.setInt(5, u.getExpired());
                ps.setTimestamp(6, (u.getEnabled() == null) ? null : 
                        new java.sql.Timestamp(u.getEnabled().toGregorianCalendar().getTime().getTime()));
                ps.setTimestamp(7, (u.getDisabled() == null) ? null : 
                        new java.sql.Timestamp(u.getDisabled().toGregorianCalendar().getTime().getTime()));
                ps.executeUpdate();
                for (String role : u.getRole()) {
                    ps2.setString(1, u.getUid());
                    ps2.setString(2, role);
                    ps2.executeUpdate();
                    ps2.clearParameters();
                }
                this.auditSingle(conn, u.getUid(), AuditTypeOperation.CREATE);
                conn.commit();
                return u;
            } catch(SQLIntegrityConstraintViolationException e) {
                conn.rollback();
                throw new AlreadyExistsException(e);
            } catch (SQLException e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
    }
    
    public User update(User u)  throws ManagerException {
        return updateInternal(u, AuditTypeOperation.UPDATE);
    }

    private User updateInternal(User u, UserManager.AuditTypeOperation type) throws ManagerException {
        checkUser(u);
        StringBuilder update = new StringBuilder("UPDATE USER SET ");
        if (u.getPassword() != null) {
            // the password is changed
            update.append("PASSWORD = ?, ");
        }
        update.append("COMMON_NAME = ?, ")
                .append("DESCRIPTION = ?, ")
                .append("EXPIRED = ?, ")
                .append("ENABLED = ?, ")
                .append("DISABLED = ? ")
                .append("WHERE USERNAME = ?");
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(update.toString());
                    PreparedStatement ps2 = conn.prepareStatement(
                            "DELETE FROM USER_ROLES WHERE USERNAME = ?");
                    PreparedStatement ps3 = conn.prepareStatement(
                            "INSERT INTO USER_ROLES(USERNAME,ROLENAME) VALUES(?,?)")) {
                conn.setAutoCommit(false);
                int i = 1;
                // update user
                if (u.getPassword() != null) {
                    ps.setString(i++, u.getPassword());
                }
                ps.setString(i++, u.getCn());
                ps.setString(i++, u.getDescription());
                ps.setInt(i++, u.getExpired());
                ps.setTimestamp(i++, (u.getEnabled() == null) ? null : 
                        new java.sql.Timestamp(u.getEnabled().toGregorianCalendar().getTime().getTime()));
                ps.setTimestamp(i++, (u.getDisabled()== null) ? null : 
                        new java.sql.Timestamp(u.getDisabled().toGregorianCalendar().getTime().getTime()));
                ps.setString(i++, u.getUid());
                int n = ps.executeUpdate();
                if (n == 1) {
                    // delete all roles
                    ps2.setString(1, u.getUid());
                    ps2.executeUpdate();
                    // add all new roles
                    for (String role : u.getRole()) {
                        ps3.setString(1, u.getUid());
                        ps3.setString(2, role);
                        ps3.executeUpdate();
                        ps3.clearParameters();
                    }
                    this.auditSingle(conn, u.getUid(), type);
                    conn.commit();
                    return u;
                } else {
                    conn.rollback();
                    throw new UserNotFoundException();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
    }

    public void delete(String uid) throws ManagerException {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM USER WHERE USERNAME = ?");
                    PreparedStatement ps2 = conn.prepareStatement("DELETE FROM USER_ROLES WHERE USERNAME = ?")) {
                conn.setAutoCommit(false);
                ps2.setString(1, uid);
                ps2.executeUpdate();
                ps.setString(1, uid);
                int n = ps.executeUpdate();
                if (n != 1) {
                    throw new UserNotFoundException();
                }
                this.auditSingle(conn, uid, AuditTypeOperation.DELETE);
                conn.commit();
            } catch(SQLException e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
    }

    public User read(String uid) throws ManagerException {
        User u = null;
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT PASSWORD,COMMON_NAME,DESCRIPTION,EXPIRED,ENABLED,DISABLED FROM USER WHERE USERNAME = ?");
                    PreparedStatement ps2 = conn.prepareStatement(
                            "SELECT ROLENAME FROM USER_ROLES WHERE USERNAME = ?")) {
                conn.setAutoCommit(false);
                ps.setString(1, uid);
                try (ResultSet rs1 = ps.executeQuery()) {
                    if (rs1.next()) {
                        u = new User();
                        u.setUid(uid);
                        u.setPassword(rs1.getString(1));
                        u.setCn(rs1.getString(2));
                        u.setDescription(rs1.getString(3));
                        u.setExpired(rs1.getInt(4));
                        GregorianCalendar c = new GregorianCalendar();
                        if (rs1.getTimestamp(5) != null) {
                            c.setTime(rs1.getTimestamp(5));
                            u.setEnabled(dataTypeFactory.newXMLGregorianCalendar(c));
                        }
                        if (rs1.getDate(6) != null) {
                            c.setTime(rs1.getTimestamp(6));
                            u.setDisabled(dataTypeFactory.newXMLGregorianCalendar(c));
                        }
                        rs1.close();
                        // read the roles
                        ps2.setString(1, uid);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while (rs2.next()) {
                                u.getRole().add(rs2.getString(1));
                            }
                        }
                    } else {
                        throw new UserNotFoundException();
                    }
                    conn.commit();
                    return u;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
    }

    public void setPassword(String uid, String password) throws ManagerException {
        User u = read(uid);
        u.setPassword(password);
        u.setExpired(-1);
        updateInternal(u, AuditTypeOperation.SET_PASSWORD);
    }

    public void expirePassword(String uid, int remaining) throws ManagerException {
        if (remaining < 0) {
            throw new ManagerException("The remaining number should be greater than zero.");
        }
        User u = read(uid);
        u.setExpired(remaining);
        updateInternal(u, AuditTypeOperation.EXPIRE_PASSWORD);
    }

    static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,;.:-_?!@#%&/()=";
    static final Random rnd = new Random();

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public String resetPassword(String uid) throws ManagerException {
        User u = read(uid);
        u.setPassword(randomString(8));
        u.setExpired(-1);
        updateInternal(u, AuditTypeOperation.RESET_PASSWORD);
        return u.getPassword();
    }

    public boolean validatePassword(String uid, String password) throws ManagerException {
        User u = read(uid);
        boolean valid = !(u.getExpired() == 0) && password.equals(u.getPassword());
        if (u.getExpired() > 0) {
            u.setExpired(u.getExpired() - 1);
            update(u);
        }
        return valid;
    }

    public void disable(String uid, Date date) throws ManagerException {
        User u = read(uid);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        u.setDisabled(dataTypeFactory.newXMLGregorianCalendar(c));
        if (date.compareTo(new Date()) <= 0) {
            u.setEnabled(null);
        }
        updateInternal(u, AuditTypeOperation.DISABLE);
    }

    public void enable(String uid, Date date) throws ManagerException {
        User u = read(uid);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        u.setEnabled(dataTypeFactory.newXMLGregorianCalendar(c));
        if (date.compareTo(new Date()) <= 0) {
            u.setDisabled(null);
        }
        updateInternal(u, AuditTypeOperation.ENABLE);
    }

    public boolean isDisabled(String uid) throws ManagerException {
        User u = read(uid);
        Date now = new Date();
        return u.getDisabled() != null && u.getDisabled().toGregorianCalendar().getTime().compareTo(now) <= 0
                && (u.getEnabled() == null
                || u.getDisabled().toGregorianCalendar().getTime().compareTo(u.getEnabled().toGregorianCalendar().getTime()) > 0
                || u.getEnabled().toGregorianCalendar().getTime().compareTo(now) > 0);
    }

    public List<User> list(String customFilter) throws ManagerException {
        List<User> res = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            StringBuilder sb = new StringBuilder()
                    .append("SELECT u.USERNAME,PASSWORD,COMMON_NAME,DESCRIPTION,EXPIRED,ENABLED,DISABLED,ROLENAME ")
                    .append("FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME");
            if (customFilter != null && !customFilter.isEmpty()) {
                sb.append(" WHERE u.USERNAME IN (")
                        .append("SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                        .append(customFilter).append(")");
            }
            System.err.println(sb.toString());
            try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
                conn.setAutoCommit(false);
                try (ResultSet rs = ps.executeQuery()) {
                    boolean cont = rs.next();
                    while (cont) {
                        User u = new User();
                        u.setUid(rs.getString(1));
                        u.setPassword(rs.getString(2));
                        u.setCn(rs.getString(3));
                        u.setDescription(rs.getString(4));
                        u.setExpired(rs.getInt(5));
                        GregorianCalendar c = new GregorianCalendar();
                        if (rs.getTimestamp(6) != null) {
                            c.setTime(rs.getTimestamp(6));
                            u.setEnabled(dataTypeFactory.newXMLGregorianCalendar(c));
                        }
                        if (rs.getTimestamp(7) != null) {
                            c.setTime(rs.getTimestamp(7));
                            u.setDisabled(dataTypeFactory.newXMLGregorianCalendar(c));
                        }
                        while (cont && u.getUid().equals(rs.getString(1))) {
                            if (rs.getString(8) != null) {
                                u.getRole().add(rs.getString(8));
                            }
                            cont = rs.next();
                        }
                        res.add(u);
                    }
                    conn.commit();
                }
            } catch (Exception e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
        return res;
    }
    
    public int bulkDelete(String customFilter) throws ManagerException {
        try (Connection conn = ds.getConnection()) {
            StringBuilder sb = new StringBuilder()
                    .append("DELETE FROM USER");
            if (customFilter != null && !customFilter.isEmpty()) {
                sb.append(" WHERE USERNAME IN (")
                        .append("SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                        .append(customFilter).append(")");
            }
            System.err.println(sb.toString());
            try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
                conn.setAutoCommit(false);
                this.auditMultiple(conn, customFilter, AuditTypeOperation.BULK_DELETE);
                int rows = ps.executeUpdate();
                conn.commit();
                return rows;
            } catch (Exception e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        }  catch (SQLException e) {
            throw new ManagerException(e);
        }
    }
    
    public void bulkModify(String customFilter, 
            boolean useCommonName, String commonName, 
            boolean useDescription, String description, 
            boolean usePassword, String password,
            RoleOperation op, Iterable<String> roles) throws ManagerException {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try {
                this.auditMultiple(conn, customFilter, AuditTypeOperation.BULK_MODIFY);
                // first do the role modifications
                switch (op) {
                    case ADD:
                        this.bulkAddRoles(conn, customFilter, roles);
                        break;
                    case DELETE:
                        this.bulkDeleteRoles(conn, customFilter, roles);
                        break;
                    case REPLACE:
                        this.bulkDeleteAllRoles(conn, customFilter);
                        this.bulkAddRoles(conn, customFilter, roles);
                        break;
                }
                // the modify the user attributes if any
                if (useCommonName || useDescription || usePassword) {
                    this.bulkModify(conn, customFilter, useCommonName, commonName,
                            useDescription, description, usePassword, password);
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
    }
    
    private int bulkModify(Connection conn, String customFilter,
            boolean useCommonName, String commonName,
            boolean useDescription, String description,
            boolean usePassword, String password) throws SQLException {
        StringBuilder sb = new StringBuilder().append("UPDATE USER SET");
        boolean first = true;
        if (useCommonName) {
            sb.append(" COMMON_NAME = ?");
            first = false;
        }
        if (useDescription) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(" DESCRIPTION = ?");
            first = false;
        }
        if (usePassword) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(" PASSWORD = ?");
        }
        if (customFilter != null && !customFilter.isEmpty()) {
            sb.append(" WHERE USERNAME IN (")
                    .append("SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                    .append(customFilter).append(")");
        }
        System.err.println(sb.toString());
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            int i = 1;
            if (useCommonName) {
                ps.setString(i, commonName);
                System.err.println("commonName=" + commonName);
                i++;
            }
            if (useDescription) {
                ps.setString(i, description);
                System.err.println("description=" + commonName);
                i++;
            }
            if (usePassword) {
                ps.setString(i, password);
                System.err.println("password=" + commonName);
            }
            int rows = ps.executeUpdate();
            return rows;
        }
    }
    
    private void bulkDeleteAllRoles(Connection conn, String customFilter) throws SQLException {
        StringBuilder sb = new StringBuilder()
                .append("DELETE FROM USER_ROLES");
        if (customFilter != null && !customFilter.isEmpty()) {
            sb.append(" WHERE USERNAME IN (SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                    .append(customFilter).append(")");
        }
        System.err.println(sb.toString());
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            ps.executeUpdate();
        }
    }
    
    private void bulkDeleteRoles(Connection conn, String customFilter, Iterable<String> roles) throws SQLException {
        StringBuilder sb = new StringBuilder()
                .append("DELETE FROM USER_ROLES WHERE");
        if (customFilter != null && !customFilter.isEmpty()) {
            sb.append(" USERNAME IN (SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                    .append(customFilter).append(") AND");
        }
        sb.append(" ROLENAME = ?");
        System.err.println(sb.toString());
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (String role : roles) {
                ps.setString(1, role);
                ps.executeUpdate();
            }
        }
    }
    
    private void bulkAddRoles(Connection conn, String customFilter, Iterable<String> roles) throws SQLException {
        StringBuilder sb = new StringBuilder()
                .append("INSERT INTO USER_ROLES(USERNAME,ROLENAME) SELECT USERNAME, ? FROM USER WHERE");
        if (customFilter != null && !customFilter.isEmpty()) {
            sb.append(" USERNAME IN (SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                    .append(customFilter).append(") AND");
        }
        sb.append(" USERNAME NOT IN (SELECT USERNAME FROM USER_ROLES WHERE ROLENAME = ?)");
        System.err.println(sb.toString());
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (String role : roles) {
                ps.setString(1, role);
                ps.setString(2, role);
                ps.executeUpdate();
            }
        }
    }
    
    private void auditSingle(Connection conn, String uid, UserManager.AuditTypeOperation type) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO AUDIT(USERNAME, UPDATE_TYPE) VALUES (?, ?)")) {
            ps.setString(1, uid);
            ps.setString(2, type.toString());
            ps.executeUpdate();
        }
    }
    
    private void auditMultiple(Connection conn, String customFilter, UserManager.AuditTypeOperation type) throws SQLException {
        StringBuilder sb = new StringBuilder()
                .append("INSERT INTO AUDIT(USERNAME, UPDATE_TYPE) SELECT USERNAME, ? FROM USER");
        if (customFilter != null && !customFilter.isEmpty()) {
            sb.append(" WHERE USERNAME IN (SELECT u.USERNAME FROM USER u LEFT OUTER JOIN USER_ROLES r ON u.USERNAME = r.USERNAME WHERE ")
                    .append(customFilter).append(")");
        }
        System.err.println(sb.toString());
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            ps.setString(1, type.toString());
            ps.executeUpdate();
        }
    }
    
    public List<AuditEntry> searchAudit(Date since, Iterable<AuditTypeOperation> types) throws ManagerException {
        List<AuditEntry> res = new ArrayList<>();
        System.err.println(since);
        System.err.println(types);
        try (Connection conn = ds.getConnection()) {
            StringBuilder sb = new StringBuilder()
                    .append("SELECT USERNAME, UPDATE_TIME, UPDATE_TYPE FROM AUDIT");
            boolean first = true;
            if (since != null) {
                sb.append(" "). append(first? "WHERE":"AND").append(" UPDATE_TIME >= ?");
                first = false;
            }
            if (types != null) {
                sb.append(" "). append(first? "WHERE":"AND").append(" UPDATE_TYPE IN (");
                for (AuditTypeOperation type: types) {
                    sb.append("?,");
                }
                sb.setLength(sb.length() - 1);
                sb.append(")");
                first = false;
            }
            sb.append(" ORDER BY UPDATE_TIME");
            System.err.println(sb.toString());
            try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
                conn.setAutoCommit(false);
                int i = 1;
                if (since != null) {
                    ps.setTimestamp(1, new java.sql.Timestamp(since.getTime()));
                    i++;
                }
                if (types != null) {
                    for (AuditTypeOperation type : types) {
                        ps.setString(i, type.toString());
                        i++;
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AuditEntry e = new AuditEntry();
                        e.setUid(rs.getString("USERNAME"));
                        e.setTime(new Date(rs.getTimestamp("UPDATE_TIME").getTime()));
                        e.setType(AuditTypeOperation.valueOf(rs.getString("UPDATE_TYPE")));
                        res.add(e);
                    }
                    conn.commit();
                    return res;
                }
            } catch (Exception e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        }  catch (SQLException e) {
            throw new ManagerException(e);
        }
    }
}
