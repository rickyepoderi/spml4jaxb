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

import es.rickyepoderi.spml4jaxb.accessor.LookupResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.LookupResponseType;
import javax.xml.bind.JAXBElement;

/**
  * <p>Builder for the Lookup response. The Lookup operation is a function in the core
 * capability as defined in SPMLv2. This method retrieves a new object from
 * the target. The builder gives methods to create the object attributes in
 * the DSML profile and the object itself in XSD. Those methods are derived
 * from the Base parent class.</p>
 * 
 * @author ricky
 */
public class LookupResponseBuilder extends ResponseBuilder<LookupResponseType, LookupResponseBuilder, LookupResponseAccessor> {

    /**
     * Constructor for an empty lookup response builder.
     */
    protected LookupResponseBuilder() {
        super(new LookupResponseType());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<LookupResponseType> build() {
        response.setPso(pso);
        return getCoreObjectFactory().createLookupResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LookupResponseAccessor asAccessor() {
        response.setPso(pso);
        return BaseResponseAccessor.accessorForResponse(response).asLookup();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LookupResponseBuilder fromResponse(LookupResponseType response) {
        this.response = response;
        this.pso = response.getPso();
        return this;
    }
}