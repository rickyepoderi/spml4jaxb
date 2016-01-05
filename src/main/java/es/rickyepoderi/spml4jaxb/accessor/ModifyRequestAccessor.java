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

import es.rickyepoderi.spml4jaxb.builder.ModifyRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ModifyRequestAccessor extends ModificationRequestAccessor<ModifyRequestType, ModifyRequestBuilder> {

    protected ModifyRequestAccessor(ModifyRequestType request) {
        super(request, request.getPsoID(), request.getReturnData());
    }
    
    @Override
    public DsmlModification[] getDsmlModifications() {
        List<DsmlModification> res = new ArrayList<>();
        if (request.getModification() != null) {
            for (ModificationType mod : request.getModification()) {
                List<Object> l = mod.getAny();
                for (Object o : l) {
                    if (o instanceof DsmlModification) {
                        res.add((DsmlModification) o);
                    } else if (o instanceof JAXBElement) {
                        JAXBElement el = (JAXBElement) o;
                        if (el.getValue() instanceof DsmlModification) {
                            res.add((DsmlModification) el.getValue());
                        }
                    }
                }
            }
        }
        return res.toArray(new DsmlModification[0]);
    }
    
    @Override
    public ModificationType[] getModifications() {
        return request.getModification().toArray(new ModificationType[0]);
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForModify();
    }
    
    @Override
    public ModifyRequestBuilder toBuilder() {
        return RequestBuilder.builderForModify().fromRequest(this.request);
    }
}
