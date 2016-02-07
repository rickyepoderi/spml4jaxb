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

import es.rickyepoderi.spml4jaxb.builder.SearchQueryBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.SelectionType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescription;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescriptions;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.Filter;
import es.rickyepoderi.spml4jaxb.msg.search.ScopeType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 * <p>Accessor for a SearchQuery type as defined in the SPMLv2 standard. The
 * SearchQuery type is the standard is used for specifying a filter criteria 
 * to some operations (search, bulk delete or modify, updates). This criteria
 * is different depending the profile was DSML or XSD. The idea is the 
 * accessors for operations that involve a search query will always return
 * a SearchQueryAccessor to read the criteria.</p>
 * 
 * @author ricky
 */
public class SearchQueryAccessor implements Accessor<SearchQueryType, SearchQueryAccessor, SearchQueryBuilder> {
    
    /**
     * The internal search query type that this accessor wraps.
     */
    protected SearchQueryType query;
    
    /**
     * Constructor using the internal SearchQueryType.
     * @param query The internal search query type as defined in the standard
     */
    public SearchQueryAccessor(SearchQueryType query) {
        this.query = query;
    }
    
    /**
     * Getter for the scope of the query. SPMLv2 admits a hierarchical (tree-like)
     * structure of the objects in the target. So the query can be delimited to
     * a part of the tree. Three different scopes are managed:
     * 
     * <ul>
     * <li>PSO: Only the base object should be checked</li>
     * <li>ONE LEVEL: Only the children objects of the base should be checked</li>
     * <li>SUBTREE: All objects starting from the base should be checked</li>
     * </ul>
     * 
     * @return The scope of the query or null
     */
    public ScopeType getScope() {
        return query.getScope();
    }
    
    /**
     * Checker for the scope of the query
     * @return true if it is PSO, false otherwise
     */
    public boolean isScopePso() {
        return ScopeType.PSO.equals(query.getScope());
    }
    
    /**
     * Checker for the scope of the query
     * @return true if it is ONE LEVEL, false otherwise
     */
    public boolean isScopeOneLevel() {
        return ScopeType.ONE_LEVEL.equals(query.getScope());
    }
    
    /**
     * Checker for the scope of the query
     * @return true if it is SUBTREE, false otherwise
     */
    public boolean isScopeSubTree() {
        return ScopeType.SUB_TREE.equals(query.getScope());
    }
    
    /**
     * Getter for the target identifier the query is about.
     * @return The target identifier of the query or null
     */
    public String getTargetId() {
        return query.getTargetID();
    }
    
    /**
     * Checker for the target identifier.
     * @param targetId The target identifier to compare
     * @return true if the target id in the query is the one passed, false if not
     */
    public boolean isTargetId(String targetId) {
        return targetId.equals(query.getTargetID());
    }
    
    /**
     * Getter for the base PSO identifier. SPMLv2 admits a hierarchical 
     * (tree-like) structure of the objects in the target, the base (and the 
     * scope) explains what part of the structure should be affected by the query.
     * If the query specifies a base the search is restricted to that part of
     * the tree depending the scope definition.
     * 
     * @return The PSO identifier of the base or null
     */
    public String getBasePsoId() {
        if (query.getBasePsoID() != null) {
            return query.getBasePsoID().getID();
        } else {
            return null;
        }
    }
    
    /**
     * Getter for the base PSO identifier. SPMLv2 admits a hierarchical 
     * (tree-like) structure of the objects in the target, the base (and the 
     * scope) explains what part of the structure should be affected by the query.
     * If the query specifies a base the search is restricted to that part of
     * the tree depending the scope definition.
     * 
     * @return The PSO target id of the base or null
     */
    public String getBasePsoTargetId() {
        if (query.getBasePsoID() != null) {
            return query.getBasePsoID().getTargetID();
        } else {
            return null;
        }
    }
    
    /**
     * Getter for the base PSO identifier. SPMLv2 admits a hierarchical 
     * (tree-like) structure of the objects in the target, the base (and the 
     * scope) explains what part of the structure should be affected by the query.
     * If the query specifies a base the search is restricted to that part of
     * the tree depending the scope definition.
     * 
     * @return The accessor for the base PSO id or null
     */
    public PsoIdentifierAccessor getBaseAccessor() {
        if (query.getBasePsoID() != null) {
            return new PsoIdentifierAccessor(query.getBasePsoID());
        } else {
            return null;
        }
    }
    
    /**
     * In a DSML search the query can specify what attributes should be returned
     * for every object (obviously this should be coherent with the <em>returnData</em>
     * property as well).
     * 
     * @return The array of attribute to return or the empty array
     */
    public String[] getDsmlAttributes() {
        List<Object> l = query.getAny();
        List<AttributeDescription> descs = null;
        for (Object o : l) {
            if (o instanceof AttributeDescriptions) {
                descs = ((AttributeDescriptions) o).getAttribute();
                break;
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof AttributeDescriptions) {
                    descs = ((AttributeDescriptions) el.getValue()).getAttribute();
                    break;
                }
            }
        }
        if (descs != null) {
            List<String> res = new ArrayList<>(descs.size());
            for (AttributeDescription desc: descs) {
                if (desc.getName() != null) {
                    res.add(desc.getName());
                }
            }
            return res.toArray(new String[0]);
        } else  {
            return new String[]{};
        }
    }
    
    /**
     * In a DSML search the query criteria is specified as a query filter (defined
     * in the DSMLv2 standard). So in a DSML search this method returns the 
     * Filter accessor to interrogate the attribute search conditions established
     * in the search.
     * 
     * @return The DSML query filter or null
     */
    public FilterAccessor getQueryFilter() {
        List<Object> l = query.getAny();
        for (Object o : l) {
            if (o instanceof Filter) {
                return new FilterAccessor((Filter) o);
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof Filter) {
                    return new FilterAccessor((Filter) el.getValue());
                }
            }
        }
        return null;
    }
    
    /**
     * In an XSD search the criteria is sent via a XPATH expression. So this
     * method return the XPATH expression set in the request.
     * 
     * @return The XPATH expression or null
     */
    public String getXsdXPathSelection() {
        for (Object o: query.getAny()) {
            if (o instanceof SelectionType) {
                return ((SelectionType) o).getPath();
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof SelectionType) {
                    return ((SelectionType) el.getValue()).getPath();
                }
            }
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        FilterAccessor filter = getQueryFilter();
        if (filter != null) {
            sb.append("  filter: ").append(filter).append(nl);
        }
        String xpath = getXsdXPathSelection();
        if (xpath != null) {
            sb.append("  xpath: ").append(getXsdXPathSelection()).append(nl);
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchQueryType getInternalType() {
        return this.query;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchQueryBuilder toBuilder() {
        return new SearchQueryBuilder(query).dsmlAttributes(this.getDsmlAttributes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchQueryAccessor asAccessor(SearchQueryType type) {
        return new SearchQueryAccessor(type);
    }
}
