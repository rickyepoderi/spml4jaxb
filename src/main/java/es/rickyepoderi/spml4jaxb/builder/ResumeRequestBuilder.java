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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.msg.suspend.ResumeRequestType;
import javax.xml.bind.JAXBElement;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author ricky
 */
public class ResumeRequestBuilder extends RequestBuilder<ResumeRequestType, ResumeRequestBuilder> {

    public ResumeRequestBuilder() {
        super(new ResumeRequestType());
    }

    public ResumeRequestBuilder effectiveDate(Date effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate);
        return effectiveDate(c);
    }
    
    public ResumeRequestBuilder effectiveDate(Calendar effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate.getTime());
        return effectiveDate(c);
    }
    
    public ResumeRequestBuilder effectiveDate(GregorianCalendar effectiveDate) {
        request.setEffectiveDate(dataTypeFactory.newXMLGregorianCalendar(effectiveDate));
        return this;
    }

    @Override
    public JAXBElement<ResumeRequestType> build() {
        request.setPsoID(pso);
        return getSuspendObjectFactory().createResumeRequest(request);
    }
    
    @Override
    public RequestAccessor asAccessor() {
        request.setPsoID(pso);
        return super.asAccessor().asResume();
    }
    
}
