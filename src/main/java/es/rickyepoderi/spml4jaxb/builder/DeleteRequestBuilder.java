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
 *
 * @author ricky
 */
public class DeleteRequestBuilder extends RequestBuilder<DeleteRequestType, DeleteRequestBuilder, DeleteRequestAccessor, DeleteResponseAccessor> {

    protected DeleteRequestBuilder() {
        super(new DeleteRequestType());
    }
    
    public DeleteRequestBuilder recursive(boolean recursive) {
        request.setRecursive(recursive);
        return this;
    }
    
    public DeleteRequestBuilder recursive() {
        return recursive(true);
    }
    
    public DeleteRequestBuilder nonRecursive() {
        return recursive(false);
    }
    
    @Override
    public JAXBElement<DeleteRequestType> build() {
        request.setPsoID(pso);
        return getCoreObjectFactory().createDeleteRequest(request);
    }
    
    @Override
    public DeleteRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asDelete();
    }

    @Override
    public DeleteRequestBuilder fromRequest(DeleteRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    @Override
    public DeleteResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendInternal(client).asDelete();
    }
    
}
