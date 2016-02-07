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

import es.rickyepoderi.spml4jaxb.accessor.AddResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.AddResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the Add response. The Add request is a function in the core
 * capability as defined in SPMLv2. This method adds / creates a new PSO inside
 * the repository. The builder let the developer to easily create the
 * object to add using DSML attributes of XSD objects. The class is very short
 * cos the parent class already contains methods for constructing the PSO
 * returned and the data.</p>
 * 
 * @author ricky
 */
public class AddResponseBuilder extends ResponseBuilder<AddResponseType, AddResponseBuilder, AddResponseAccessor> {
    
    /**
     * Constructor for the empty add response builder.
     */
    protected AddResponseBuilder() {
        super(new AddResponseType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<AddResponseType> build() {
        response.setPso(pso);
        return getCoreObjectFactory().createAddResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AddResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asAdd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddResponseBuilder fromResponse(AddResponseType response) {
        this.response = response;
        this.pso = response.getPso();
        return this;
    }
    
}
