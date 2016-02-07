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
 * <p>Accessor for the SPMLv2 Search response. The search
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
 * <p>The search response implements the Iterable and Iterors interfaces so it can
 * be iterated in a for loop. Besides the inherited PSOType object is used to 
 * access to the current object in the iteration.</p>
 * 
 * @author ricky
 */
public class SearchResponseAccessor extends BaseResponseAccessor<SearchResponseType, SearchResponseAccessor, SearchResponseBuilder> 
        implements Iterator<SearchResponseAccessor>, Iterable<SearchResponseAccessor> {

    /**
     * The iterator to handle the PSOType list.
     */
    protected Iterator<PSOType> iterator;
    
    /**
     * Constructor for an empty search response accessor.
     */
    protected SearchResponseAccessor() {
        this(new SearchResponseType());
    }
    
    /**
     * Constructor for a search response accessor giving the internal type.
     * @param response The search response type as defined in the standard and obtained by JAXB
     */
    protected SearchResponseAccessor(SearchResponseType response) {
        super(response, null);
        start();
    }
    
    /**
     * Getter for the iterator id if the search is paged and this current
     * response is just a page of that search and there are more pages still
     * to finish.
     * @return The iterator id or null
     */
    public String getIteratorId() {
        if (response.getIterator() != null) {
            return response.getIterator().getID();
        } else {
            return null;
        }
    }
    
    /**
     * Starts and initializes the iterator back to the beginning of the list
     * of PSO types. This method is called in the constructor that receives
     * a real search type. So the response is already started.
     */
    public void start() {
        iterator = response.getPso().iterator();
    }
    
    /**
     * Iterator method to know if there are still more element to iterate.
     * @return true if there are atill more elements in the list, false if not
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    
    /**
     * Iterator method to move the cursor of the iterator to the next element.
     * If there no more elements the pso will be null.
     * @return The same accessor but with the pso type updated to the next object
     */
    @Override
    public SearchResponseAccessor next() {
        if (iterator.hasNext()) {
            pso = iterator.next();
        } else {
            pso = null;
        }
        return this;
    }

    /**
     * Iterator method to delete the current object but this is a unmodifiable
     * iterator so it throws and exception (UnsupportedOperationException).
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Unmodifiable iterator");
    }

    /**
     * Iterable method that returns the iterator to loop. As the same search
     * response is an iterator it returns itself but starting again the
     * iterator to the beginning.
     * @return The same search response initialized to the first object
     */
    @Override
    public Iterator<SearchResponseAccessor> iterator() {
        start();
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SearchResponseBuilder toBuilder() {
        return ResponseBuilder.builderForSearch().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchResponseAccessor asAccessor(SearchResponseType response) {
        return new SearchResponseAccessor(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  iteratorId: ").append(getIteratorId()).append(nl);
        sb.append("  returnedData: ").append(response.getPso().size()).append(nl);
        return sb.toString();
    }
}
