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

import es.rickyepoderi.spml4jaxb.builder.ListTargetsRequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType;

/**
 *
 * @author ricky
 */
public class ListTargetsRequestAccessor extends RequestAccessor<ListTargetsRequestType> {

    protected ListTargetsRequestAccessor(ListTargetsRequestType request) {
        super(request, null, null);
    }
    
    public String getProfile() {
        return request.getProfile();
    }
    
    public boolean isDsml() {
        return ListTargetsRequestBuilder.DSML_PROFILE_URI.equals(request.getProfile());
    }
    
    public boolean isXsd() {
        return ListTargetsRequestBuilder.XSD_PROFILE_URI.equals(request.getProfile());
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForListTargets();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        return sb.append(super.toString())
                .append("  profile: ").append(getProfile()).append(nl).toString();
    }
    
}
