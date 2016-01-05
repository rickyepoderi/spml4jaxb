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
 *
 * @author ricky
 */
public class SearchQueryAccessor implements Accessor<SearchQueryType, SearchQueryAccessor, SearchQueryBuilder> {
    
    protected SearchQueryType query;
    
    public SearchQueryAccessor(SearchQueryType query) {
        this.query = query;
    }
    
    public ScopeType getScope() {
        return query.getScope();
    }
    
    public boolean isScopePso() {
        return ScopeType.PSO.equals(query.getScope());
    }
    
    public boolean isScopeOneLevel() {
        return ScopeType.ONE_LEVEL.equals(query.getScope());
    }
    
    public boolean isScopeSubTree() {
        return ScopeType.SUB_TREE.equals(query.getScope());
    }
    
    public String getTargetId() {
        return query.getTargetID();
    }
    
    public boolean isTargetId(String targetId) {
        return targetId.equals(query.getTargetID());
    }
    
    public String getBasePsoId() {
        if (query.getBasePsoID() != null) {
            return query.getBasePsoID().getID();
        } else {
            return null;
        }
    }
    
    public String getBasePsoTargetId() {
        if (query.getBasePsoID() != null) {
            return query.getBasePsoID().getTargetID();
        } else {
            return null;
        }
    }
    
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

    @Override
    public SearchQueryType getInternalType() {
        return this.query;
    }

    @Override
    public SearchQueryBuilder toBuilder() {
        return new SearchQueryBuilder(query).dsmlAttributes(this.getDsmlAttributes());
    }

    @Override
    public SearchQueryAccessor asAccessor(SearchQueryType type) {
        return new SearchQueryAccessor(type);
    }
}
