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

import es.rickyepoderi.spml4jaxb.accessor.PsoIdentifierAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;

/**
 *
 * @author ricky
 */
public class PsoIdentifierBuilder implements Builder<PSOIdentifierType, PsoIdentifierAccessor> {

    protected PSOIdentifierType psoId = null;
    
    protected PsoIdentifierBuilder() {
        this.psoId = new PSOIdentifierType();
    }
    
    public PsoIdentifierBuilder(PSOIdentifierType psoId) {
        this.psoId = psoId;
    }
    
    public PsoIdentifierBuilder id(String id) {
        psoId.setID(id);
        return this;
    }
    
    public PsoIdentifierBuilder targetId(String targetId) {
        psoId.setTargetID(targetId);
        return this;
    }
    
    public PsoIdentifierBuilder container(PsoIdentifierBuilder parent) {
        psoId.setContainerID(parent.build());
        return this;
    }
    
    @Override
    public PSOIdentifierType build() {
        return psoId;
    }

    @Override
    public PsoIdentifierAccessor asAccessor() {
        return new PsoIdentifierAccessor(psoId);
    }
    
}
