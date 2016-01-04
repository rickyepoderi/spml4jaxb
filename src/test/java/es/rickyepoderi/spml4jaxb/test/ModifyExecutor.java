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

import es.rickyepoderi.spml4jaxb.accessor.ModifyRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ModifyResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import es.rickyepoderi.spml4jaxb.user.InvalidUserException;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author ricky
 */
public class ModifyExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    private JAXBContext ctx;
    
    public ModifyExecutor(UserManager um, WorkQueue queue, JAXBContext ctx) {
        super(queue);
        this.um = um;
        this.ctx = ctx;
    }
    
    static public User spmlDsml2User(User u, DsmlModification[] mods) throws ManagerException {
        for (DsmlModification mod: mods) {
            AddExecutor.modifyAttr(u, mod.getName(), ModificationModeType.fromValue(mod.getOperation()), mod.getValue());
        }
        return u;
    }
    
    static public Document user2Doc(User user, JAXBContext ctx) throws JAXBException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        Document doc = dbf.newDocumentBuilder().newDocument();
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.marshal(user, doc);
        return doc;
    }
    
    static public User doc2User(Document doc, JAXBContext ctx) throws JAXBException {
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        return (User) unmarshaller.unmarshal(new DOMSource(doc));
    }
    
    static public Document changeNamespaces(Document doc, Node node, String namespace) {
        doc.renameNode(node, namespace, node.getLocalName());
        for (Node child = node.getFirstChild(); child != null;) {
            Node nextChild = child.getNextSibling();
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                changeNamespaces(doc, child, namespace);
            }
            child = nextChild;
        }
        return doc;
    }
    
    static public void proccessModification(ModifyRequestAccessor req, Document doc,
            Node xpathNode, ModificationType mod, Document userDoc) {
        if (req.isModeAdd(mod)) {
            // add to the current node the modification
            Node addNode = doc.getDocumentElement().cloneNode(true);
            userDoc.adoptNode(addNode);
            System.err.println("Adding node " + addNode + " to " + xpathNode);
            xpathNode.appendChild(addNode);
        } else if (req.isModeDelete(mod)) {
            // delete the node found as an xpath
            xpathNode.getParentNode().removeChild(xpathNode);
            System.err.println("Deleting node " + xpathNode);
        } else if (req.isModeReplace(mod)) {
            // replace the node found as an xpath by the one sent in the modification
            Node newNode = doc.getDocumentElement().cloneNode(true);
            userDoc.adoptNode(newNode);
            System.err.println("Replacing node " + xpathNode + " with " + newNode);
            Node parent = xpathNode.getParentNode();
            Node nextSibling = xpathNode.getNextSibling();
            parent.removeChild(xpathNode);
            if (nextSibling == null) {
                parent.appendChild(newNode);
            } else {
                parent.insertBefore(newNode, nextSibling);
            }
        }
    }
    
    static public User spmlXsd2User(User u, ModificationType[] mods, JAXBContext ctx, ModifyRequestAccessor req) throws ManagerException {
        try {
            Document userDoc = user2Doc(u, ctx);
            if (mods != null) {
                for (ModificationType mod : mods) {
                    Document[] docs = req.getXsdModificationFragment(mod);
                    Object[] users = req.getXsdModificationObject(mod, User.class);
                    XPath xpath = XPathFactory.newInstance().newXPath();
                    // first try using namespaces
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
                    String namespace = null;
                    Node node = (Node) xpath.evaluate(req.getModificationXPath(mod), userDoc.getDocumentElement(), XPathConstants.NODE);
                    if (node == null) {
                        // try deleting namespaces
                        namespace = "urn:ddbb-spml-dsml:user";
                        changeNamespaces(userDoc, userDoc.getDocumentElement(), "");
                        node = (Node) xpath.evaluate(req.getModificationXPath(mod), userDoc.getDocumentElement(), XPathConstants.NODE);
                    }
                    System.err.println("Node for modification: " + node);
                    if (node == null) {
                        throw new ManagerException("The xpath does not return a node");
                    }
                    if (!req.isModeDelete(mod)) {
                        // process if the user has sent completely
                        for (Object user : users) {
                            Document doc = user2Doc((User) user, ctx);
                            proccessModification(req, doc, node, mod, userDoc);
                        }
                        // process any doc
                        for (Document doc : docs) {
                            proccessModification(req, doc, node, mod, userDoc);
                        }
                    } else {
                        proccessModification(req, null, node, mod, userDoc);
                    }
                    if (namespace != null) {
                        changeNamespaces(userDoc, userDoc.getDocumentElement(), namespace);
                    }
                }
                // after all modifications retransform dom to user
                u = doc2User(userDoc, ctx);
            }
            System.err.println(u);
            return u;
        } catch (DOMException | JAXBException | XPathExpressionException | ParserConfigurationException e) {
            throw new ManagerException(e);
        }
    }

    @Override
    public RequestAccessor specificAccessor(RequestAccessor request) {
        return request.asModify();
    }

    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        ModifyResponseBuilder builder = ResponseBuilder.builderForModify()
                .requestId(item.getId());
        ModifyRequestAccessor req = item.getRequestAccessor().asModify();
        try {
            if (req.getPsoId() != null) {
                if (req.isPsoTargetId(ListTargetsExecutor.DSML_TARGET_ID)
                        || req.isPsoTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                    User u = um.read(req.getPsoId());
                    if (u != null) {
                        if (req.isPsoTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                            // dsml
                            u = spmlDsml2User(u, req.getDsmlModifications());
                        } else {
                            // xsd
                            u = spmlXsd2User(u, req.getModifications(), ctx, req);
                        }
                        um.update(u);
                        builder.psoId(u.getUid()).psoTargetId(req.getPsoTargetId());
                        if (req.isPsoTargetId(ListTargetsExecutor.DSML_TARGET_ID)
                                && (req.isReturnData() || req.isReturnEverything())) {
                            builder.dsmlAttribute(AddExecutor.user2Dsml(u));
                        } else if (req.isPsoTargetId(ListTargetsExecutor.XSD_TARGET_ID)
                                && (req.isReturnData() || req.isReturnEverything())) {
                            builder.xsdObject(u);
                        }
                        builder.success();
                    } else {
                        builder.failure().noSuchIdentifier()
                                .errorMessage("The psoId does not exists");
                    }
                } else {
                    builder.failure().invalidIdentifier()
                            .errorMessage("The PSO targetId should be provided");
                }
            } else {
                builder.failure().invalidIdentifier()
                        .errorMessage("The psoId should be provided");
            }
        } catch (InvalidUserException e) {
            builder.failure()
                    .malformedRequest()
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
