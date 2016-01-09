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

import es.rickyepoderi.spml4jaxb.accessor.ListTargetsRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ListTargetsRequestBuilder extends RequestBuilder<ListTargetsRequestType, ListTargetsRequestBuilder, ListTargetsRequestAccessor> {
    
    public ListTargetsRequestBuilder() {
        super(new ListTargetsRequestType());
    }
    
    public ListTargetsRequestBuilder profileDsml() {
        request.setProfile(DSML_PROFILE_URI);
        return this;
    }
    
    public ListTargetsRequestBuilder profileXsd() {
        request.setProfile(XSD_PROFILE_URI);
        return this;
    }
    
    @Override
    public JAXBElement<ListTargetsRequestType> build() {
        return getCoreObjectFactory().createListTargetsRequest(request);
    }
    
    @Override
    public ListTargetsRequestAccessor asAccessor() {
        return BaseRequestAccessor.accessorForRequest(request).asListTargets();
    }

    @Override
    public ListTargetsRequestBuilder fromRequest(ListTargetsRequestType request) {
        this.request = request;
        return this;
    }
    
}
