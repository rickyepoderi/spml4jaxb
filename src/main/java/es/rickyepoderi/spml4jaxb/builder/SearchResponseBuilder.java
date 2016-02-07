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
 * <p>Builoder for the SPMLv2 Search response. The search
 * operation is defined inside the search capability (capability to perform
 * general searches over the objects contained in the repository). A search
 * (as defined by the standard) can sent the result objects in several 
 * pages (the idea in the standard is limiting the number of objects returned
 * in a single response). For that the search operation can return an 
 * iterator, this iterator is a mark that the client can use to request
 * the following pages of the same search.</p>
 * 
 * <p>The search operation returns the first page and an iterator id. The client 
 * requests the second page using the Iterate request with the previous received 
 * id. This procedure is repeated as many times as pages needed for that search. 
 * Finally the CloseIterator is used to close the current search. This operation 
 * is used by the server to free any resources related to the iterator / search
 * management.</p>
 * 
 * <p>The search response builder re-uses the pso methods of the base buidler
 * but introduces a nextPso method to advance to next object to set. So the
 * builder constructs the PSOType like in add, modify or any other method that
 * returns a PSOType but then the <em>nextPso</em> is called and another
 * PSOType could be added.</p>
 * 
 * @author ricky
 */
public class SearchResponseBuilder extends ResponseBuilder<SearchResponseType, SearchResponseBuilder, SearchResponseAccessor> {

    /**
     * Constructor for a new search response builder.
     */
    public SearchResponseBuilder() {
        super(new SearchResponseType());
    }
    
    /**
     * Setter for the iterator id in case this page was not the only one.
     * @param id The iterator id for requesting the next page of the current search
     * @return The same builder
     */
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
     * Calling this method the pso is added to the list and reseted, ready to
     * add another pso to the list of responses.
     * 
     * @return The same builder
     */
    public SearchResponseBuilder nextPso() {
        if (pso != null) {
            response.getPso().add(pso);
            pso = null;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<SearchResponseType> build() {
        nextPso();
        return getSearchObjectFactory().createSearchResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SearchResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asSearch();
    }
    
}
