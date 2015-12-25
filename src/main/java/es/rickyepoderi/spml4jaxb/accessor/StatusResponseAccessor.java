/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.msg.async.StatusResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class StatusResponseAccessor extends ResponseAccessor<StatusResponseType> {

    protected StatusResponseAccessor(StatusResponseType response) {
        super(response, null);
    }
    
    public String getAsyncRequestId() {
        return response.getAsyncRequestID();
    }
    
    public boolean isAsyncRequestId(String asyncRequestId) {
        return asyncRequestId.equals(response.getAsyncRequestID());
    }
    
    public ResponseAccessor[] getNestedResponses() {
        List<ResponseAccessor> res = new ArrayList<>();
        List<Object> l = response.getAny();
        for (Object o : l) {
            if (o instanceof ResponseType) {
                res.add(ResponseAccessor.accessorForResponse((ResponseType) o));
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (el.getValue() instanceof ResponseType) {
                    res.add(ResponseAccessor.accessorForResponse((ResponseType) el.getValue()));
                }
            }
        }
        return res.toArray(new ResponseAccessor[0]);
    }
    
    public ResponseAccessor getNestedResponse() {
        ResponseAccessor[] res = getNestedResponses();
        if (res.length > 0) {
            return res[0];
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString())
                .append("  Nested Responses:").append(nl);
        for (ResponseAccessor res: getNestedResponses()) {
            sb.append(res);
        }
        return sb.toString();
    }
}
