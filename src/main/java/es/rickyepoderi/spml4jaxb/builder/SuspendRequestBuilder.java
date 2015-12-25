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
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.suspend.SuspendRequestType;
import javax.xml.bind.JAXBElement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 *
 * @author ricky
 */
public class SuspendRequestBuilder extends RequestBuilder<SuspendRequestType, SuspendRequestBuilder> {

    public SuspendRequestBuilder() {
        super(new SuspendRequestType());
    }
    
    public SuspendRequestBuilder effectiveDate(Date effectiveDate) throws SpmlException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate);
        return effectiveDate(c);
    }
    
    public SuspendRequestBuilder effectiveDate(Calendar effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate.getTime());
        return effectiveDate(c);
    }
    
    public SuspendRequestBuilder effectiveDate(GregorianCalendar effectiveDate) {
        request.setEffectiveDate(dataTypeFactory.newXMLGregorianCalendar(effectiveDate));
        return this;
    }

    @Override
    public JAXBElement<SuspendRequestType> build() {
        request.setPsoID(pso);
        return getSuspendObjectFactory().createSuspendRequest(request);
    }
    
    @Override
    public RequestAccessor asAccessor() {
        request.setPsoID(pso);
        return super.asAccessor().asSuspend();
    }
    
}
