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
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ModifyRequestBuilder extends ModificationRequestBuilder<ModifyRequestType, ModifyRequestBuilder, ModifyRequestAccessor> {

    protected  ModifyRequestBuilder() {
        super(new ModifyRequestType());
    }
    
    @Override
    public ModifyRequestBuilder dsmlModification(DsmlModification dsmlMod) {
        ModificationType spmlMod = new ModificationType();
        spmlMod.getAny().add(getDsmlv2ObjectFactory().createModification(dsmlMod));
        request.getModification().add(spmlMod);
        return this;
    }
    
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
    
    @Override
    public JAXBElement<ModifyRequestType> build() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return getCoreObjectFactory().createModifyRequest(request);
    }
    
    @Override
    public ModifyRequestAccessor asAccessor() {
        request.setPsoID(pso);
        request.setReturnData(returnData);
        return BaseRequestAccessor.accessorForRequest(request).asModify();
    }

    @Override
    public ModifyRequestBuilder fromRequest(ModifyRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        this.returnData = request.getReturnData();
        return this;
    }
    
}
