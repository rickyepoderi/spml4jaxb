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

import es.rickyepoderi.spml4jaxb.accessor.SearchQueryAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescription;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescriptions;
import es.rickyepoderi.spml4jaxb.msg.search.ScopeType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;

/**
 * <p>Builder for a SearchQuery type as defined in the SPMLv2 standard. The
 * SearchQuery type is the standard is used for specifying a filter criteria 
 * to some operations (search, bulk delete or modify, updates). This criteria
 * is different depending the profile was DSML or XSD. The idea is the 
 * builders for operations that involve a search query will always admit
 * a SearchQueryBuilder to set the criteria.</p>
 * 
 * @author ricky
 */
public class SearchQueryBuilder implements Builder<SearchQueryType, SearchQueryAccessor> {
    
    /**
     * The internal search query type as defined in the standard.
     */
    protected SearchQueryType query = null;
    
    /**
     * The list of attribute descriptions to return in a DSML search.
     */
    protected AttributeDescriptions attributes = null;
    
    /**
     * COnstructor for a empty search query builder.
     */
    protected SearchQueryBuilder() {
        query = new SearchQueryType();
        attributes = null;
    }
    
    /**
     * Constructor for a search query builder giving the internal type.
     * @param query The internal search query type as defined in the standard
     */
    public SearchQueryBuilder(SearchQueryType query) {
        this.query = query;
    }
    
    //
    // SCOPE
    //
    
    /**
     * Setter for the scope of the query. SPMLv2 admits a hierarchical (tree-like)
     * structure of the objects in the target. So the query can be delimited to
     * a part of the tree. Three different scopes are managed:
     * 
     * <ul>
     * <li>PSO: Only the base object should be checked</li>
     * <li>ONE LEVEL: Only the children objects of the base should be checked</li>
     * <li>SUBTREE: All objects starting from the base should be checked</li>
     * </ul>
     * 
     * @param scope The scope of the query
     * @return The same builder
     */
    public SearchQueryBuilder scope(ScopeType scope) {
        query.setScope(scope);
        return this;
    }
    
    /**
     * Setter of the scope to PSO.
     * @return The same builder
     */
    public SearchQueryBuilder scopePso() {
        query.setScope(ScopeType.PSO);
        return this;
    }
    
    /**
     * Setter of the scope to ONE LEVEL.
     * @return The same builder
     */
    public SearchQueryBuilder scopeOneLevel() {
        query.setScope(ScopeType.ONE_LEVEL);
        return this;
    }
    
    /**
     * Setter of the scope to SUBTREE.
     * @return The same builder
     */
    public SearchQueryBuilder scopeSubTree() {
        query.setScope(ScopeType.SUB_TREE);
        return this;
    }
    
    //
    // TARGET ID
    //
    
    /**
     * Setter for the target id of the query.
     * @param targetId The target id the query is about
     * @return The same builder
     */
    public SearchQueryBuilder targetId(String targetId) {
        query.setTargetID(targetId);
        return this;
    }
    
    //
    // BASE PSO
    //
    
    /**
     * Setter for the base PSO identifier. SPMLv2 admits a hierarchical 
     * (tree-like) structure of the objects in the target, the base (and the 
     * scope) explains what part of the structure should be affected by the query.
     * If the query specifies a base the search is restricted to that part of
     * the tree depending the scope definition.
     * 
     * @param id The PSO identifier to set
     * @return The same builder
     */
    public SearchQueryBuilder basePsoId(String id) {
        PSOIdentifierType basePso = query.getBasePsoID();
        if (basePso == null) {
            basePso = new PSOIdentifierType();
            query.setBasePsoID(basePso);
        }
        basePso.setID(id);
        return this;
    }
    
    /**
     * Setter for the base PSO target identifier. SPMLv2 admits a hierarchical 
     * (tree-like) structure of the objects in the target, the base (and the 
     * scope) explains what part of the structure should be affected by the query.
     * If the query specifies a base the search is restricted to that part of
     * the tree depending the scope definition.
     * 
     * @param targetId The PSO target identifier to set
     * @return The same builder
     */
    public SearchQueryBuilder basePsoTargetId(String targetId) {
        PSOIdentifierType basePso = query.getBasePsoID();
        if (basePso == null) {
            basePso = new PSOIdentifierType();
            query.setBasePsoID(basePso);
        }
        basePso.setTargetID(targetId);
        return this;
    }
    
    /**
     * Setter for the base PSO target identifier. SPMLv2 admits a hierarchical 
     * (tree-like) structure of the objects in the target, the base (and the 
     * scope) explains what part of the structure should be affected by the query.
     * If the query specifies a base the search is restricted to that part of
     * the tree depending the scope definition.
     * 
     * @param base The base pso identifier builder to set
     * @return The same builder
     */
    public SearchQueryBuilder basePsoIdentifier(PsoIdentifierBuilder base) {
        query.setBasePsoID(base.build());
        return this;
    }
    
    //
    // DSML ATTRIBUTES
    //
    
    /**
     * In a DSML search the query can specify what attributes should be returned
     * for every object (obviously this should be coherent with the <em>returnData</em>
     * property as well).
     * 
     * @param attr The attributes to return for every object
     * @return The same buidler
     */
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
    
    /**
     * In a DSML search the query criteria is specified as a query filter (defined
     * in the DSMLv2 standard). So in a DSML search this method assigns the 
     * Filter built by the builder as the criteria in the search.
     * 
     * @param filter The filter builder to construct the DSML cfriteria
     * @return The same builder
     */
    public SearchQueryBuilder dsmlFilter(FilterBuilder filter) {
        query.getAny().add(filter.build());
        return this;
    }
    
    //
    // XSD SELECTION
    //
    
    /**
     * In an XSD search the criteria is sent via a XPATH expression. So this
     * method assigns the XPATH expression as the criteria in the search.
     * 
     * @param xpath The XPATH criteria to assign
     * @return The same builder
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SearchQueryType build() {
        if (attributes != null) {
            query.getAny().add(RequestBuilder.dsmlv2ObjectFactory.createAttributeDescriptions(attributes));
        }
        return query;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchQueryAccessor asAccessor() {
        build();
        return new SearchQueryAccessor(query);
    }
}
