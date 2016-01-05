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

import es.rickyepoderi.spml4jaxb.builder.PsoIdentifierBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;

/**
 *
 * @author ricky
 */
public class PsoIdentifierAccessor implements Accessor<PSOIdentifierType, PsoIdentifierAccessor, PsoIdentifierBuilder> {
    
    protected PSOIdentifierType psoId;
    
    public PsoIdentifierAccessor(PSOIdentifierType psoId) {
        this.psoId = psoId;
    }
    
    public PsoIdentifierAccessor getContainer() {
        if (psoId.getContainerID() != null) {
            return new PsoIdentifierAccessor(psoId.getContainerID());
        } else {
            return null;
        }
    }
    
    public String getId() {
        return psoId.getID();
    }
    
    public String getTargetId() {
        return psoId.getTargetID();
    }

    @Override
    public PSOIdentifierType getInternalType() {
        return psoId;
    }

    @Override
    public PsoIdentifierBuilder toBuilder() {
        return new PsoIdentifierBuilder(psoId);
    }

    @Override
    public PsoIdentifierAccessor asAccessor(PSOIdentifierType type) {
        return new PsoIdentifierAccessor(type);
    }
    
}
