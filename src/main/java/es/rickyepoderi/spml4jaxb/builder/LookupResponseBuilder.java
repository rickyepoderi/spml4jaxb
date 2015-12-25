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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.LookupResponseType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class LookupResponseBuilder extends ResponseBuilder<LookupResponseType, LookupResponseBuilder> {

    protected LookupResponseBuilder() {
        super(new LookupResponseType());
    }
    
    @Override
    public JAXBElement<LookupResponseType> build() {
        response.setPso(pso);
        return getCoreObjectFactory().createLookupResponse(response);
    }
    
    @Override
    public ResponseAccessor asAccessor() {
        response.setPso(pso);
        return super.asAccessor().asLookup();
    }
}
