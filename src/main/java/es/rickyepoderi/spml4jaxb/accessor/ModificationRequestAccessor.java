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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ModificationRequestBuilder;
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
 * <p>This class is a base modification request accessor for real SPMLv2
 * Modify and BulkModify operations. Both operation requests manages the same
 * data (the modifications) so it is quite logical to share how to pass the
 * modifications to the internal request type.</p>
 * 
 * @author ricky
 * @param <R> The real SPMLv2 request object (JAXB obtained)
 * @param <A> The accessor itself to be used in some methods
 * @param <B> The builder to see the request in the builder side
 */
public abstract class ModificationRequestAccessor<R extends RequestType, 
        A extends ModificationRequestAccessor, B extends ModificationRequestBuilder> 
        extends BaseRequestAccessor<R, A, B> {

    /**
     * Constructor created using all the parameters.
     * 
     * @param request The internal request type obtained by JAXB
     * @param pso The pso identifier 
     * @param returnData How to return the data
     */
    protected ModificationRequestAccessor(R request, PSOIdentifierType pso, ReturnDataType returnData) {
        super(request, pso, returnData);
    }
    
    /**
     * Abstract method to retrieve the real DSML modifications from the internal
     * type. This method should be implemented for any real modification request
     * accessor. This method will be used when communicating to a DSML profile
     * target.
     * 
     * @return  The array of modifications or empty array
     */
    public abstract DsmlModification[] getDsmlModifications();
    
    /**
     * Abstract method to return an array of SPMLv2 modification type. This
     * method will be using when communicating to a XSD profile.
     * 
     * @return The array of modifications or empty array
     */
    public abstract ModificationType[] getModifications();
    
    /**
     * Checks if the passed DSML modification is a replace modification.
     * 
     * @param mod The modification to check
     * @return true is the DSML modification is a replace
     */
    public boolean isModeReplace(DsmlModification mod) {
        return ModificationModeType.REPLACE.value().equals(mod.getOperation());
    }
    
    /**
     * Checks if the passed DSML modification is an add modification.
     * 
     * @param mod The modification to check
     * @return true is the DSML modification is an add
     */
    public boolean isModeAdd(DsmlModification mod) {
        return ModificationModeType.ADD.value().equals(mod.getOperation());
    }
    
    /**
     * Checks if the passed DSML modification is a delete modification.
     * 
     * @param mod The modification to check
     * @return true is the DSML modification is a delete
     */
    public boolean isModeDelete(DsmlModification mod) {
        return ModificationModeType.DELETE.value().equals(mod.getOperation());
    }
    
    /**
     * Returns an array of fragments as a DOM document array. The idea is that
     * SPMLv2 let the client to send not the whole object to replace but parts
     * or fragments of the XML that can be added, replaced or deleted from
     * a XPATH starting point (XSD profile). When doing that this method should 
     * be used tp retrieve those fragments as documents.
     * 
     * @param mod The SPMLv2 modification to obtain the fragments from
     * @return The array of documents or an empty array
     * @throws ParserConfigurationException Some error parsing the XML
     */
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
    
    /**
     * Returns an array of objects of the given class from a SPML modification.
     * This method will be used in the XSD profile to obtain a full object from
     * the modification (remember that the client can send a full object or
     * fragments).
     * 
     * @param mod The modification to obtain the objects from
     * @param clazz The class of the object to obtain
     * @return The array of objects or nulls
     */
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
    
    /**
     * Gets the modification XPATH of a SPML modification. The target should
     * be XSD profiled. The XPATH marks where to apply the modification sent
     * by the client.
     * 
     * @param mod The modification to obtain the XPATH from
     * @return The SPATH expression
     */
    public String getModificationXPath(ModificationType mod) {
        SelectionType sel = mod.getComponent();
        if (sel != null) {
            return sel.getPath();
        } else {
            return null;
        }
    }
    
    /**
     * Checks if the SPML modification (XSD profile) is a replace.
     * 
     * @param mod The modification to check
     * @return true if it is a replace, false otherwise
     */
    public boolean isModeReplace(ModificationType mod) {
        return ModificationModeType.REPLACE.equals(mod.getModificationMode());
    }
    
    /**
     * Checks if the SPML modification (XSD profile) is an add.
     * 
     * @param mod The modification to check
     * @return true if it is an add, false otherwise
     */
    public boolean isModeAdd(ModificationType mod) {
        return ModificationModeType.ADD.equals(mod.getModificationMode());
    }
    
    /**
     * Checks if the SPML modification (XSD profile) is a delete.
     * 
     * @param mod The modification to check
     * @return true if it is a delete, false otherwise
     */
    public boolean isModeDelete(ModificationType mod) {
        return ModificationModeType.DELETE.equals(mod.getModificationMode());
    }
    
    /**
     * Returns the string representation of the request supposing that
     * the objects sent are of the class specified.
     * @param clazz The class type of the objects sent
     * @return The string representation of the request
     */
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(null);
    }
}
