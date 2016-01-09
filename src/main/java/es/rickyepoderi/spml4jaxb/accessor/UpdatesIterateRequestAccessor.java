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

import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.UpdatesIterateRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType;

/**
 *
 * @author ricky
 */
public class UpdatesIterateRequestAccessor extends BaseRequestAccessor<IterateRequestType, UpdatesIterateRequestAccessor, UpdatesIterateRequestBuilder> {

    protected UpdatesIterateRequestAccessor() {
        this(new IterateRequestType());
    }
    
    protected UpdatesIterateRequestAccessor(IterateRequestType request) {
        super(request, null, null);
    }
    
    public String getIteratorId() {
        if (request.getIterator() != null) {
            return request.getIterator().getID();
        } else {
            return null;
        }
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForUpdates();
    }
    
    @Override
    public UpdatesIterateRequestBuilder toBuilder() {
        return RequestBuilder.builderForUpdatesIterate().fromRequest(this.request);
    }

    @Override
    public UpdatesIterateRequestAccessor asAccessor(IterateRequestType request) {
        return new UpdatesIterateRequestAccessor(request);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  iteratorId: ").append(getIteratorId()).append(nl);
        return sb.toString();
    }
    
}
