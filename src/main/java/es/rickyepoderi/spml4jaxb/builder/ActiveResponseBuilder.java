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

import es.rickyepoderi.spml4jaxb.accessor.ActiveResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder fo the SPMLv2 Active operation response. The active operation
 * is defined inside the suspend capability (capability to enable, disable
 * and know the status of an object in the repository). The Active operation
 * id the one to know the status (enabled/active or disabled/unactive). The
 * response only has a specific attribute <em>isActive</em> to identify
 * if the requested PSO is currently active / enabled.</p>
 * 
 * @author ricky
 */
public class ActiveResponseBuilder extends ResponseBuilder<ActiveResponseType, ActiveResponseBuilder, ActiveResponseAccessor> {
    
    /**
     * constructor for an empty active response buidler.
     */
    protected ActiveResponseBuilder() {
        super(new ActiveResponseType());
    }
    
    /**
     * Sets the active property.
     * @param active the new status of the PSO
     * @return The same builder
     */
    public ActiveResponseBuilder active(boolean active) {
        response.setActive(active);
        return this;
    }
    
    /**
     * Sets the active property to false.
     * @return The same builder
     */
    public ActiveResponseBuilder active() {
        return this.active(true);
    }
    
    /**
     * Sets the active property to false.
     * @return The same builder
     */
    public ActiveResponseBuilder notActive() {
        return this.active(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ActiveResponseType> build() {
        return getSuspendObjectFactory().createActiveResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asActive();
    }
}
