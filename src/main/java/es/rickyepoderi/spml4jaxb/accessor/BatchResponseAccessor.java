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
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class BatchResponseAccessor extends BaseResponseAccessor<BatchResponseType, BatchResponseAccessor, BatchResponseBuilder> {

    protected BatchResponseAccessor() {
        this(new BatchResponseType());
    }
    
    protected BatchResponseAccessor(BatchResponseType response) {
        super(response, null);
    }
    
    public BaseResponseAccessor[] getNestedResponses() {
        List<BaseResponseAccessor> res = new ArrayList<>();
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
        return res.toArray(new BaseResponseAccessor[0]);
    }
    
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
    
    @Override
    public BatchResponseBuilder toBuilder() {
        return ResponseBuilder.builderForBatch().fromResponse(this.response);
    }

    @Override
    public BatchResponseAccessor asAccessor(BatchResponseType response) {
        return new BatchResponseAccessor(response);
    }
}
