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

import es.rickyepoderi.spml4jaxb.accessor.CloseIteratorResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 CloseIterator response request. The close iterator
 * operation is defined inside the search capability (capability to perform
 * general searches over the objects contained by the repository). A search
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
 * management. The response has no specific properties or data.<p>
 * 
 * @author ricky
 */
public class CloseIteratorResponseBuilder extends ResponseBuilder<ResponseType, CloseIteratorResponseBuilder, CloseIteratorResponseAccessor> {

    /**
     * Constructor for an empty CloseIterator response builder.
     */
    public CloseIteratorResponseBuilder() {
        super(new ResponseType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
        return getSearchObjectFactory().createCloseIteratorResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CloseIteratorResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asCloseIterator();
    }
    
}
