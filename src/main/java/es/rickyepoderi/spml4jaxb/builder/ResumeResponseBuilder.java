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

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResumeResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 Resume operation response. The resume
 * operation is defined inside the suspend capability (capability to
 * enable and disable objects in the target). The resume operation
 * is used to enable an object previously disabled (suspended). The resume 
 * response has no special properties.<p>
 * 
 * @author ricky
 */
public class ResumeResponseBuilder extends ResponseBuilder<ResponseType, ResumeResponseBuilder, ResumeResponseAccessor> {
    
    /**
     * Constructor for a new resume response builder.
     */
    protected ResumeResponseBuilder() {
        super(new ResponseType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResponseType> build() {
        return getSuspendObjectFactory().createResumeResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResumeResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asResume();
    }
    
}
