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
import es.rickyepoderi.spml4jaxb.builder.UpdatesCloseIteratorResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>Accessor for the SPMLv2 Updates CloseIterator response. The CloseIterator
 * operation is defined inside the updates capability. The updates capability 
 * allow a requestor to obtain in a scalable manner every recorded update (i.e., 
 * modification to an object) that matches specified selection criteria. The
 * updates operates in the same manner than search, it return the update 
 * records in pages. So again the same technique is used, the updates return
 * the first page of records and, if there are more to come, it appends a
 * iterator identifier in the response. With that id the requestor can
 * request the next page (Iterate operation). If it is needed a third page
 * another iterator id is used in the second page. Finally this CloseIterator operation
 * in this capability frees any resources in the server related to the updates
 * operation.</p>
 * 
 * <p>The response has no special properties to manage.</p>
 * 
 * @author ricky
 */
public class UpdatesCloseIteratorResponseAccessor extends BaseResponseAccessor<ResponseType, UpdatesCloseIteratorResponseAccessor, UpdatesCloseIteratorResponseBuilder> {
    
    /**
     * Constructor for an updates close iterator response accessor.
     */
    protected UpdatesCloseIteratorResponseAccessor() {
        this(new ResponseType());
    }
    
    /**
     * Constructor for an updates close iterator response accessor using the internal type.
     * @param response The close iterator type as defined in the standard and obtained via JAXB
     */
    protected UpdatesCloseIteratorResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorResponseBuilder toBuilder() {
        return ResponseBuilder.builderForUpdatesCloseIterator().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesCloseIteratorResponseAccessor asAccessor(ResponseType response) {
        return new UpdatesCloseIteratorResponseAccessor(response);
    }
}
