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

import es.rickyepoderi.spml4jaxb.builder.AddResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.AddResponseType;

/**
 * <p>Accessor for the Add response. The Add operation is a function in the core
 * capability as defined in SPMLv2. This method adds / creates a new PSO inside
 * the repository. The accessor lets the developer to easily retrieve the
 * object added using DSML attributes of XSD objects. The class is very short
 * cos the parent class already contains methods for accessing the PSO
 * returned and the data.</p>
 * 
 * @author ricky
 */
public class AddResponseAccessor extends BaseResponseAccessor<AddResponseType, AddResponseAccessor, AddResponseBuilder> {

    /**
     * Constructor for an empty add response accessor.
     */
    protected AddResponseAccessor() {
        this(new AddResponseType());
    }
    
    /**
     * Constructor for a specific response type.
     * @param response The add response type obtained by JAXB
     */
    protected AddResponseAccessor(AddResponseType response) {
        super(response, response.getPso());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AddResponseAccessor asAccessor(AddResponseType response) {
        return new AddResponseAccessor(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddResponseBuilder toBuilder() {
        return ResponseBuilder.builderForAdd().fromResponse(this.response);
    }
    
}
