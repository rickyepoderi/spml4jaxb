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
 * <p>Builder for the SPMLv2 Bulk Modify operation request. The bulk modify
 * operation is defined inside the bulk capability (capability to perform
 * massive deletes or modifies based on a search condition). The bulk
 * modify performs the modify of a bunch of objects that honors a 
 * specific search criteria using always the same modifications. The search 
 * criteria used is the same as defined in the search capability. The 
 * modifications are passed exactly in the same way as in the core modify
 * operation, that is why there is a parent class for both types of 
 * requests.</p>
 * 
 * @author ricky
 */
public class BulkModifyRequestBuilder extends ModificationRequestBuilder<BulkModifyRequestType, BulkModifyRequestBuilder, 
        BulkModifyRequestAccessor, BulkModifyResponseAccessor> {

    /**
     * Builder for an empty bulk modify request builder.
     */
    public BulkModifyRequestBuilder() {
        super(new BulkModifyRequestType());
    }

    /**
     * Adds a new DSML modification to the modification list. This method will
     * be used when using a DSML profile target.
     * 
     * @param dsmlMod The DSML modification to add
     * @return The same builder
     */
    @Override
    public BulkModifyRequestBuilder dsmlModification(DsmlModification dsmlMod) {
        ModificationType spmlMod = new ModificationType();
        spmlMod.getAny().add(getDsmlv2ObjectFactory().createModification(dsmlMod));
        request.getModification().add(spmlMod);
        return this;
    }

    /**
     * Adds a new XSD modification to the list. This method will be used when
     * using a XSD profile target.
     * 
     * @param type The modification type (add, replace, delete)
     * @param xpath The XPATH where the modification applies
     * @param o The object to add, delete or replace
     * @return The same builder
     */
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
    
    /**
     * Setter for the query that applies in the request. The query is exactly the
     * same format of the one used in the search operation. So a query builder
     * is used.
     * 
     * @param query The query builder that is used to construct the query
     * @return The same builder
     */
    public BulkModifyRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<BulkModifyRequestType> build() {
        return getBulkObjectFactory().createBulkModifyRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asBulkModify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyRequestBuilder fromRequest(BulkModifyRequestType request) {
        this.request = request;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asBulkModify();
    }
    
}
