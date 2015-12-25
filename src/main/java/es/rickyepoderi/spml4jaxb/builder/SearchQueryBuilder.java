/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescription;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescriptions;
import es.rickyepoderi.spml4jaxb.msg.search.ScopeType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;

/**
 *
 * @author ricky
 */
public class SearchQueryBuilder implements Builder<SearchQueryType> {
    
    protected SearchQueryType query = null;
    protected AttributeDescriptions attributes = null;
    
    protected SearchQueryBuilder() {
        query = new SearchQueryType();
        attributes = null;
    }
    
    //
    // SCOPE
    //
    
    public SearchQueryBuilder scope(ScopeType scope) {
        query.setScope(scope);
        return this;
    }
    
    public SearchQueryBuilder scopePso() {
        query.setScope(ScopeType.PSO);
        return this;
    }
    
    public SearchQueryBuilder scopeOneLevel() {
        query.setScope(ScopeType.ONE_LEVEL);
        return this;
    }
    
    public SearchQueryBuilder scopeSubTree() {
        query.setScope(ScopeType.SUB_TREE);
        return this;
    }
    
    //
    // TARGET ID
    //
    
    public SearchQueryBuilder targetId(String targetId) {
        query.setTargetID(targetId);
        return this;
    }
    
    //
    // BASE PSO
    //
    
    public SearchQueryBuilder basePsoId(String id) {
        PSOIdentifierType basePso = query.getBasePsoID();
        if (basePso == null) {
            basePso = new PSOIdentifierType();
            query.setBasePsoID(basePso);
        }
        basePso.setID(id);
        return this;
    }
    
    public SearchQueryBuilder basePsoTargetId(String targetId) {
        PSOIdentifierType basePso = query.getBasePsoID();
        if (basePso == null) {
            basePso = new PSOIdentifierType();
            query.setBasePsoID(basePso);
        }
        basePso.setTargetID(targetId);
        return this;
    }
    
    //
    // DSML ATTRIBUTES
    //
    
    public SearchQueryBuilder dsmlAttributes(String... attr) {
        if (attributes == null) {
            attributes = new AttributeDescriptions();
        }
        for (String a: attr) {
            AttributeDescription desc = new AttributeDescription();
            desc.setName(a);
            attributes.getAttribute().add(desc);
        }
        return this;
    }
    
    //
    // DSML FILTER
    // 
    
    public SearchQueryBuilder dsmlFilter(FilterBuilder filter) {
        query.getAny().add(filter.build());
        return this;
    }
    
    //
    // XSD SELECTION
    //
    
    /**
     * Based on standard: 
     * 3.4.1. Search Request 
     * "The search request can specify a search base and an XPath selection statement."
     * "The select clause for the search request treats each target as a document root that 
     * (directly or indirectly) contains all other objects as nodes"
     * 
     * @param xpath
     * @return 
     */
    public SearchQueryBuilder xsdXPathSelection(String xpath) {
        SelectionType sel = new SelectionType();
        sel.setPath(xpath);
        sel.setNamespaceURI(xpath);
        sel.setNamespaceURI("http://www.w3.org/TR/xpath20");
        query.getAny().add(RequestBuilder.coreObjectFactory.createSelect(sel));
        return this;
    }
    
    //
    // BUILDER
    //
    
    @Override
    public SearchQueryType build() {
        if (attributes != null) {
            query.getAny().add(RequestBuilder.dsmlv2ObjectFactory.createAttributeDescriptions(attributes));
        }
        return query;
    }
}
