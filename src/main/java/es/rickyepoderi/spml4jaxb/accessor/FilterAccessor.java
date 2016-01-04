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

import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeDescription;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.AttributeValueAssertion;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.Filter;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.FilterSet;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.SubstringFilter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class FilterAccessor {
    
    protected Filter filter;
    
    protected FilterAccessor(Filter filter) {
        this.filter = filter;
    }
    
    public boolean isEquals() {
        return filter.getEqualityMatch() != null;
    }
    
    public AttributeValueAssertion getEquals() {
        if (isEquals()) {
            return filter.getEqualityMatch();
        } else {
            return null;
        }
    }
    
    public boolean isGreaterOrEqual() {
        return filter.getGreaterOrEqual() != null;
    }
    
    public AttributeValueAssertion getGreaterOrEqual() {
        if (isGreaterOrEqual()) {
            return filter.getGreaterOrEqual();
        } else {
            return null;
        }
    }
    
    public boolean isLessOrEqual() {
        return filter.getLessOrEqual() != null;
    }
    
    public AttributeValueAssertion getLessOrEqual() {
        if (isLessOrEqual()) {
            return filter.getLessOrEqual();
        } else {
            return null;
        }
    }
    
    public boolean isApproxMatch() {
        return filter.getApproxMatch() != null;
    }
    
    public AttributeValueAssertion getApproxMatch() {
        if (isApproxMatch()) {
            return filter.getApproxMatch();
        } else {
            return null;
        }
    }
    
    public boolean isPresent() {
        return filter.getPresent()!= null;
    }
    
    public AttributeDescription getPresent() {
        if (isPresent()) {
            return filter.getPresent();
        } else {
            return null;
        }
    }
    
    public boolean isStartsWith() {
        return filter.getSubstrings() != null && filter.getSubstrings().getInitial() != null;
    }
    
    public AttributeValueAssertion getStartsWith() {
        if (isStartsWith()) {
            AttributeValueAssertion ava = new AttributeValueAssertion();
            ava.setName(filter.getSubstrings().getName());
            ava.setValue(filter.getSubstrings().getInitial());
            return ava;
        } else {
            return null;
        }
    }
    
    public boolean isEndsWith() {
        return filter.getSubstrings() != null && filter.getSubstrings().getFinal() != null;
    }
    
    public AttributeValueAssertion getEndsWith() {
        if (isEndsWith()) {
            AttributeValueAssertion ava = new AttributeValueAssertion();
            ava.setName(filter.getSubstrings().getName());
            ava.setValue(filter.getSubstrings().getFinal());
            return ava;
        } else {
            return null;
        }
    }
    
    public boolean isContains() {
        return filter.getSubstrings() != null && !filter.getSubstrings().getAny().isEmpty();
    }
    
    public AttributeValueAssertion getContains() {
        if (isContains()) {
            AttributeValueAssertion ava = new AttributeValueAssertion();
            ava.setName(filter.getSubstrings().getName());
            ava.setValue(filter.getSubstrings().getAny().get(0));
            return ava;
        } else {
            return null;
        }
    }
    
    public boolean isAnd() {
        return filter.getAnd() != null && !filter.getAnd().getFilterGroup().isEmpty();
    }
    
    public FilterAccessor[] getAnd() {
        if (isAnd()) {
            return getFilterSet(filter.getAnd());
        } else {
            return null;
        }
    }
    
    public boolean isOr() {
        return filter.getOr() != null && !filter.getOr().getFilterGroup().isEmpty();
    }
    
    public FilterAccessor[] getOr() {
        if (isOr()) {
            return getFilterSet(filter.getOr());
        } else {
            return null;
        }
    }
    
    public boolean isNot() {
        return filter.getNot() != null;
    }
    
    public FilterAccessor getNot() {
        if (isNot()) {
            return new FilterAccessor(filter.getNot());
        } else {
            return null;
        }
    }
    
    public FilterAccessor[] getFilterSet(FilterSet fs) {
        List<JAXBElement<?>> list = fs.getFilterGroup();
        List<FilterAccessor> res = new ArrayList<>(list.size());
        for (JAXBElement<?> el: list) {
            if ("urn:oasis:names:tc:DSML:2:0:core".equals(el.getName().getNamespaceURI())) {
                if ("equalityMatch".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setEqualityMatch((AttributeValueAssertion) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("greaterOrEqual".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setGreaterOrEqual((AttributeValueAssertion) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("lessOrEqual".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setLessOrEqual((AttributeValueAssertion) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("approxMatch".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setApproxMatch((AttributeValueAssertion) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("present".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setPresent((AttributeDescription) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("substrings".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setSubstrings((SubstringFilter) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("and".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setAnd((FilterSet) el.getValue());
                    res.add(new FilterAccessor(f));
                } else if ("or".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setOr((FilterSet) el.getValue());
                    res.add(new FilterAccessor(f));
                }  else if ("not".equals(el.getName().getLocalPart())) {
                    Filter f = new Filter();
                    f.setNot((Filter) el.getValue());
                    res.add(new FilterAccessor(f));
                }
            }
        }
        return res.toArray(new FilterAccessor[0]);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isEquals()) {
            AttributeValueAssertion a = getEquals();
            sb.append("(").append(a.getName()).append("=").append(a.getValue()).append(")");
        } else if (isGreaterOrEqual()) {
            AttributeValueAssertion a = getGreaterOrEqual();
            sb.append("(").append(a.getName()).append(">=").append(a.getValue()).append(")");
        } else if (isLessOrEqual()) {
            AttributeValueAssertion a = getLessOrEqual();
            sb.append("(").append(a.getName()).append("<=").append(a.getValue()).append(")");
        } else if (isApproxMatch()) {
            AttributeValueAssertion a = getApproxMatch();
            sb.append("(").append(a.getName()).append("=~").append(a.getValue()).append(")");
        } else if (isPresent()) {
            AttributeDescription a = getPresent();
            sb.append("(").append(a.getName()).append("=*").append(")");
        } else if (isStartsWith()) {
            AttributeValueAssertion a = getStartsWith();
            sb.append("(").append(a.getName()).append("=").append(a.getValue()).append("*)");
        } else if (isEndsWith()) {
            AttributeValueAssertion a = getEndsWith();
            sb.append("(").append(a.getName()).append("=*").append(a.getValue()).append(")");
        } else if (isContains()) {
            AttributeValueAssertion a = getContains();
            sb.append("(").append(a.getName()).append("=*").append(a.getValue()).append("*)");
        } else if (isNot()) {
            sb.append("(!").append(getNot()).append(")");
        } else if (isAnd()) {
            sb.append("(&");
            for (FilterAccessor f: getAnd()) {
                sb.append(f);
            }
            sb.append(")");
        } else if (isOr()) {
            sb.append("(|");
            for (FilterAccessor f: getOr()) {
                sb.append(f);
            }
            sb.append(")");
        } else {
            throw new IllegalStateException("Invalid filter!!!");
        }
        return sb.toString();
    }
}
