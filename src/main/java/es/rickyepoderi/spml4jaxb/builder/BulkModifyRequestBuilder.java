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

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
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
public class BulkModifyRequestBuilder extends ModificationRequestBuilder<BulkModifyRequestType, BulkModifyRequestBuilder> {

    public BulkModifyRequestBuilder() {
        super(new BulkModifyRequestType());
    }

    @Override
    public BulkModifyRequestBuilder dsmlModification(DsmlModification dsmlMod) {
        if (request.getModification().isEmpty()) {
            request.getModification().add(new ModificationType());
        }
        ModificationType spmlMod = request.getModification().get(0);
        spmlMod.getAny().add(getDsmlv2ObjectFactory().createModification(dsmlMod));
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
    public RequestAccessor asAccessor() {
        return super.asAccessor().asBulkModify();
    }

    @Override
    public BulkModifyRequestBuilder fromRequest(BulkModifyRequestType request) {
        this.request = request;
        return this;
    }
    
}
