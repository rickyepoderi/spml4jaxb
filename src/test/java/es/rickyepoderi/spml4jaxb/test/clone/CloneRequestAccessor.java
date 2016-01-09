/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.AddRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.PsoIdentifierAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;

/**
 *
 * @author ricky
 */
public class CloneRequestAccessor extends BaseRequestAccessor<CloneRequestType, CloneRequestAccessor, CloneRequestBuilder> {

    protected CloneRequestAccessor() {
        this(new CloneRequestType());
    }
    
    protected CloneRequestAccessor(CloneRequestType request) {
        super(request, request.getPsoID(), request.getReturnData());
    }
    
    static public CloneRequestAccessor emptyCloneAccessor() {
        return new CloneRequestAccessor();
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
    
    public DsmlAttr[] getDsmlAttributes() {
        return AddRequestAccessor.getDsmlAttributes(request.getData() == null? null:request.getData().getAny());
    }
    
    public <T> T getXsdObject(Class<T> clazz) {
        return AddRequestAccessor.getXsdObject(request.getData() == null? null:request.getData().getAny(), clazz);
    }
    
    public String getTemplateId() {
        if (request.getPsoTemplateID()!= null) {
            return request.getPsoTemplateID().getID();
        } else {
            return null;
        }
    }
    
    public String getTemplateTargetId() {
        if (request.getPsoTemplateID() != null) {
            return request.getPsoTemplateID().getTargetID();
        } else {
            return null;
        }
    }
    
    public PsoIdentifierAccessor getTemplate() {
        if (request.getPsoTemplateID() != null) {
            return new PsoIdentifierAccessor(request.getPsoTemplateID());
        } else {
            return null;
        }
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return new CloneResponseBuilder();
    }

    @Override
    public CloneRequestBuilder toBuilder() {
        return CloneRequestBuilder.builderForClone().fromRequest(this.request);
    }

    @Override
    public CloneRequestAccessor asAccessor(CloneRequestType request) {
        return new CloneRequestAccessor(request);
    }
    
}
