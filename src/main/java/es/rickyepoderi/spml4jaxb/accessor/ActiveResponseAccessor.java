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
 *
 * @author ricky
 */
public class ActiveResponseAccessor extends BaseResponseAccessor<ActiveResponseType, ActiveResponseAccessor, ActiveResponseBuilder> {

    protected ActiveResponseAccessor() {
        this(new ActiveResponseType());
    }
    
    protected ActiveResponseAccessor(ActiveResponseType response) {
        super(response, null);
    }
    
    public boolean isActive() {
        return isSuccess() && response.isActive();
    }
    
    @Override
    public ActiveResponseBuilder toBuilder() {
        return ResponseBuilder.builderForActive().fromResponse(this.response);
    }
    
    @Override
    public ActiveResponseAccessor asAccessor(ActiveResponseType response) {
        return new ActiveResponseAccessor(response);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  active: ").append(isActive()).append(nl);
        return sb.toString();
    }
}
