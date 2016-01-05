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
 *
 * @author ricky
 */
public class AddResponseAccessor extends ResponseAccessor<AddResponseType, AddResponseBuilder> {

    protected AddResponseAccessor(AddResponseType response) {
        super(response, response.getPso());
    }
    
    @Override
    public AddResponseBuilder toBuilder() {
        return ResponseBuilder.builderForAdd().fromResponse(this.response);
    }
    
}
