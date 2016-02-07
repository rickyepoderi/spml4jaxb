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

import es.rickyepoderi.spml4jaxb.accessor.DeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Delete operation response. The delete
 * operation is defined inside the core capability (the core capability
 * should be implemented by any SPMLv2 compatible server and gives basic
 * CRUD methods over the managed objects). The delete operation just deletes
 * a specific object in the repository. The response has no special properties.</p>
 * 
 * @author ricky
 */
public class DeleteResponseBuilder extends ResponseBuilder<ResponseType, DeleteResponseBuilder, DeleteResponseAccessor> {

    /**
     * Constructor for an empty delete response builder.
     */
    protected DeleteResponseBuilder() {
        super(new ResponseType());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
        return getCoreObjectFactory().createDeleteResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asDelete();
    }
    
}
