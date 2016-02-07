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

import es.rickyepoderi.spml4jaxb.accessor.ModifyRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the modify request. The modify operation is a function in the core
 * capability as defined in SPMLv2. This method modifies an existing object from
 * the target. The modification uses an intermediate class to manage the
 * different modifications can be sent (DSML and XSD).</p>
 * 
 * @author ricky
 */
public class ModifyRequestBuilder extends ModificationRequestBuilder<ModifyRequestType, ModifyRequestBuilder, 
        ModifyRequestAccessor, ModifyResponseAccessor> {

    /**
     * Constructor for an empty modify request builder.
     */
    protected  ModifyRequestBuilder() {
        super(new ModifyRequestType());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyRequestBuilder dsmlModification(DsmlModification dsmlMod) {
        ModificationType spmlMod = new ModificationType();
        spmlMod.getAny().add(getDsmlv2ObjectFactory().createModification(dsmlMod));
        request.getModification().add(spmlMod);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyRequestBuilder xsdModification(ModificationModeType type, String xpath, Object o) {
        ModificationType mod = new ModificationType();
        mod.setModificationMode(type);
        SelectionType sel = new SelectionType();
        sel.setPath(xpath);
        sel.setNamespaceURI("http://www.w3.org/TR/xpath20");
        if (!ModificationModeType.DELETE.equals(type)) {
            sel.getAny().add(o);
        }
        mod.setComponent(sel);
        request.getModification().add(mod);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ModifyRequestType> build() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return getCoreObjectFactory().createModifyRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyRequestAccessor asAccessor() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return BaseRequestAccessor.accessorForRequest(request).asModify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyRequestBuilder fromRequest(ModifyRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        this.returnData = request.getReturnData();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asModify();
    }
    
}