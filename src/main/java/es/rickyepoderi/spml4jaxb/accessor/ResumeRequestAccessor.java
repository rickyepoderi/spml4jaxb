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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.suspend.ResumeRequestType;
import java.util.Date;

/**
 *
 * @author ricky
 */
public class ResumeRequestAccessor extends RequestAccessor<ResumeRequestType> {

    public ResumeRequestAccessor(ResumeRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    public Date getEffectiveDate() {
        if (request.getEffectiveDate() != null) {
            return request.getEffectiveDate().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }
    
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForResume();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  effectiveDate: ").append(getEffectiveDate()).append(nl);
        return sb.toString();
    }
    
}
