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
 * <p>Builder for a PSOIdentifierType which is the object defined in the SPMLv2
 * standard to identify a PSO (Provisioning Service Object) in the target. 
 * Usually any builder has direct method to assign the identifier without the 
 * need of a particular builder like this (methods psoId or psoTargetId to set 
 * the identifier string and the target identifier id of the object). 
 * Nevertheless the standard also let you give a hierarchical structure to 
 * identifier, so the an object identifier can have a parent identifier. In 
 * those cases using a special accessor / builder is needed.</p>
 * 
 * @author ricky
 */
public class PsoIdentifierBuilder implements Builder<PSOIdentifierType, PsoIdentifierAccessor> {

    /**
     * The internal PSOIdentifierType wrapped by the builder.
     */
    protected PSOIdentifierType psoId = null;
    
    /**
     * Constructor for an empty PSO identifier.
     */
    protected PsoIdentifierBuilder() {
        this.psoId = new PSOIdentifierType();
    }
    
    /**
     * Constructor for a PSO identifier giving the internal type.
     * @param psoId The PSOIdentifierType as generated by JAXB
     */
    public PsoIdentifierBuilder(PSOIdentifierType psoId) {
        this.psoId = psoId;
    }
    
    /**
     * Setter for the id of the identifier.
     * @param id The new identifier
     * @return The same builder
     */
    public PsoIdentifierBuilder id(String id) {
        psoId.setID(id);
        return this;
    }
    
    /**
     * Setter for the target id.
     * @param targetId The new target id
     * @return The same builder
     */
    public PsoIdentifierBuilder targetId(String targetId) {
        psoId.setTargetID(targetId);
        return this;
    }
    
    /**
     * Setter for the container or parent identifier to construct the hierarchy,
     * @param parent The parant builder to set as the container of the current
     * @return The same builder
     */
    public PsoIdentifierBuilder container(PsoIdentifierBuilder parent) {
        psoId.setContainerID(parent.build());
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PSOIdentifierType build() {
        return psoId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PsoIdentifierAccessor asAccessor() {
        return new PsoIdentifierAccessor(psoId);
    }
    
}