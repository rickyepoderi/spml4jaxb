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

import es.rickyepoderi.spml4jaxb.builder.ActiveResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveResponseType;

/**
 * <p>Accessor fo the SPMLv2 Active operation response. The active operation
 * is defined inside the suspend capability (capability to enable, disable
 * and know the status of an object in the repository). The Active operation
 * id the one to know the status (enabled/active or disabled/unactived). The
 * response only has a specific attribute <em>isActive</em> to identify
 * if the requested PSO is currently active / enabled.</p>
 * 
 * @author ricky
 */
public class ActiveResponseAccessor extends BaseResponseAccessor<ActiveResponseType, ActiveResponseAccessor, ActiveResponseBuilder> {

    /**
     * Protected constructor for an empty active response.
     */
    protected ActiveResponseAccessor() {
        this(new ActiveResponseType());
    }
    
    /**
     * Protected constructor for an active response based on the type.
     * @param response The active response type obtained by JAXB
     */
    protected ActiveResponseAccessor(ActiveResponseType response) {
        super(response, null);
    }
    
    /**
     * The method specific for the active response that identified if the PSO
     * is currently active in the repository.
     * @return true if active, false if not
     */
    public boolean isActive() {
        return isSuccess() && response.isActive();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveResponseBuilder toBuilder() {
        return ResponseBuilder.builderForActive().fromResponse(this.response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActiveResponseAccessor asAccessor(ActiveResponseType response) {
        return new ActiveResponseAccessor(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  active: ").append(isActive()).append(nl);
        return sb.toString();
    }
}
