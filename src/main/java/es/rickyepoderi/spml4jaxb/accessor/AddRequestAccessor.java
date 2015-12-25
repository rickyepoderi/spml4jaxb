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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.AddRequestType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class AddRequestAccessor extends RequestAccessor<AddRequestType> {

    protected AddRequestAccessor(AddRequestType request) {
        super(request, request.getPsoID(), request.getReturnData());
    }
    
    public String getContainerId() {
        if (request.getContainerID() != null) {
            return request.getContainerID().getID();
        } else {
            return null;
        }
    }
    
    public String getContainerTargetId() {
        if (request.getContainerID() != null) {
            return request.getContainerID().getTargetID();
        } else {
            return null;
        }
    }
    
    public String getTargetId() {
        return request.getTargetID();
    }
    
    public boolean isTargetId(String id) {
        return id.equals(request.getTargetID());
    }
    
    public DsmlAttr[] getDsmlAttributes() {
        List<DsmlAttr> res = new ArrayList<>();
        if (request.getData() != null) {
            List<Object> l = request.getData().getAny();
            for (Object o: l) {
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
    
    public Object getXsdObject(Class clazz) {
        if (request.getData() != null) {
            List<Object> l = request.getData().getAny();
            for (Object o: l) {
                if (clazz.isInstance(o)) {
                    return o;
                } else if (o instanceof JAXBElement) {
                    JAXBElement el = (JAXBElement) o;
                    if (clazz.isInstance(el.getValue())) {
                        return el.getValue();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForAdd();
    }
    
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
