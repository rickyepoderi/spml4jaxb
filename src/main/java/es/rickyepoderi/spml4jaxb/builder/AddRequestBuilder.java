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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.AddRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.msg.core.AddRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ExtensibleType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class AddRequestBuilder extends RequestBuilder<AddRequestType, AddRequestBuilder, AddRequestAccessor> {

    protected AddRequestBuilder() {
        super(new AddRequestType());
    }

    //
    // TARGETID
    //
    
    public AddRequestBuilder targetId(String targetId) {
        request.setTargetID(targetId);
        return this;
    }

    //
    // ContainerID
    //
    
    public AddRequestBuilder containerId(String containerId) {
        PSOIdentifierType container = request.getContainerID();
        if (container == null) {
            container = new PSOIdentifierType();
            request.setContainerID(container);
        }
        container.setID(containerId);
        return this;
    }
    
    public AddRequestBuilder containerTargetId(String containerTargetId) {
        PSOIdentifierType container = request.getContainerID();
        if (container == null) {
            container = new PSOIdentifierType();
            request.setContainerID(container);
        }
        container.setTargetID(containerTargetId);
        return this;
    }
    
    //
    // DATA
    //
    
    public AddRequestBuilder dsmlAttribute(DsmlAttr attr) {
        ExtensibleType ext = request.getData();
        if (ext == null) {
            ext = new ExtensibleType();
            request.setData(ext);
        }
        ext.getAny().add(getDsmlv2ObjectFactory().createAttr(attr));
        return this;
    }
    
    public AddRequestBuilder dsmlAttribute(String name, String... values) {
        DsmlAttr attr = new DsmlAttr();
        attr.setName(name);
        attr.getValue().addAll(Arrays.asList(values));
        this.dsmlAttribute(attr);
        return this;
    }
    
    public AddRequestBuilder xsdObject(Object o) {
        ExtensibleType ext = request.getData();
        if (ext == null) {
            ext = new ExtensibleType();
            request.setData(ext);
        }
        ext.getAny().add(o);
        return this;
    }

    @Override
    public JAXBElement<AddRequestType> build() {
        request.setReturnData(returnData);
        request.setPsoID(pso);
        return getCoreObjectFactory().createAddRequest(request);
    }
    
    @Override
    public AddRequestAccessor asAccessor() {
        request.setReturnData(returnData);
        request.setPsoID(pso);
        return RequestAccessor.accessorForRequest(request).asAdd();
    }

    @Override
    public AddRequestBuilder fromRequest(AddRequestType request) {
        this.request = request;
        this.returnData = request.getReturnData();
        this.pso = request.getPsoID();
        return this;
    }

}
