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

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SearchResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.search.ResultsIteratorType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchResponseType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class SearchResponseBuilder extends ResponseBuilder<SearchResponseType, SearchResponseBuilder, SearchResponseAccessor> {

    public SearchResponseBuilder() {
        super(new SearchResponseType());
    }
    
    public SearchResponseBuilder iteratorId(String id) {
        ResultsIteratorType iter = response.getIterator();
        if (iter == null) {
            iter = new ResultsIteratorType();
            response.setIterator(iter);
        }
        iter.setID(id);
        return this;
    }
    
    /**
     * In this request the pso attribute is used to add the current pso data.
     * Calling this method the pso is added to the list and reseted.
     * @return The same builder
     */
    public SearchResponseBuilder nextPso() {
        if (pso != null) {
            response.getPso().add(pso);
            pso = null;
        }
        return this;
    }

    @Override
    public JAXBElement<SearchResponseType> build() {
        nextPso();
        return getSearchObjectFactory().createSearchResponse(response);
    }
    
    @Override
    public SearchResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asSearch();
    }
    
}
