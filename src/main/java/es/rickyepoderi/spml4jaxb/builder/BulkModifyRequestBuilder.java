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

import es.rickyepoderi.spml4jaxb.accessor.BulkModifyRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BulkModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class BulkModifyRequestBuilder extends ModificationRequestBuilder<BulkModifyRequestType, BulkModifyRequestBuilder, 
        BulkModifyRequestAccessor, BulkModifyResponseAccessor> {

    public BulkModifyRequestBuilder() {
        super(new BulkModifyRequestType());
    }

    @Override
    public BulkModifyRequestBuilder dsmlModification(DsmlModification dsmlMod) {
        ModificationType spmlMod = new ModificationType();
        spmlMod.getAny().add(getDsmlv2ObjectFactory().createModification(dsmlMod));
        request.getModification().add(spmlMod);
        return this;
    }

    @Override
    public BulkModifyRequestBuilder xsdModification(ModificationModeType type, String xpath, Object o) {
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
    
    public BulkModifyRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }
    
    @Override
    public JAXBElement<BulkModifyRequestType> build() {
        return getBulkObjectFactory().createBulkModifyRequest(request);
    }
    
    @Override
    public BulkModifyRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asBulkModify();
    }

    @Override
    public BulkModifyRequestBuilder fromRequest(BulkModifyRequestType request) {
        this.request = request;
        return this;
    }

    @Override
    public BulkModifyResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendInternal(client).asBulkModify();
    }
    
}
