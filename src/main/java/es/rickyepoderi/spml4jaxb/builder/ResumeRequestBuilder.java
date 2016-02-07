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
import es.rickyepoderi.spml4jaxb.accessor.ResumeRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResumeResponseAccessor;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.msg.suspend.ResumeRequestType;
import javax.xml.bind.JAXBElement;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <p>Builder for the SPMLv2 Resume operation request. The resume
 * operation is defined inside the suspend capability (capability to
 * enable and disable objects in the target). The resume operation
 * is used to enable an object previously disabled (suspended). The resume 
 * request besides the PSO identifier to resume can send the effective
 * date of the action (the date-time when the object will be resumed).</p>
 * 
 * @author ricky
 */
public class ResumeRequestBuilder extends RequestBuilder<ResumeRequestType, ResumeRequestBuilder, 
        ResumeRequestAccessor, ResumeResponseAccessor> {

    /**
     * Constructor for a new resume request buidler.
     */
    public ResumeRequestBuilder() {
        super(new ResumeRequestType());
    }

    /**
     * Setter for the effective date of resuming using a date.
     * @param effectiveDate The effective date of the operation
     * @return The same builder
     */
    public ResumeRequestBuilder effectiveDate(Date effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate);
        return effectiveDate(c);
    }
    
    /**
     * Setter for the effective date of resuming using a calendar.
     * @param effectiveDate The effective date of the operation
     * @return The same builder
     */
    public ResumeRequestBuilder effectiveDate(Calendar effectiveDate) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(effectiveDate.getTime());
        return effectiveDate(c);
    }
    
    /**
     * Setter for the effective date of resuming using a gregorian calendar.
     * @param effectiveDate The effective date of the operation
     * @return The same builder
     */
    public ResumeRequestBuilder effectiveDate(GregorianCalendar effectiveDate) {
        request.setEffectiveDate(dataTypeFactory.newXMLGregorianCalendar(effectiveDate));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ResumeRequestType> build() {
        request.setPsoID(pso);
        return getSuspendObjectFactory().createResumeRequest(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResumeRequestAccessor asAccessor() {
        request.setPsoID(pso);
        return BaseRequestAccessor.accessorForRequest(request).asResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResumeRequestBuilder fromRequest(ResumeRequestType request) {
        this.request = request;
        this.pso = request.getPsoID();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResumeResponseAccessor send(SpmlRequestor client) throws SpmlException {
        return this.sendGeneric(client).asResume();
    }
    
}