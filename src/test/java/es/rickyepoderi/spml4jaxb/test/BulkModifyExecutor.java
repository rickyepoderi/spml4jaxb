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

import es.rickyepoderi.spml4jaxb.accessor.BulkModifyRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.builder.BulkModifyResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import es.rickyepoderi.spml4jaxb.test.ModifyExecutor;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ricky
 */
public class BulkModifyExecutor extends AsyncSpmlBaseExecutor {
    
    private UserManager um;
    private JAXBContext ctx;

    public BulkModifyExecutor(UserManager um, WorkQueue queue, JAXBContext ctx) {
        super(queue);
        this.um = um;
        this.ctx = ctx;
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asBulkModify();
    }
    
    protected static DsmlModification findModification(String name, DsmlModification[] mods) {
        for (DsmlModification mod: mods) {
            if (name.equals(mod.getName())) {
                return mod;
            }
        }
        return null;
    }
    
    protected static String getModificationValue(DsmlModification mod) throws ManagerException {
        if (mod == null) {
            return null;
        }
        if (ModificationModeType.REPLACE.equals(ModificationModeType.fromValue(mod.getOperation()))) {
            if (mod.getValue().size() > 1) {
                // error
                throw new ManagerException("Ivalid value for attribute: " + mod.getName());
            } else if (mod.getValue().isEmpty()) {
                // delete the value
                return null;
            } else {
                // change the value
                return mod.getValue().get(0);
            }
        } else {
            throw new ManagerException("Only replace operation is valid for attribute: " + mod.getName());
        }
    }
    
    protected Document getDocumentTemplate() throws JAXBException, ParserConfigurationException {
        User u = new User();
        u.setCn("cn");
        u.setDescription("description");
        u.setPassword("password");
        u.getRole().add("role");
        return ModifyExecutor.user2Doc(u, ctx);
    }
    
    protected static String getModificationAttribute(Document userDoc, XPath xpath, String selector) throws XPathExpressionException {
        Node node = (Node) xpath.evaluate(selector, userDoc.getDocumentElement(), XPathConstants.NODE);
        if (node == null) {
            // try deleting namespaces
            ModifyExecutor.changeNamespaces(userDoc, userDoc.getDocumentElement(), "");
            node = (Node) xpath.evaluate(selector, userDoc.getDocumentElement(), XPathConstants.NODE);
            ModifyExecutor.changeNamespaces(userDoc, userDoc.getDocumentElement(), "urn:ddbb-spml-dsml:user");
        }
        System.err.println("Node for modification: " + node);
        if (node == null) {
            throw new XPathExpressionException("The xpath expression does not return a node: " + selector);
        } else {
            return node.getLocalName();
        }
    }
    
    protected static List<String> getModificationValues(Document fragment, String name, 
            boolean multiple) throws ManagerException {
        List<String> res = new ArrayList<>();
        NodeList nodes = fragment.getElementsByTagNameNS("urn:ddbb-spml-dsml:user", name);
        System.err.println(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            System.err.println(node.getTextContent());
            if (node.getTextContent() != null) {
                res.add(node.getTextContent());
            }
        }
        if (!multiple && res.size() > 1) {
            throw new ManagerException("The attribute cannot have multiple values: " + name);
        }
        return res;
    }
    
    protected static void checkNotRolesModification(BulkModifyRequestAccessor req, 
            ModificationType mod, Document[] docs) throws ManagerException {
        // only replace admitted for non role attribute
        if (!req.isModeReplace(mod)) {
            throw new ManagerException("Only replace operation is valid for xpath: " + req.getModificationXPath(mod));
        }
        // just one value can be sent or none
        if (docs == null || docs.length > 1) {
            throw new ManagerException("Only one value can be sent for xpath: " + req.getModificationXPath(mod));
        }
    }
    
    protected static UserManager.RoleOperation getGroupOperation(
            BulkModifyRequestAccessor req, DsmlModification mod) {
        if (mod == null) {
            return UserManager.RoleOperation.NONE;
        } else if (req.isModeAdd(mod)) {
            return UserManager.RoleOperation.ADD;
        } else if (req.isModeReplace(mod)) {
            return UserManager.RoleOperation.REPLACE;
        } else if (req.isModeDelete(mod)) {
            return UserManager.RoleOperation.DELETE;
        } else {
            return UserManager.RoleOperation.NONE;
        }
    }
    
    protected static UserManager.RoleOperation getGroupOperation(
            BulkModifyRequestAccessor req, ModificationType mod) {
        if (mod == null) {
            return UserManager.RoleOperation.NONE;
        } else if (req.isModeAdd(mod)) {
            return UserManager.RoleOperation.ADD;
        } else if (req.isModeReplace(mod)) {
            return UserManager.RoleOperation.REPLACE;
        } else if (req.isModeDelete(mod)) {
            return UserManager.RoleOperation.DELETE;
        } else {
            return UserManager.RoleOperation.NONE;
        }
    }
    
    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        BulkModifyResponseBuilder builder = ResponseBuilder.builderForBulkModify().requestId(item.getId());
        BulkModifyRequestAccessor req = item.getRequestAccessor().asBulkModify();
        try {
            SearchQueryAccessor query = req.getQuery();
            if (query != null) {
                if (query.isTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                    // DSML target
                    FilterAccessor filter = query.getQueryFilter();
                    String customFilter = SearchExecutor.filter2SQL(filter);
                    DsmlModification[] mods = req.getDsmlModifications();
                    // get the modifications for the attrs that can be bulk processed
                    DsmlModification cnMod = findModification("cn", mods);
                    DsmlModification descMod = findModification("description", mods);
                    DsmlModification passwdMod = findModification("password", mods);
                    DsmlModification rolesMod = findModification("role", mods);
                    um.bulkModify(customFilter, 
                            cnMod != null, getModificationValue(cnMod),
                            descMod != null, getModificationValue(descMod),
                            passwdMod != null, getModificationValue(passwdMod),
                            getGroupOperation(req, rolesMod), rolesMod == null? null : rolesMod.getValue());
                    builder.success();
                } else if (query.isTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                    // get the template to know what attrs are being modified
                    Document userDoc = getDocumentTemplate();
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    xpath.setNamespaceContext(new NamespaceContext() {
                        @Override
                        public String getNamespaceURI(String prefix) {
                            return prefix.equals("usr") ? "urn:ddbb-spml-dsml:user" : null;
                        }

                        @Override
                        public Iterator getPrefixes(String val) {
                            return null;
                        }

                        @Override
                        public String getPrefix(String uri) {
                            return uri.equals("urn:ddbb-spml-dsml:user") ? "usr" : null;
                        }
                    });
                    // find the modifications
                    String customFilter = SearchExecutor.xpath2SQL(query.getXsdXPathSelection());
                    boolean useCn = false;
                    String cn = null;
                    boolean useDesc = false;
                    String desc = null;
                    boolean usePasswd = false;
                    String password = null;
                    UserManager.RoleOperation op = UserManager.RoleOperation.NONE;
                    List<String> roles = null;
                    List<String> values = null;
                    for (ModificationType mod : req.getModifications()) {
                        String selector = req.getModificationXPath(mod);
                        Document[] docs = req.getXsdModificationFragment(mod);
                        String attr = getModificationAttribute(userDoc, xpath, selector);
                        switch (attr) {
                            case "cn":
                                useCn = true;
                                checkNotRolesModification(req, mod, docs);
                                values = getModificationValues(docs[0], attr, false);
                                cn = values.isEmpty()? null : values.get(0);
                                break;
                            case "description":
                                useDesc = true;
                                checkNotRolesModification(req, mod, docs);
                                values = getModificationValues(docs[0], attr, false);
                                desc = values.isEmpty()? null : values.get(0);
                                break;
                            case "password":
                                usePasswd = true;
                                checkNotRolesModification(req, mod, docs);
                                values = getModificationValues(docs[0], attr, false);
                                password = values.isEmpty()? null : values.get(0);
                                break;
                            case "role":
                            case "user":
                                roles = new ArrayList<>();
                                for (Document doc : docs) {
                                    roles.addAll(getModificationValues(doc, "role", true));
                                }
                                op = getGroupOperation(req, mod);
                                break;
                        }
                    }
                    um.bulkModify(customFilter, useCn, cn, useDesc, desc, usePasswd, password, op, roles);
                    builder.success();
                } else {
                    builder.failure().invalidIdentifier().errorMessage("Invalid search target id");
                }
            }
        } catch (ManagerException | JAXBException | ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
            builder.failure().customError().errorMessage(e.getMessage());
        }
        queue.finish(item, builder);
        return builder;
    }
}
