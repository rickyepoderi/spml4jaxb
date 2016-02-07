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

import es.rickyepoderi.spml4jaxb.builder.DeleteResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>Accessor for the SPMLv2 Delete operation response. The delete
 * operation is defined inside the core capability (the core capability
 * should be implemented by any SPMLv2 compatible server and gives basic
 * CRUD methods over the managed objects). The delete operation just deletes
 * a specific object in the repository. The response has no special properties.</p>
 * 
 * @author ricky
 */
public class DeleteResponseAccessor extends BaseResponseAccessor<ResponseType, DeleteResponseAccessor, DeleteResponseBuilder> {

    /**
     * Constructor for an empty delete response accessor.
     */
    protected DeleteResponseAccessor() {
        this(new ResponseType());
    }
    
    /**
     * Constructor for a delete response accessor using the internal type.
     * @param response The internal delete response obtained by JAXB
     */
    protected DeleteResponseAccessor(ResponseType response) {
        super(response, null);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteResponseBuilder toBuilder() {
        return ResponseBuilder.builderForDelete().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteResponseAccessor asAccessor(ResponseType response) {
        return new DeleteResponseAccessor(response);
    }
    
}
