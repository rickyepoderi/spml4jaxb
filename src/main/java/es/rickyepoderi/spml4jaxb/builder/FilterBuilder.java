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

import es.rickyepoderi.spml4jaxb.accessor.FilterAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescription;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeValueAssertion;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.Filter;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.FilterSet;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.SubstringFilter;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class FilterBuilder implements Builder<JAXBElement<Filter>> {
    
    protected Filter filter = null;
    
    protected FilterBuilder(Filter filter) {
        this.filter = filter;
    }
    
    static public FilterBuilder equals(String name, String value) {
        Filter f = new Filter();
        AttributeValueAssertion ava = new AttributeValueAssertion();
        ava.setName(name);
        ava.setValue(value);
        f.setEqualityMatch(ava);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder greaterOrEqual(String name, String value) {
        Filter f = new Filter();
        AttributeValueAssertion ava = new AttributeValueAssertion();
        ava.setName(name);
        ava.setValue(value);
        f.setGreaterOrEqual(ava);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder lessOrEqual(String name, String value) {
        Filter f = new Filter();
        AttributeValueAssertion ava = new AttributeValueAssertion();
        ava.setName(name);
        ava.setValue(value);
        f.setLessOrEqual(ava);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder approxMatch(String name, String value) {
        Filter f = new Filter();
        AttributeValueAssertion ava = new AttributeValueAssertion();
        ava.setName(name);
        ava.setValue(value);
        f.setApproxMatch(ava);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder present(String name) {
        Filter f = new Filter();
        AttributeDescription ad = new AttributeDescription();
        ad.setName(name);
        f.setPresent(ad);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder startsWith(String name, String value) {
        Filter f = new Filter();
        SubstringFilter sf = new SubstringFilter();
        sf.setName(name);
        sf.setInitial(value);
        f.setSubstrings(sf);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder endsWith(String name, String value) {
        Filter f = new Filter();
        SubstringFilter sf = new SubstringFilter();
        sf.setName(name);
        sf.setFinal(value);
        f.setSubstrings(sf);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder contains(String name, String value) {
        Filter f = new Filter();
        SubstringFilter sf = new SubstringFilter();
        sf.setName(name);
        sf.getAny().add(value);
        f.setSubstrings(sf);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder and(FilterBuilder... filter) {
        Filter f = new Filter();
        FilterSet fs = new FilterSet();
        for (FilterBuilder fb: filter) {
            fs.getFilterGroup().add(fb.buildFilterSet());
        }
        f.setAnd(fs);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder or(FilterBuilder... filter) {
        Filter f = new Filter();
        FilterSet fs = new FilterSet();
        for (FilterBuilder fb: filter) {
            fs.getFilterGroup().add(fb.buildFilterSet());
        }
        f.setOr(fs);
        return new FilterBuilder(f);
    }
    
    static public FilterBuilder not(FilterBuilder filter) {
        Filter f = new Filter();
        f.setNot(filter.filter);
        return new FilterBuilder(f);
    }
    
    protected JAXBElement buildFilterSet() {
        FilterAccessor accessor = RequestAccessor.accessorForFilter(filter);
        if (accessor.isEquals()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetEqualityMatch(filter.getEqualityMatch());
        } else if (accessor.isGreaterOrEqual()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetGreaterOrEqual(filter.getGreaterOrEqual());
        } else if (accessor.isLessOrEqual()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetLessOrEqual(filter.getLessOrEqual());
        } else if (accessor.isApproxMatch()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetApproxMatch(filter.getApproxMatch());
        } else if (accessor.isPresent()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetPresent(filter.getPresent());
        } else if (accessor.isStartsWith() || accessor.isEndsWith() || accessor.isContains()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetSubstrings(filter.getSubstrings());
        } else if (accessor.isNot()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetNot(filter.getNot());
        } else if (accessor.isAnd()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetAnd(filter.getAnd());
        } else if (accessor.isOr()) {
            return RequestBuilder.dsmlv2ObjectFactory.createFilterSetOr(filter.getOr());
        } else {
            throw new IllegalStateException("Invalid filter!!!");
        }
    }
    
    @Override
    public JAXBElement<Filter> build() {
        return RequestBuilder.dsmlv2ObjectFactory.createFiler(filter);
    }
    
    public FilterBuilder not() {
        return FilterBuilder.not(this);
    }
    
}
