/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
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
import es.rickyepoderi.spml4jaxb.msg.core.DeleteRequestType;

/**
 *
 * @author ricky
 */
public class DeleteRequestAccessor extends RequestAccessor<DeleteRequestType>{ 

    protected DeleteRequestAccessor(DeleteRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    public Boolean isRecursive() {
        return request.isRecursive();
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForDelete();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        return sb.append(super.toString())
                .append("  recursive: ").append(isRecursive()).append(nl).toString();
    }
}
