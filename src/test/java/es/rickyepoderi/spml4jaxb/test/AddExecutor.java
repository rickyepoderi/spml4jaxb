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

import es.rickyepoderi.spml4jaxb.accessor.AddRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.AddResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.user.AlreadyExistsException;
import es.rickyepoderi.spml4jaxb.user.InvalidUserException;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ricky
 */
public class AddExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    
    public AddExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }
    
    static public void setAttr(User u, String name, String[] values) throws ManagerException {
        switch (name) {
            case "uid":
                if (values.length > 1) {
                    throw new InvalidUserException("username is singlevalue");
                } else if (values.length == 1) {
                    u.setUid(values[0]);
                } else {
                    u.setUid(null);
                }
                break;
            case "password":
                if (values.length > 1) {
                    throw new InvalidUserException("password is singlevalue");
                } else if (values.length == 1) {
                    u.setPassword(values[0]);
                } else {
                    u.setPassword(null);
                }
                break;
            case "cn":
                if (values.length > 1) {
                    throw new InvalidUserException("cn is singlevalue");
                } else if (values.length == 1) {
                    u.setCn(values[0]);
                } else {
                    u.setCn(null);
                }
                break;
            case "description":
                if (values.length > 1) {
                    throw new InvalidUserException("description is singlevalue");
                } else if (values.length == 1) {
                    u.setDescription(values[0]);
                } else {
                    u.setDescription(null);
                }
                break;
            case "role":
                u.getRole().clear();
                u.getRole().addAll(Arrays.asList(values));
                break;
        }
    }
    
    static public Set<String> getAttr(User u, String name) {
        Set<String> res = new HashSet<>();
        switch (name) {
            case "uid":
                if (u.getUid()!= null) {
                    res.add(u.getUid());
                }
                break;
            case "password":
                if (u.getPassword()!= null) {
                    res.add(u.getPassword());
                }
                break;
            case "cn":
                if (u.getCn()!= null) {
                    res.add(u.getCn());
                }
                break;
            case "description":
                if (u.getDescription()!= null) {
                    res.add(u.getDescription());
                }
                break;
            case "role":
                res.addAll(u.getRole());
                break;
        }
        return res;
    }
    
    static public void modifyAttr(User u, String name, ModificationModeType mode, List<String> values) throws ManagerException {
        Set<String> current = getAttr(u, name);
        Set<String> news = new HashSet<>(values.size());
        news.addAll(values);
        if (ModificationModeType.ADD.equals(mode)) {
            current.addAll(news);
        } else if (ModificationModeType.DELETE.equals(mode)) {
            current.removeAll(news);
        } else if (ModificationModeType.REPLACE.equals(mode)) {
            current = news;
        }
        setAttr(u, name, current.toArray(new String[0]));
    }
    
    static public User spml2User(User u, DsmlAttr[] attrs) throws ManagerException {
        for (DsmlAttr a : attrs) {
            modifyAttr(u, a.getName(), ModificationModeType.REPLACE, a.getValue());
        }
        return u;
    }
    
    static public DsmlAttr createDsmlValue(String name, String... values) {
        DsmlAttr attr = new DsmlAttr();
        attr.setName(name);
        attr.getValue().addAll(Arrays.asList(values));
        return attr;
    }
    
    static public DsmlAttr[] user2Dsml(User u) {
        Map<String, DsmlAttr> attrs = user2DsmlMap(u);
        return attrs.values().toArray(new DsmlAttr[0]);
    }
    
    static public Map<String, DsmlAttr> user2DsmlMap(User u) {
        Map<String, DsmlAttr> attrs = new HashMap<>();
        if (u.getUid() != null) {
            DsmlAttr a = createDsmlValue("uid", u.getUid());
            attrs.put(a.getName(), a);
        }
        if (u.getPassword() != null) {
            DsmlAttr a = createDsmlValue("password", u.getPassword());
            attrs.put(a.getName(), a);
        }
        if (u.getCn() != null) {
            DsmlAttr a = createDsmlValue("cn", u.getCn());
            attrs.put(a.getName(), a);
        }
        if (u.getDescription() != null) {
            DsmlAttr a = createDsmlValue("description", u.getDescription());
            attrs.put(a.getName(), a);
        }
        if (!u.getRole().isEmpty()) {
            DsmlAttr a = createDsmlValue("role", u.getRole().toArray(new String[0]));
            attrs.put(a.getName(), a);
        }
        return attrs;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asAdd();
    }
    
    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        AddResponseBuilder builder = ResponseBuilder.builderForAdd().requestId(item.getId()).success();
        AddRequestAccessor req = item.getRequestAccessor().asAdd();
        try {
            User u = new User();
            if (req.isTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                // dsml
                spml2User(u, req.getDsmlAttributes());
                um.create(u);
                builder.psoId(u.getUid()).psoTargetId(req.getTargetId());
                if (req.isReturnData() || req.isReturnEverything()) {
                    builder.dsmlAttribute(user2Dsml(u));
                }
            } else if (req.isTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                // xsd
                u = (User) req.getXsdObject(User.class);
                um.create(u);
                builder.psoId(u.getUid()).psoTargetId(req.getTargetId());
                if (req.isReturnData() || req.isReturnEverything()) {
                    builder.xsdObject(u);
                }
            } else {
                builder.failure()
                        .unsupportedIdentifierType()
                        .errorMessage("Invalid targetID!");
            }
        } catch (InvalidUserException e) {
            builder.failure()
                    .malformedRequest()
                    .errorMessage(e.getMessage());
        } catch (AlreadyExistsException e) {
            builder.failure()
                    .alreadyExists()
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
