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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ReturnDataType;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author ricky
 * @param <R>
 */
public abstract class ModificationRequestAccessor<R extends RequestType> extends RequestAccessor<R> {

    protected ModificationRequestAccessor(R request, PSOIdentifierType pso, ReturnDataType returnData) {
        super(request, pso, returnData);
    }
    
    public abstract DsmlModification[] getDsmlModifications();
    
    public abstract ModificationType[] getModifications();
    
    public boolean isModeReplace(DsmlModification mod) {
        return ModificationModeType.REPLACE.value().equals(mod.getOperation());
    }
    
    public boolean isModeAdd(DsmlModification mod) {
        return ModificationModeType.ADD.value().equals(mod.getOperation());
    }
    
    public boolean isModeDelete(DsmlModification mod) {
        return ModificationModeType.DELETE.value().equals(mod.getOperation());
    }
    
    public Document[] getXsdModificationFragment(ModificationType mod) throws ParserConfigurationException {
        List<Document> res = new ArrayList<>();
        SelectionType sel = mod.getComponent();
        if (sel != null) {
            for (Object o : sel.getAny()) {
                if (o instanceof Element) {
                    Element el = (Element) o;
                    Node n = el.cloneNode(true);
                    Document doc = documentBuilderFactory.newDocumentBuilder().newDocument();
                    doc.adoptNode(n);
                    doc.appendChild(n);
                    res.add(doc);
                }
            }
        }
        return res.toArray(new Document[0]);
    }
    
    public Object[] getXsdModificationObject(ModificationType mod, Class clazz) {
        List<Object> res = new ArrayList<>();
        SelectionType sel = mod.getComponent();
        if (sel != null) {
            for (Object o : sel.getAny()) {
                if (clazz.isInstance(o)) {
                    res.add(o);
                } else if (o instanceof JAXBElement) {
                    JAXBElement el = (JAXBElement) o;
                    if (clazz.isInstance(el.getValue())) {
                        res.add(el.getValue());
                    }
                }
            }
        }
        return res.toArray();
    }
    
    public String getModificationXPath(ModificationType mod) {
        SelectionType sel = mod.getComponent();
        if (sel != null) {
            return sel.getPath();
        } else {
            return null;
        }
    }
    
    public boolean isModeReplace(ModificationType mod) {
        return ModificationModeType.REPLACE.equals(mod.getModificationMode());
    }
    
    public boolean isModeAdd(ModificationType mod) {
        return ModificationModeType.ADD.equals(mod.getModificationMode());
    }
    
    public boolean isModeDelete(ModificationType mod) {
        return ModificationModeType.DELETE.equals(mod.getModificationMode());
    }
    
    public String toString(Class clazz) {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  Modifications:").append(nl);
        for (DsmlModification mod : getDsmlModifications()) {
            sb.append("    ").append(mod.getOperation()).append(": ")
                    .append(mod.getName()).append(" - ").append(mod.getValue());
        }
        for (ModificationType mod: getModifications()) {
             Document[] docs = null;
            try {
                docs = getXsdModificationFragment(mod);
            } catch (ParserConfigurationException e) {}
            if (docs != null) {
                for (Document doc : docs) {
                    sb.append("    ").append(mod.getModificationMode())
                            .append(": ").append(doc);
                }
            } else if (clazz != null) {
                Object[] objs = getXsdModificationObject(mod, clazz);
                if (objs != null) {
                    for (Object o : objs) {
                        sb.append("    ").append(mod.getModificationMode())
                                .append(": ").append(o);
                    }
                }
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }
}
