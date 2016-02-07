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
 * <p>Builder for the SPMLv2 Status operation response. The status
 * operation is defined inside the asynchronous capability (capability to
 * manage other previous methods sent asynchronously). The status operation
 * is used to retrieve the status of a previous asynchronous operation. 
 * The status response gives the current status (pending, success or failure)
 * and, in case the operation is finished, the response of that request.
 * So the builder has a method to add the another response using the builder.
 * Following the standard this operation can return more than one response if no
 * asynch request id was specified.</p>
 * 
 * @author ricky
 */
public class StatusResponseBuilder extends ResponseBuilder<StatusResponseType, StatusResponseBuilder, StatusResponseAccessor> {

    /**
     * Constructor for a new status response builder.
     */
    protected StatusResponseBuilder() {
        super(new StatusResponseType());
    }
    
    /**
     * Setter for the asynchronous request id that is returned in the status
     * response.
     * @param asyncRequestId The previous asynch request id
     * @return The same builder
     */
    public StatusResponseBuilder asyncRequestId(String asyncRequestId) {
        response.setAsyncRequestID(asyncRequestId);
        return this;
    }
    
    /**
     * Adds a response in the status response. As status can return the status
     * for several different SPMLv2 operations a generic builder is passed
     * to build the specific response. The standard says the status response
     * can send several responses if no synch request id is specified (the
     * standard says that in that case the operation should return all 
     * the previous status of all operations that the provider has 
     * executed asynchronously on behalf of the requestor). So the method
     * as the response into the list.
     * 
     * @param res The response builder properly filled
     * @return The same builder
     */
    public StatusResponseBuilder nestedResponse(ResponseBuilder res) {
        response.getAny().add(res.build());
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<StatusResponseType> build() {
        return getAsyncObjectFactory().createStatusResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public StatusResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asStatus();
    }
    
}
