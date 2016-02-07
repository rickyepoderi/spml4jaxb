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

import es.rickyepoderi.spml4jaxb.builder.BulkModifyRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 * <p>Accessor for the SPMLv2 Bulk Modify operation request. The bulk modify
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
public class BulkModifyRequestAccessor extends ModificationRequestAccessor<BulkModifyRequestType, 
        BulkModifyRequestAccessor, BulkModifyRequestBuilder> { 

    /**
     * Constructor for an empty bulk modify request accessor.
     */
    protected BulkModifyRequestAccessor() {
        this(new BulkModifyRequestType());
    }
    
    /**
     * Constructor for a bulk modify request accessor using the internal type.
     * @param request The internal bulk modify request type as obtained by JAXB
     */
    protected BulkModifyRequestAccessor(BulkModifyRequestType request) {
        super(request, null, null);
    }
    
    /**
     * Getter for the array of DSML modifications present in the request. The array
     * represents the modifications to perform to all entries that matches the
     * search criteria. This method should be used when using a DSML target.
     * 
     * @return The array of modifications or empty array
     */
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

    /**
     * Getter for the array of modifications present in the request. This method
     * return the modifications of the request (intended for XSD target).
     * 
     * @return The array of modifications or empty array
     */
    @Override
    public ModificationType[] getModifications() {
        return request.getModification().toArray(new ModificationType[0]);
    }

    /**
     * Getter for the query accessor. The query is exactly the same than in the
     * search operation of the search capability.
     * 
     * @return The search query accessor to retrieve the search filter.
     */
    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (query != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForBulkModify();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyRequestBuilder toBuilder() {
        return RequestBuilder.builderForBulkModify().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkModifyRequestAccessor asAccessor(BulkModifyRequestType request) {
        return new BulkModifyRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(Class clazz) {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString(clazz));
        sb.append("  query: ").append(getQuery());
        return sb.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString((Class) null);
    }
    
}
