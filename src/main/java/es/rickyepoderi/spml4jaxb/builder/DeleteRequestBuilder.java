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

import es.rickyepoderi.spml4jaxb.accessor.DeleteRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.DeleteResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.core.DeleteRequestType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Delete operation request. The delete
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
public class DeleteRequestBuilder extends RequestBuilder<DeleteRequestType, DeleteRequestBuilder, DeleteRequestAccessor, DeleteResponseAccessor> {

    /**
     * Constructor for an empty delete request builder.
     */
    protected DeleteRequestBuilder() {
        super(new DeleteRequestType());
    }
    
    /**
     * Setter for the recursive property. This property let the client to delete
     * a container with children objects.
     * @param recursive The recursive property yo use in the delete request
     * @return The smae builder
     */
    public DeleteRequestBuilder recursive(boolean recursive) {
        request.setRecursive(recursive);
        return this;
    }
    
    /**
     * Sets the recursive property to true.
     * @return The same builder
     */
    public DeleteRequestBuilder recursive() {
        return recursive(true);
    }
    
    /**
     * Sets the recursive property to false (default by standard).
     * @return The same builder
     */
    public DeleteRequestBuilder nonRecursive() {
        return recursive(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<DeleteRequestType> build() {
        request.setPsoID(pso);
        return getCoreObjectFactory().createDeleteRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteRequestBuilder fromRequest(DeleteRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asDelete();
    }
    
}
