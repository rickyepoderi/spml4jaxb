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
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class AddRequestAccessor extends BaseRequestAccessor<AddRequestType, AddRequestAccessor, AddRequestBuilder> {

    protected AddRequestAccessor() {
        this(new AddRequestType());
    }
    
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
    
    public PsoIdentifierAccessor getContainer() {
        if (request.getContainerID() != null) {
            return new PsoIdentifierAccessor(request.getContainerID());
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
    
    public DsmlAttr[] getDsmlAttributes() {
        return getDsmlAttributes(request.getData() == null? null:request.getData().getAny());
    }
    
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
    
    public <T> T getXsdObject(Class<T> clazz) {
        return getXsdObject(request.getData() == null? null:request.getData().getAny(), clazz);
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForAdd();
    }
    
    @Override
    public AddRequestBuilder toBuilder() {
        return RequestBuilder.builderForAdd().fromRequest(this.request);
    }

    @Override
    public AddRequestAccessor asAccessor(AddRequestType request) {
        return new AddRequestAccessor(request);
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
