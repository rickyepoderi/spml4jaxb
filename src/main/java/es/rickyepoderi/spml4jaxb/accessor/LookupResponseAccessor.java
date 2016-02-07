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

import es.rickyepoderi.spml4jaxb.builder.LookupResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.LookupResponseType;

/**
 * <p>Accessor for the Lookup response. The Lookup operation is a function in the core
 * capability as defined in SPMLv2. This method retrieves a new object from
 * the target. The accessor gives methods to access the object attributes in
 * the DSML profile and the object itself in XSD. Those methods are derived
 * from the Base parent class.</p>
 * 
 * @author ricky
 */
public class LookupResponseAccessor extends BaseResponseAccessor<LookupResponseType, LookupResponseAccessor, LookupResponseBuilder> {

    /**
     * Constructor for an empty lookup response accessor.
     */
    protected LookupResponseAccessor() {
        this(new LookupResponseType());
    }
    
    /**
     * Constructor for a lookup response accessor giving the internal type.
     * @param response The lookup response type generated from JAXB
     */
    protected LookupResponseAccessor(LookupResponseType response) {
        super(response, response.getPso());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LookupResponseBuilder toBuilder() {
        return ResponseBuilder.builderForLookup().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LookupResponseAccessor asAccessor(LookupResponseType response) {
        return new LookupResponseAccessor(response);
    }
    
}
