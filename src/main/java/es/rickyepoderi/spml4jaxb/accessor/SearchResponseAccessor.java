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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SearchResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.PSOType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchResponseType;
import java.util.Iterator;

/**
 *
 * @author ricky
 */
public class SearchResponseAccessor extends ResponseAccessor<SearchResponseType, SearchResponseBuilder> 
        implements Iterator<SearchResponseAccessor>, Iterable<SearchResponseAccessor> {

    protected Iterator<PSOType> iterator;
    
    protected SearchResponseAccessor(SearchResponseType response) {
        super(response, null);
        start();
    }
    
    public String getIteratorId() {
        if (response.getIterator() != null) {
            return response.getIterator().getID();
        } else {
            return null;
        }
    }
    
    public void start() {
        iterator = response.getPso().iterator();
    }
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    
    @Override
    public SearchResponseAccessor next() {
        if (iterator.hasNext()) {
            pso = iterator.next();
        } else {
            pso = null;
        }
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Unmodifiable iterator");
    }

    @Override
    public Iterator<SearchResponseAccessor> iterator() {
        start();
        return this;
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  iteratorId: ").append(getIteratorId()).append(nl);
        sb.append("  returnedData: ").append(response.getPso().size()).append(nl);
        return sb.toString();
    }
    
    @Override
    public SearchResponseBuilder toBuilder() {
        return ResponseBuilder.builderForSearch().fromResponse(this.response);
    }
}
