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

import es.rickyepoderi.spml4jaxb.accessor.ModifyResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the modify response. The modify operation is a function in the core
 * capability as defined in SPMLv2. This method modifies an existing object from
 * the target. The response has no special properties because it just manages 
 * the return data whose methods are inherited from the base response builder.</p>
 * 
 * @author ricky
 */
public class ModifyResponseBuilder extends ResponseBuilder<ModifyResponseType, ModifyResponseBuilder, ModifyResponseAccessor> {

    /**
     * Constructor for an empty modify response builder.
     */
    protected ModifyResponseBuilder() {
        super(new ModifyResponseType());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ModifyResponseType> build() {
        response.setPso(pso);
        return getCoreObjectFactory().createModifyResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asModify();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ModifyResponseBuilder fromResponse(ModifyResponseType response) {
        this.response = response;
        this.pso = response.getPso();
        return this;
    }
    
}