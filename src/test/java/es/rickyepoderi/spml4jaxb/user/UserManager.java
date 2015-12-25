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
        try (Connection conn = dataSource.getConnection()) {
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
                            + "FOREIGN KEY (USERNAME) REFERENCES USER(USERNAME))")) {
                pstmt1.executeUpdate();
                pstmt2.executeUpdate();
                conn.commit();
            }
        }
        this.ds = dataSource;
        dataTypeFactory = DatatypeFactory.newInstance();
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

    public User update(User u) throws ManagerException {
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
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    u = new User();
                    u.setUid(uid);
                    u.setPassword(rs.getString(1));
                    u.setCn(rs.getString(2));
                    u.setDescription(rs.getString(3));
                    u.setExpired(rs.getInt(4));
                    GregorianCalendar c = new GregorianCalendar();
                    if (rs.getTimestamp(5) != null) {
                        c.setTime(rs.getTimestamp(5));
                        u.setEnabled(dataTypeFactory.newXMLGregorianCalendar(c));
                    }
                    if (rs.getDate(6) != null) {
                        c.setTime(rs.getTimestamp(6));
                        u.setDisabled(dataTypeFactory.newXMLGregorianCalendar(c));
                    }
                    rs.close();
                    // read the roles
                    ps2.setString(1, uid);
                    rs = ps2.executeQuery();
                    while (rs.next()) {
                        u.getRole().add(rs.getString(1));
                    }
                } else {
                    throw new UserNotFoundException();
                }
                conn.commit();
                return u;
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
        update(u);
    }

    public void expirePassword(String uid, int remaining) throws ManagerException {
        if (remaining < 0) {
            throw new ManagerException("The remaining number should be greater than zero.");
        }
        User u = read(uid);
        u.setExpired(remaining);
        update(u);
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
        update(u);
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
        update(u);
    }

    public void enable(String uid, Date date) throws ManagerException {
        User u = read(uid);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        u.setEnabled(dataTypeFactory.newXMLGregorianCalendar(c));
        if (date.compareTo(new Date()) <= 0) {
            u.setDisabled(null);
        }
        update(u);
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
                ResultSet rs = ps.executeQuery();
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
            } catch (Exception e) {
                conn.rollback();
                throw new ManagerException(e);
            }
        } catch (SQLException e) {
            throw new ManagerException(e);
        }
        return res;
    }
}
