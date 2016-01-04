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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class UserManagerTest {
    
    private UserManager um;
    
    public UserManagerTest() throws SQLException, DatatypeConfigurationException {
        um = new UserManager();
    }
    
    public User createUser() {
        User u = new User();
        u.setUid("uid");
        u.setPassword("password");
        u.setCn("Ricardo Martin");
        u.setDescription("me");
        return u;
    }
    
    @Test
    public void testCRUD() throws ManagerException {
        Date start = new Date();
        User u = createUser();
        um.create(u);
        Assert.assertEquals(u, um.read(u.getUid()));
        u.getRole().add("User");
        u.getRole().add("Admin");
        u.setPassword("password123");
        u.setCn("Ricardo Martin Camarero");
        um.update(u);
        Assert.assertEquals(u, um.read(u.getUid()));
        um.delete(u.getUid());
        List <UserManager.AuditEntry> audit = um.searchAudit(start, null);
        Assert.assertEquals(audit.size(), 3);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.CREATE);
        Assert.assertEquals(audit.get(0).getUid(), u.getUid());
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.UPDATE);
        Assert.assertEquals(audit.get(1).getUid(), u.getUid());
        Assert.assertEquals(audit.get(2).getType(), UserManager.AuditTypeOperation.DELETE);
        Assert.assertEquals(audit.get(2).getUid(), u.getUid());
    }
    
    @Test
    public void testAlreadyExistsInCreation() throws ManagerException {
        User u = createUser();
        um.create(u);
        Assert.assertEquals(u, um.read(u.getUid()));
        boolean exception = false;
        try {
            um.create(u);
        } catch (AlreadyExistsException e) {
            exception = true;
        } 
        Assert.assertTrue(exception);
        um.delete(u.getUid());
    }
    
    @Test
    public void testNotFoundInModify() throws ManagerException {
        User u = createUser();
        boolean exception = false;
        try {
            um.update(u);
        } catch (UserNotFoundException e) {
            exception = true;
        } 
        Assert.assertTrue(exception);
    }
    
    @Test
    public void testPassword() throws ManagerException {
        Date start = new Date();
        User u = createUser();
        um.create(u);
        Assert.assertEquals(u, um.read(u.getUid()));
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        // set password
        String previous = u.getPassword();
        u.setPassword("password123");
        um.setPassword(u.getUid(), u.getPassword());
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        Assert.assertFalse(um.validatePassword(u.getUid(), previous));
        // reset password
        previous = u.getPassword();
        u.setPassword(um.resetPassword(u.getUid()));
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        Assert.assertFalse(um.validatePassword(u.getUid(), previous));
        // expire the user now
        um.expirePassword(u.getUid(), 0);
        Assert.assertFalse(um.validatePassword(u.getUid(), u.getPassword()));
        // set again the password
        u.setPassword("password321");
        um.setPassword(u.getUid(), u.getPassword());
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        // reset in three
        um.expirePassword(u.getUid(), 2);
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        Assert.assertFalse(um.validatePassword(u.getUid(), u.getPassword()));
        // delete
        um.delete(u.getUid());
        List <UserManager.AuditEntry> audit = um.searchAudit(start, 
                Arrays.asList(new UserManager.AuditTypeOperation[] {
                    UserManager.AuditTypeOperation.EXPIRE_PASSWORD,
                    UserManager.AuditTypeOperation.SET_PASSWORD,
                    UserManager.AuditTypeOperation.RESET_PASSWORD}));
        Assert.assertEquals(audit.size(), 5);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.SET_PASSWORD);
        Assert.assertEquals(audit.get(0).getUid(), u.getUid());
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.RESET_PASSWORD);
        Assert.assertEquals(audit.get(1).getUid(), u.getUid());
        Assert.assertEquals(audit.get(2).getType(), UserManager.AuditTypeOperation.EXPIRE_PASSWORD);
        Assert.assertEquals(audit.get(2).getUid(), u.getUid());
        Assert.assertEquals(audit.get(3).getType(), UserManager.AuditTypeOperation.SET_PASSWORD);
        Assert.assertEquals(audit.get(3).getUid(), u.getUid());
        Assert.assertEquals(audit.get(4).getType(), UserManager.AuditTypeOperation.EXPIRE_PASSWORD);
        Assert.assertEquals(audit.get(4).getUid(), u.getUid());
    }
    
    @Test
    public void testDisable() throws ManagerException {
        Date start = new Date();
        User u = createUser();
        um.create(u);
        Assert.assertEquals(u, um.read(u.getUid()));
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        // disable now
        um.disable(u.getUid(), new Date());
        Assert.assertTrue(um.isDisabled(u.getUid()));
        // enable now
        um.enable(u.getUid(), new Date());
        Assert.assertFalse(um.isDisabled(u.getUid()));
        // delete
        um.delete(u.getUid());
        List <UserManager.AuditEntry> audit = um.searchAudit(start, 
                Arrays.asList(new UserManager.AuditTypeOperation[] {
                    UserManager.AuditTypeOperation.DISABLE,
                    UserManager.AuditTypeOperation.ENABLE}));
        Assert.assertEquals(audit.size(), 2);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.DISABLE);
        Assert.assertEquals(audit.get(0).getUid(), u.getUid());
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.ENABLE);
        Assert.assertEquals(audit.get(1).getUid(), u.getUid());
    }
    
    @Test
    public void testDisableDates() throws ManagerException {
        User u = createUser();
        um.create(u);
        Assert.assertEquals(u, um.read(u.getUid()));
        Assert.assertTrue(um.validatePassword(u.getUid(), u.getPassword()));
        // disable tomorrow
        um.disable(u.getUid(), new Date(System.currentTimeMillis() + 86400000L));
        Assert.assertFalse(um.isDisabled(u.getUid()));
        // disable yesterday
        um.disable(u.getUid(), new Date(System.currentTimeMillis() - 86400000L));
        Assert.assertTrue(um.isDisabled(u.getUid()));
        // enable tomorrow
        um.enable(u.getUid(), new Date(System.currentTimeMillis() + 86400000L));
        Assert.assertTrue(um.isDisabled(u.getUid()));
        // enable half day ago
        um.enable(u.getUid(), new Date(System.currentTimeMillis() - 43200000L));
        Assert.assertFalse(um.isDisabled(u.getUid()));
        // delete
        um.delete(u.getUid());
    }
    
    @Test
    public void testListAll() throws ManagerException {
        Map<String,User> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            User u = createUser();
            u.setUid(u.getUid() + Integer.toString(i));
            map.put(u.getUid(), u);
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        map.get("uid1").getRole().add("Admin");
        map.get("uid1").getRole().add("User");
        map.get("uid1").setDisabled(um.dataTypeFactory.newXMLGregorianCalendar(c));
        map.get("uid2").setDescription(null);
        map.get("uid2").setCn("Another user");
        map.get("uid3").getRole().add("Other");
        for (User u: map.values()) {
            um.create(u);
        }
        // list
        List<User> list = um.list(null);
        Assert.assertEquals(3, list.size());
        for (User u: list) {
            Assert.assertEquals(u, map.get(u.getUid()));
        }
        // delete
        for (User u: map.values()) {
            um.delete(u.getUid());
        }
    }
    
    @Test
    public void testListAllWithFilter() throws ManagerException {
        Map<String,User> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            User u = createUser();
            u.setUid(u.getUid() + Integer.toString(i));
            map.put(u.getUid(), u);
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        map.get("uid1").getRole().add("Admin");
        map.get("uid1").getRole().add("User");
        map.get("uid1").setDisabled(um.dataTypeFactory.newXMLGregorianCalendar(c));
        map.get("uid2").setDescription(null);
        map.get("uid2").setCn("Another user");
        map.get("uid3").getRole().add("User");
        for (User u: map.values()) {
            um.create(u);
        }
        // list
        List<User> list = um.list("ROLENAME='User' AND DESCRIPTION='me'");
        Assert.assertEquals(2, list.size());
        for (User u: list) {
            Assert.assertEquals(u, map.get(u.getUid()));
        }
        // delete
        for (User u: map.values()) {
            um.delete(u.getUid());
        }
    }
    
    @Test
    public void testBulkDelete() throws ManagerException {
        Date start = new Date();
        Map<String,User> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            User u = createUser();
            u.setUid(u.getUid() + Integer.toString(i));
            map.put(u.getUid(), u);
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        map.get("uid1").getRole().add("Admin");
        map.get("uid1").getRole().add("User");
        map.get("uid1").setDisabled(um.dataTypeFactory.newXMLGregorianCalendar(c));
        map.get("uid2").setDescription(null);
        map.get("uid2").setCn("Another user");
        map.get("uid3").getRole().add("User");
        for (User u: map.values()) {
            um.create(u);
        }
        // delete the filter
        int rows = um.bulkDelete("ROLENAME='User' AND DESCRIPTION='me'");
        Assert.assertEquals(rows, 2);
        // delete uid2
        um.delete("uid2");
        List <UserManager.AuditEntry> audit = um.searchAudit(start, 
                Arrays.asList(new UserManager.AuditTypeOperation[] {
                    UserManager.AuditTypeOperation.BULK_DELETE}));
        Assert.assertEquals(audit.size(), rows);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.BULK_DELETE);
        Assert.assertEquals(audit.get(0).getUid(), "uid1");
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.BULK_DELETE);
        Assert.assertEquals(audit.get(1).getUid(), "uid3");
    }
    
    @Test
    public void testBulkModify() throws ManagerException {
        Date start = new Date();
        Map<String,User> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            User u = createUser();
            u.setUid(u.getUid() + Integer.toString(i));
            map.put(u.getUid(), u);
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        map.get("uid1").getRole().add("Admin");
        map.get("uid1").getRole().add("User");
        map.get("uid1").setDisabled(um.dataTypeFactory.newXMLGregorianCalendar(c));
        map.get("uid2").setDescription(null);
        map.get("uid2").setCn("Another user");
        map.get("uid3").getRole().add("User");
        for (User u: map.values()) {
            um.create(u);
        }
        // modify the three elements using the filter
        um.bulkModify("ROLENAME='User' AND DESCRIPTION='me'",
                true, "Ricardo Martin Camarero",
                true, "new description",
                true, "password123",
                UserManager.RoleOperation.NONE, null);
        // check user1 and user3
        User u1 = um.read("uid1");
        Assert.assertNotNull(u1);
        Assert.assertEquals(u1.getCn(), "Ricardo Martin Camarero");
        Assert.assertEquals(u1.getDescription(), "new description");
        Assert.assertEquals(u1.getPassword(), "password123");
        u1 = um.read("uid3");
        Assert.assertNotNull(u1);
        Assert.assertEquals(u1.getCn(), "Ricardo Martin Camarero");
        Assert.assertEquals(u1.getDescription(), "new description");
        Assert.assertEquals(u1.getPassword(), "password123");
        // delete
        for (User u: map.values()) {
            um.delete(u.getUid());
        }
        List <UserManager.AuditEntry> audit = um.searchAudit(start, 
                Arrays.asList(new UserManager.AuditTypeOperation[] {
                    UserManager.AuditTypeOperation.BULK_MODIFY}));
        Assert.assertEquals(audit.size(), 2);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.BULK_MODIFY);
        Assert.assertEquals(audit.get(0).getUid(), "uid1");
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.BULK_MODIFY);
        Assert.assertEquals(audit.get(1).getUid(), "uid3");
    }
    
    @Test
    public void testBulkAddRoles() throws ManagerException {
        Date start = new Date();
        Map<String,User> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            User u = createUser();
            u.setUid(u.getUid() + Integer.toString(i));
            map.put(u.getUid(), u);
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        map.get("uid1").getRole().add("Admin");
        map.get("uid1").getRole().add("User");
        map.get("uid1").setDisabled(um.dataTypeFactory.newXMLGregorianCalendar(c));
        map.get("uid2").setDescription(null);
        map.get("uid2").setCn("Another user");
        map.get("uid3").getRole().add("User");
        for (User u: map.values()) {
            um.create(u);
        }
        // modify uid3 to have Admin
        um.bulkModify("DESCRIPTION='me'", 
                false, null, false, null, false, null, 
                UserManager.RoleOperation.ADD, Arrays.asList(new String[]{"Admin", "Other"}));
        // check user1 and user3
        User u1 = um.read("uid1");
        Assert.assertNotNull(u1);
        Assert.assertTrue(u1.getRole().contains("Admin"));
        Assert.assertTrue(u1.getRole().contains("Other"));
        u1 = um.read("uid3");
        Assert.assertNotNull(u1);
        Assert.assertTrue(u1.getRole().contains("Admin"));
        Assert.assertTrue(u1.getRole().contains("Other"));
        // delete
        for (User u: map.values()) {
            um.delete(u.getUid());
        }
        List <UserManager.AuditEntry> audit = um.searchAudit(start, 
                Arrays.asList(new UserManager.AuditTypeOperation[] {
                    UserManager.AuditTypeOperation.BULK_MODIFY}));
        Assert.assertEquals(audit.size(), 2);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.BULK_MODIFY);
        Assert.assertEquals(audit.get(0).getUid(), "uid1");
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.BULK_MODIFY);
        Assert.assertEquals(audit.get(1).getUid(), "uid3");
    }
    
    @Test
    public void testBulkDeleteRoles() throws ManagerException {
        Date start = new Date();
        Map<String,User> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            User u = createUser();
            u.setUid(u.getUid() + Integer.toString(i));
            map.put(u.getUid(), u);
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        map.get("uid1").getRole().add("Admin");
        map.get("uid1").getRole().add("User");
        map.get("uid1").setDisabled(um.dataTypeFactory.newXMLGregorianCalendar(c));
        map.get("uid2").setDescription(null);
        map.get("uid2").setCn("Another user");
        map.get("uid3").getRole().add("User");
        map.get("uid3").getRole().add("Other");
        for (User u: map.values()) {
            um.create(u);
        }
        // modify uid3 to have Admin
        um.bulkModify("DESCRIPTION='me'", 
                false, null, false, null, false, null, 
                UserManager.RoleOperation.DELETE, Arrays.asList(new String[]{"User", "Other"}));
        // check user1 and user3
        User u1 = um.read("uid1");
        Assert.assertNotNull(u1);
        Assert.assertFalse(u1.getRole().contains("User"));
        Assert.assertFalse(u1.getRole().contains("Other"));
        u1 = um.read("uid3");
        Assert.assertNotNull(u1);
        Assert.assertFalse(u1.getRole().contains("User"));
        Assert.assertFalse(u1.getRole().contains("Other"));
        // delete
        for (User u: map.values()) {
            um.delete(u.getUid());
        }
        List <UserManager.AuditEntry> audit = um.searchAudit(start, 
                Arrays.asList(new UserManager.AuditTypeOperation[] {
                    UserManager.AuditTypeOperation.BULK_MODIFY}));
        Assert.assertEquals(audit.size(), 2);
        Assert.assertEquals(audit.get(0).getType(), UserManager.AuditTypeOperation.BULK_MODIFY);
        Assert.assertEquals(audit.get(0).getUid(), "uid1");
        Assert.assertEquals(audit.get(1).getType(), UserManager.AuditTypeOperation.BULK_MODIFY);
        Assert.assertEquals(audit.get(1).getUid(), "uid3");
    }
    
}
