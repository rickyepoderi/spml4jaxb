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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.msg.bulk.BulkModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class BulkModifyRequestAccessor extends ModificationRequestAccessor<BulkModifyRequestType> { 

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

    @Override
    public ModificationType[] getModifications() {
        return request.getModification().toArray(new ModificationType[0]);
    }
    
    protected BulkModifyRequestAccessor(BulkModifyRequestType request) {
        super(request, null, null);
    }

    public SearchQueryAccessor getQuery() {
        SearchQueryType query = request.getQuery();
        if (query != null) {
            return new SearchQueryAccessor(query);
        } else {
            return null;
        }
    }
    
    @Override
    public String toString(Class clazz) {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  query: ").append(getQuery());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toString((Class) null);
    }
    
}
