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

import es.rickyepoderi.spml4jaxb.builder.AddRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.AddRequestType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;

/**
 * <p>Accessor for the Add request. The Add operation is a function in the core
 * capability as defined in SPMLv2. This method adds / creates a new PSO inside
 * the repository. The accessor gives methods to retrieve easily attributes
 * if using the DSML profile or the object itself if using the XSD profile.</p>
 * 
 * @author ricky
 */
public class AddRequestAccessor extends BaseRequestAccessor<AddRequestType, AddRequestAccessor, AddRequestBuilder> {

    /**
     * Constructor for an empty add request accessor.
     */
    protected AddRequestAccessor() {
        this(new AddRequestType());
    }
    
    /**
     * Constructor specifying the internal add type.
     * @param request The add type obtained using JAXB over SPMLv2 XSD files.
     */
    protected AddRequestAccessor(AddRequestType request) {
        super(request, request.getPsoID(), request.getReturnData());
    }
    
    /**
     * Getter for the container ID of the request.
     * @return The container ID
     */
    public String getContainerId() {
        if (request.getContainerID() != null) {
            return request.getContainerID().getID();
        } else {
            return null;
        }
    }
    
    /**
     * Getter for the container target ID.
     * @return The container target ID
     */
    public String getContainerTargetId() {
        if (request.getContainerID() != null) {
            return request.getContainerID().getTargetID();
        } else {
            return null;
        }
    }
    
    /**
     * Getter for the container PSO identifier but returning the PSO identifier
     * accessor.
     * @return The PSO identifier accessor for the container
     */
    public PsoIdentifierAccessor getContainer() {
        if (request.getContainerID() != null) {
            return new PsoIdentifierAccessor(request.getContainerID());
        } else {
            return null;
        }
    }
    
    /**
     * Getter for the target ID of the operation.
     * @return The target ID of teh operation
     */
    public String getTargetId() {
        return request.getTargetID();
    }
    
    /**
     * Checks if the target ID is the specified one.
     * @param id The target ID to compare
     * @return true if the target ID of the request if the one specified in the argument
     */
    public boolean isTargetId(String id) {
        return id.equals(request.getTargetID());
    }
    
    /**
     * Method that returns an array of DSML attributes based on a list
     * of objects. The list of objects is iterated to find instances of
     * the class DsmlAttr and the object there are returned as an array.
     * 
     * @param list The list of objects to look for DsmlAttr objects
     * @return An array of DsmlAttr objects or an empty array
     */
    static public DsmlAttr[] getDsmlAttributes(List<Object> list) {
        List<DsmlAttr> res = new ArrayList<>();
        if (list != null) {
            for (Object o : list) {
                if (o instanceof DsmlAttr) {
                    res.add((DsmlAttr) o);
                } else if (o instanceof JAXBElement) {
                    JAXBElement el = (JAXBElement) o;
                    if (el.getValue() instanceof DsmlAttr) {
                        res.add((DsmlAttr) el.getValue());
                    }
                }
            }
        }
        return res.toArray(new DsmlAttr[0]);
    }
    
    /**
     * Method that return the array of DsmlAttr that the request contains. It
     * returns an empty array if none is specified. This method is thought to
     * be used if the target uses the DSML profile.
     * @return The array of DsmlAttr attributes that the request has (or empty array)
     */
    public DsmlAttr[] getDsmlAttributes() {
        return getDsmlAttributes(request.getData() == null? null:request.getData().getAny());
    }
    
    /**
     * Get the DSML attributes but in a map keyed by the attribute name
     * (used in DSML profile targets).
     * @return The map of attributes or empty map
     */
    public Map<String,DsmlAttr> getDsmlAttributesMap() {
        DsmlAttr[] attrs = getDsmlAttributes();
        Map<String,DsmlAttr> res = new HashMap<>(attrs.length);
        for (DsmlAttr attr: attrs) {
            res.put(attr.getName(), attr);
        }
        return res;
    }
    
    /**
     * Gets the attribute for a specific name (used in DSML profile targets).
     * @param name The name of the attribute
     * @return The DSML attribute or null
     */
    public DsmlAttr getDsmlAttribute(String name) {
        return this.getDsmlAttributesMap().get(name);
    }
    
    /**
     * The values of a DSML attribute (used in DSML profile targets).
     * @param name The attribute name
     * @return The array of values or null
     */
    public String[] getDsmlAttributeValues(String name) {
        DsmlAttr attr = this.getDsmlAttribute(name);
        if (attr != null) {
            return attr.getValue().toArray(new String[0]);
        } else {
            return null;
        }
    }
    
    /**
     * The first value of a DSML attribute (used in DSML profile targets).
     * @param name The attribute name
     * @return The first value of null
     */
    public String getDsmlAttributeFirstValue(String name) {
        String[] values = this.getDsmlAttributeValues(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }
    
    /**
     * Method that return an object of the class specified if it is contained
     * in the list. It return the first object found in the list.
     * 
     * @param <T> The class of the object to look for
     * @param list The list of objects to look into
     * @param clazz The class to search
     * @return The first object of class clazz in the list or null
     */
    static public <T> T getXsdObject(List<Object> list, Class<T> clazz) {
        if (list != null) {
            for (Object o: list) {
                if (clazz.isInstance(o)) {
                    return clazz.cast(o);
                } else if (o instanceof JAXBElement) {
                    JAXBElement el = (JAXBElement) o;
                    if (clazz.isInstance(el.getValue())) {
                        return clazz.cast(el.getValue());
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Method that return the object of a specific class that is contained
     * in the add request. This method should be used in the XSD profile.
     * 
     * @param <T> The type of the object we are looking for
     * @param clazz The class of that type
     * @return The first object of that class in the add request or null
     */
    public <T> T getXsdObject(Class<T> clazz) {
        return getXsdObject(request.getData() == null? null:request.getData().getAny(), clazz);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForAdd();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AddRequestBuilder toBuilder() {
        return RequestBuilder.builderForAdd().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddRequestAccessor asAccessor(AddRequestType request) {
        return new AddRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString())
                .append("  targetId: ").append(getTargetId()).append(nl);
        DsmlAttr[] attrs  = getDsmlAttributes();
        if (attrs != null && attrs.length > 0) {
            sb.append("  Attributes:").append(nl);
            for (DsmlAttr attr : attrs) {
                sb.append("    ").append(attr.getName()).append(": ")
                        .append(attr.getValue()).append(nl);
            }
        }
        return sb.toString();
    }
}
