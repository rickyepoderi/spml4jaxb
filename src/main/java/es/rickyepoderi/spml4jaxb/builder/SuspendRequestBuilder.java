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

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SuspendRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.SuspendResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.suspend.SuspendRequestType;
import javax.xml.bind.JAXBElement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * <p>Builder for the SPMLv2 Suspend operation request. The suspend
 * operation is defined inside the suspend capability (capability to
 * enable and disable objects in the target). The suspend operation
 * is used to disable an object. The suspend request besides the PSO identifier 
 * to disable can send the effective date of the action (the date-time when the
 * object will be definitely suspended).</p>
 * 
 * @author ricky
 */
public class SuspendRequestBuilder extends RequestBuilder<SuspendRequestType, SuspendRequestBuilder, 
        SuspendRequestAccessor, SuspendResponseAccessor> {

    /**
     * Constructor for a new suspend request builder.
     */
    public SuspendRequestBuilder() {
        super(new SuspendRequestType());
    }
    
    /**
     * Setter for the effective date of teh operation using a date.
     * @param effectiveDate The date when the operation will be effective
     * @return The same builder
     */
    public SuspendRequestBuilder effectiveDate(Date effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate);
        return effectiveDate(c);
    }
    
    /**
     * Setter for the effective date of teh operation using a calendar.
     * @param effectiveDate The date when the operation will be effective
     * @return The same builder
     */
    public SuspendRequestBuilder effectiveDate(Calendar effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate.getTime());
        return effectiveDate(c);
    }
    
    /**
     * Setter for the effective date of teh operation using a gregorian calendar.
     * @param effectiveDate The date when the operation will be effective
     * @return The same builder
     */
    public SuspendRequestBuilder effectiveDate(GregorianCalendar effectiveDate) {
        request.setEffectiveDate(dataTypeFactory.newXMLGregorianCalendar(effectiveDate));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<SuspendRequestType> build() {
        request.setPsoID(pso);
        return getSuspendObjectFactory().createSuspendRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asSuspend();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendRequestBuilder fromRequest(SuspendRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asSuspend();
    }
    
}