/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.PsoIdentifierBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.core.ExtensibleType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class CloneRequestBuilder extends RequestBuilder<CloneRequestType, CloneRequestBuilder, 
        CloneRequestAccessor, CloneResponseAccessor> {

    static protected final ObjectFactory cloneObjectFactory = new ObjectFactory();
    
    protected CloneRequestBuilder() {
        super(new CloneRequestType());
    }
    
    static public CloneRequestBuilder builderForClone() {
        return new CloneRequestBuilder();
    }
    
    public ObjectFactory getCloneObjectFactory() {
        return cloneObjectFactory;
    }
    
    //
    // TARGETID
    //
    
    public CloneRequestBuilder targetId(String targetId) {
        request.setTargetID(targetId);
        return this;
    }

    //
    // ContainerID
    //
    
    public CloneRequestBuilder containerId(String containerId) {
        PSOIdentifierType container = request.getContainerID();
        if (container == null) {
            container = new PSOIdentifierType();
            request.setContainerID(container);
        }
        container.setID(containerId);
        return this;
    }
    
    public CloneRequestBuilder containerTargetId(String containerTargetId) {
        PSOIdentifierType container = request.getContainerID();
        if (container == null) {
            container = new PSOIdentifierType();
            request.setContainerID(container);
        }
        container.setTargetID(containerTargetId);
        return this;
    }
    
    public CloneRequestBuilder container(PsoIdentifierBuilder builder) {
        PSOIdentifierType container = request.getContainerID();
        request.setContainerID(builder.build());
        return this;
    }
    
    //
    // DATA
    //
    
    public CloneRequestBuilder dsmlAttribute(DsmlAttr attr) {
        ExtensibleType ext = request.getData();
        if (ext == null) {
            ext = new ExtensibleType();
            request.setData(ext);
        }
        ext.getAny().add(getDsmlv2ObjectFactory().createAttr(attr));
        return this;
    }
    
    public CloneRequestBuilder dsmlAttribute(String name, String... values) {
        DsmlAttr attr = new DsmlAttr();
        attr.setName(name);
        attr.getValue().addAll(Arrays.asList(values));
        this.dsmlAttribute(attr);
        return this;
    }
    
    public CloneRequestBuilder xsdObject(Object o) {
        ExtensibleType ext = request.getData();
        if (ext == null) {
            ext = new ExtensibleType();
            request.setData(ext);
        }
        ext.getAny().add(o);
        return this;
    }
    
    //
    // TEMPLATE
    //
    
    public CloneRequestBuilder templateId(String templateId) {
        PSOIdentifierType template = request.getPsoTemplateID();
        if (template == null) {
            template = new PSOIdentifierType();
            request.setPsoTemplateID(template);
        }
        template.setID(templateId);
        return this;
    }
    
    public CloneRequestBuilder templateTargetId(String templateTargetId) {
        PSOIdentifierType template = request.getPsoTemplateID();
        if (template == null) {
            template = new PSOIdentifierType();
            request.setPsoTemplateID(template);
        }
        template.setTargetID(templateTargetId);
        return this;
    }
    
    public CloneRequestBuilder template(PsoIdentifierBuilder builder) {
        request.setPsoTemplateID(builder.build());
        return this;
    }
    
    //
    // DEFAULT METHODS
    //
    
    @Override
    public JAXBElement<CloneRequestType> build() {
        request.setReturnData(returnData);
        request.setPsoID(pso);
        return getCloneObjectFactory().createCloneRequestType(request);
    }

    @Override
    public CloneRequestAccessor asAccessor() {
        request.setReturnData(returnData);
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asAccessor(new CloneRequestAccessor(request));
    }
    
    @Override
    public CloneRequestBuilder fromRequest(CloneRequestType request) {
        this.request = request;
        this.returnData = request.getReturnData();
        this.pso = request.getPsoID();
        return this;
    }

    @Override
    public CloneResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asAccessor(CloneResponseAccessor.emptyResponseAccessor());
    }
    
}
