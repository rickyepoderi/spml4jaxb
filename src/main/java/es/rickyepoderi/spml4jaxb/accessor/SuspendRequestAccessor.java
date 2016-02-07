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

import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.SuspendRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.suspend.SuspendRequestType;
import java.util.Date;

/**
 * <p>Accessor for the SPMLv2 Suspend operation request. The suspend
 * operation is defined inside the suspend capability (capability to
 * enable and disable objects in the target). The suspend operation
 * is used to disable an object. The suspend request besides the PSO identifier 
 * to disable can send the effective date of the action (the date-time when the
 * object will be definitely suspended).</p>
 * 
 * @author ricky
 */
public class SuspendRequestAccessor extends BaseRequestAccessor<SuspendRequestType, SuspendRequestAccessor, SuspendRequestBuilder> {

    /**
     * Constructor for a empty suspend request accessor.
     */
    protected SuspendRequestAccessor() {
        this(new SuspendRequestType());
    }
    
    /**
     * Constructor for a suspend request accessor using the internal type.
     * @param request The internal suspend request type as defined in the standard
     */
    protected SuspendRequestAccessor(SuspendRequestType request) {
        super(request, request.getPsoID(), null);
    }
    
    /**
     * Getter of the effective date when the object will be suspended.
     * @return The date or null
     */
    public Date getEffectiveDate() {
        if (request.getEffectiveDate() != null) {
            return request.getEffectiveDate().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseBuilder responseBuilder() {
        return ResponseBuilder.builderForSuspend();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendRequestBuilder toBuilder() {
        return RequestBuilder.builderForSuspend().fromRequest(this.request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuspendRequestAccessor asAccessor(SuspendRequestType request) {
        return new SuspendRequestAccessor(request);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  effectiveDate: ").append(getEffectiveDate()).append(nl);
        return sb.toString();
    }
}