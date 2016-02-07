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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.StatusResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.async.StatusResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 * <p>Accessor for the SPMLv2 Status operation response. The status
 * operation is defined inside the asynchronous capability (capability to
 * manage other previous methods sent asynchronously). The status operation
 * is used to retrieve the status of a previous asynchronous operation. 
 * The status response gives the current status (pending, success or failure)
 * and, in case the operation is finished, the response of that request.
 * So the response has a method to return the another response accessor
 * (unknown) which is the one of the previous operation. Following the
 * standard this operation can return more than one response if no
 * asynch request id was specified.</p>
 * 
 * @author ricky
 */
public class StatusResponseAccessor extends BaseResponseAccessor<StatusResponseType, StatusResponseAccessor, StatusResponseBuilder> {

    /**
     * Constructor for a new status response accessor.
     */
    protected StatusResponseAccessor() {
        this(new StatusResponseType());
    }
    
    /**
     * Constructor for a status response accessor passing the internal type.
     * @param response The internal status response accessor as defined in the standard
     */
    protected StatusResponseAccessor(StatusResponseType response) {
        super(response, null);
    }
    
    /**
     * Getter for the asynch request id (the id of the previously asynch operation
     * the client is asking for).
     * 
     * @return The asynch request id set in the response
     */
    public String getAsyncRequestId() {
        return response.getAsyncRequestID();
    }
    
    /**
     * Checker for the asynch request id. It just compares if the passed id
     * is equals to the asynch request id assigned in the response.
     * 
     * @param asyncRequestId The id to compare
     * @return true if the passed if is equals to the internal one, false otherwise
     */
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(response.getAsyncRequestID());
    }
    
    /**
     * Returns all the nested response as an array. The standard says that if
     * the requestor does not assign an asynch id this method should return all 
     * the previous status of all operations that the provider has 
     * executed asynchronously on behalf of the requestor. So it can have more
     * than one nested response. The unknown accessor is returned so you
     * have to use asXXX methods properly.
     * 
     * @return The array of responses or empty array
     */
    public ResponseAccessor[] getNestedResponses() {
        List<ResponseAccessor> res = new ArrayList<>();
        List<Object> l = response.getAny();
        for (Object o : l) {
            if (o instanceof ResponseType) {
                res.add(BaseResponseAccessor.accessorForResponse((ResponseType) o));
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof ResponseType) {
                    res.add(BaseResponseAccessor.accessorForResponse((ResponseType) el.getValue()));
                }
            }
        }
        return res.toArray(new ResponseAccessor[0]);
    }
    
    /**
     * Proper method if you want to return just the first nested response
     * (which is the common use). The unknown response is returned.
     * 
     * @return The first nested response or null
     */
    public ResponseAccessor getNestedResponse() {
        ResponseAccessor[] res = getNestedResponses();
        if (res.length > 0) {
            return res[0];
        } else {
            return null;
        }
    }
    
    /**
     * {{@inheritDoc}
     */
    @Override
    public StatusResponseBuilder toBuilder() {
        return ResponseBuilder.builderForStatus().fromResponse(this.response);
    }

    /**
     * {{@inheritDoc}
     */
    @Override
    public StatusResponseAccessor asAccessor(StatusResponseType response) {
        return new StatusResponseAccessor(response);
    }
    
    /**
     * {{@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString())
                .append("  Nested Responses:").append(nl);
        for (BaseResponseAccessor res: getNestedResponses()) {
            sb.append(res);
        }
        return sb.toString();
    }
}