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

import es.rickyepoderi.spml4jaxb.builder.DeleteRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.DeleteRequestType;

/**
 * <p>Accessor for the SPMLv2 Delete operation request. The delete
 * operation is defined inside the core capability (the core capability
 * should be implemented by any SPMLv2 compatible server and gives basic
 * CRUD methods over the managed objects). The delete operation just deletes
 * a specific object in the repository. It only manages the PSO identifier
 * of the object to delete and a recursive property. But the former was 
 * already contained in the base class so only recursive property
 * is specifically managed by this class.</p>
 * 
 * @author ricky
 */
public class DeleteRequestAccessor extends BaseRequestAccessor<DeleteRequestType, DeleteRequestAccessor, DeleteRequestBuilder>{ 

    /**
     * Constructor for an empty delete request accessor.
     */
    protected DeleteRequestAccessor() {
        this(new DeleteRequestType());
    }
    
    /**
     * Constructor for a delete request accessor using the internal type.
     * @param request The internal delete request type as obtained by the JAXB parsing
     */
    protected DeleteRequestAccessor(DeleteRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    /**
     * Getter of the recursive property. This property is used to let the 
     * deletion of contained objects (if the object to delete is a container
     * of other objects).
     * 
     * @return true if recursive delete is allowed, false if not
     */
    public Boolean isRecursive() {
        return request.isRecursive();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForDelete();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteRequestBuilder toBuilder() {
        return RequestBuilder.builderForDelete().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteRequestAccessor asAccessor(DeleteRequestType request) {
        return new DeleteRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        return sb.append(super.toString())
                .append("  recursive: ").append(isRecursive()).append(nl).toString();
    }
}
