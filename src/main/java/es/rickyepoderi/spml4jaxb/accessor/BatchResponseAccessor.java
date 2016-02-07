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

import es.rickyepoderi.spml4jaxb.builder.BatchResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;

/**
 * <p>Accessor for the SPMLv2 Batch operation response. The batch operation
 * is defined inside the batch capability (capability to send several operations
 * in batch mode in a single request, it is a bunch of requests in one). The
 * response packs some other responses in a single batch operation. Only
 * some operations are allowed to be batched (see OASIS specification for
 * more details).</p>
 * 
 * @author ricky
 */
public class BatchResponseAccessor extends BaseResponseAccessor<BatchResponseType, BatchResponseAccessor, BatchResponseBuilder> {

    /**
     * Constructor for an empty batch response accessor.
     */
    protected BatchResponseAccessor() {
        this(new BatchResponseType());
    }
    
    /**
     * Constructor of the accessor using the internal batch response type.
     * @param response The batch response type obtained using JAXB
     */
    protected BatchResponseAccessor(BatchResponseType response) {
        super(response, null);
    }
    
    /**
     * Method that returns the nested responses in an array. The array
     * is defined as the unknown response accessor to be defined using asXXX
     * methods.
     * @return The array of nested response accessor or empty array
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
     * Returns the nested responses but in a map indexed by the request id.
     * @return The map of responses or empty map
     */
    public Map<String,ResponseAccessor> getNestedResponsesMap() {
        ResponseAccessor[] responses = this.getNestedResponses();
        Map<String,ResponseAccessor> result = new HashMap<>(responses.length);
        for (ResponseAccessor res: responses) {
            result.put(res.getRequestId(), res);
        }
        return result;
    }
    
    /**
     * Returns the nested response for a specific request id. This method 
     * first constructs the map so do not call it a lot of times, better
     * get the map using the previous method and use that map.
     * 
     * @param requestId The request id to get from the map
     * @return the response for that request id or null
     */
    public ResponseAccessor getNestedResponse(String requestId) {
        return this.getNestedResponsesMap().get(requestId);
    }
    
    /**
     * {@inheritDoc}
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BatchResponseBuilder toBuilder() {
        return ResponseBuilder.builderForBatch().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BatchResponseAccessor asAccessor(BatchResponseType response) {
        return new BatchResponseAccessor(response);
    }
}
