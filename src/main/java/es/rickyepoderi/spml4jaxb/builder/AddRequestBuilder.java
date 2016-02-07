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
import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.msg.core.AddRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ExtensibleType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the Add request. The Add request is a function in the core
 * capability as defined in SPMLv2. This method adds / creates a new PSO inside
 * the repository. The builder gives methods to construct easily attributes
 * if using the DSML profile or the object itself if using the XSD profile.</p>
 * 
 * @author ricky
 */
public class AddRequestBuilder extends RequestBuilder<AddRequestType, AddRequestBuilder, AddRequestAccessor, AddResponseAccessor> {

    /**
     * Constructor for the empty add request builder.
     */
    protected AddRequestBuilder() {
        super(new AddRequestType());
    }

    //
    // TARGETID
    //
    
    /**
     * Sets the target ID in the response.
     * 
     * @param targetId The new target id used in the response
     * @return The same builder
     */
    public AddRequestBuilder targetId(String targetId) {
        request.setTargetID(targetId);
        return this;
    }

    //
    // ContainerID
    //
    
    /**
     * Sets the container ID in the response.
     * @param containerId The new container ID
     * @return The same builder
     */
    public AddRequestBuilder containerId(String containerId) {
        PSOIdentifierType container = request.getContainerID();
        if (container == null) {
            container = new PSOIdentifierType();
            request.setContainerID(container);
        }
        container.setID(containerId);
        return this;
    }
    
    /**
     * Sets the container target ID in the response.
     * @param containerTargetId The new container target ID
     * @return The same builder
     */
    public AddRequestBuilder containerTargetId(String containerTargetId) {
        PSOIdentifierType container = request.getContainerID();
        if (container == null) {
            container = new PSOIdentifierType();
            request.setContainerID(container);
        }
        container.setTargetID(containerTargetId);
        return this;
    }
    
    /**
     * Sets the container identifier using the builder.
     * @param builder The PSO builder for the container
     * @return The same builder
     */
    public AddRequestBuilder container(PsoIdentifierBuilder builder) {
        request.setContainerID(builder.build());
        return this;
    }
    
    //
    // DATA
    //
    
    /**
     * Sets a new DSML attribute in the data (data that forms the new PSO
     * to create in the DSML profile). 
     * 
     * @param attr The new DsmlAttr to add in the data
     * @return The same builder
     */
    public AddRequestBuilder dsmlAttribute(DsmlAttr attr) {
        ExtensibleType ext = request.getData();
        if (ext == null) {
            ext = new ExtensibleType();
            request.setData(ext);
        }
        ext.getAny().add(getDsmlv2ObjectFactory().createAttr(attr));
        return this;
    }
    
    /**
     * Sets a new DSML attribute but giving the attribute name and the values
     * instead the real DsmlAttr object.
     * @param name The name of the attribute to add
     * @param values The values of that attribute
     * @return The same builder
     */
    public AddRequestBuilder dsmlAttribute(String name, String... values) {
        DsmlAttr attr = new DsmlAttr();
        attr.setName(name);
        attr.getValue().addAll(Arrays.asList(values));
        this.dsmlAttribute(attr);
        return this;
    }
    
    /**
     * Sets the object to add but receiving the real object (it should be used
     * in the XSD profile). The object should be generated using JAXB and
     * the factory for that class added to the server and client.
     * 
     * @param o The object to set in the request to be added
     * @return The same builder
     */
    public AddRequestBuilder xsdObject(Object o) {
        ExtensibleType ext = request.getData();
        if (ext == null) {
            ext = new ExtensibleType();
            request.setData(ext);
        }
        ext.getAny().add(o);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<AddRequestType> build() {
        request.setReturnData(returnData);
        request.setPsoID(pso);
        return getCoreObjectFactory().createAddRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AddRequestAccessor asAccessor() {
        request.setReturnData(returnData);
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asAdd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddRequestBuilder fromRequest(AddRequestType request) {
        this.request = request;
        this.returnData = request.getReturnData();
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asAdd();
    }
}
