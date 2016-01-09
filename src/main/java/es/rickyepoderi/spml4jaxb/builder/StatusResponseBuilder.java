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
import es.rickyepoderi.spml4jaxb.accessor.StatusResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.async.StatusResponseType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class StatusResponseBuilder extends ResponseBuilder<StatusResponseType, StatusResponseBuilder, StatusResponseAccessor> {

    protected StatusResponseBuilder() {
        super(new StatusResponseType());
    }
    
    public StatusResponseBuilder asyncRequestId(String asyncRequestId) {
        response.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    public StatusResponseBuilder nestedResponse(ResponseBuilder res) {
        response.getAny().add(res.build());
        return this;
    }
    
    @Override
    public JAXBElement<StatusResponseType> build() {
        return getAsyncObjectFactory().createStatusResponse(response);
    }
    
    @Override
    public StatusResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asStatus();
    }
    
}
