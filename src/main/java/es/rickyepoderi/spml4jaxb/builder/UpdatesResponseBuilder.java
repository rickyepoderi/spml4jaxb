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

import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.updates.ResultsIteratorType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateKindType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesResponseType;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class UpdatesResponseBuilder extends ResponseBuilder<UpdatesResponseType, UpdatesResponseBuilder> {

    protected UpdateType update = null;
    
    public UpdatesResponseBuilder() {
        super(new UpdatesResponseType());
    }
    
    public UpdatesResponseBuilder iteratorId(String id) {
        ResultsIteratorType iter = response.getIterator();
        if (iter == null) {
            iter = new ResultsIteratorType();
            response.setIterator(iter);
        }
        iter.setID(id);
        return this;
    }
    
    public UpdatesResponseBuilder updatePsoId(String psoId) {
        if (update == null) {
            update = new UpdateType();
        }
        if (update.getPsoID() == null) {
            update.setPsoID(new PSOIdentifierType());
        }
        update.getPsoID().setID(psoId);
        return this;
    }
    
    public UpdatesResponseBuilder updatePsoTargetId(String targetId) {
        if (update == null) {
            update = new UpdateType();
        }
        if (update.getPsoID() == null) {
            update.setPsoID(new PSOIdentifierType());
        }
        update.getPsoID().setTargetID(targetId);
        return this;
    }
    
    public UpdatesResponseBuilder updateTimestamp(long timestamp) {
        return updateTimestamp(new Date(timestamp));
    }
    
    public UpdatesResponseBuilder updateTimestamp(Date timestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(timestamp);
        return updateTimestamp(c);
    }
    
    public UpdatesResponseBuilder updateTimestamp(Calendar timestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(timestamp.getTime());
        return updateTimestamp(c);
    }
    
    public UpdatesResponseBuilder updateTimestamp(GregorianCalendar timestamp) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setTimestamp(RequestBuilder.dataTypeFactory.newXMLGregorianCalendar(timestamp));
        return this;
    }
    
    public UpdatesResponseBuilder updateKind(UpdateKindType kind) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setUpdateKind(kind);
        return this;
    }
    
    public UpdatesResponseBuilder updateKindAdd() {
        return updateKind(UpdateKindType.ADD);
    }
    
    public UpdatesResponseBuilder updateKindModify() {
        return updateKind(UpdateKindType.MODIFY);
    }
    
    public UpdatesResponseBuilder updateKindDelete() {
        return updateKind(UpdateKindType.DELETE);
    }
    
    public UpdatesResponseBuilder updateKindCapabilite() {
        return updateKind(UpdateKindType.CAPABILITY);
    }
    
    public UpdatesResponseBuilder updateByCapability(String capability) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setWasUpdatedByCapability(capability);
        return this;
    }
    
    public UpdatesResponseBuilder nextUpdate() {
        if (update != null) {
            response.getUpdate().add(update);
            update = null;
        }
        return this;
    }

    @Override
    public JAXBElement<UpdatesResponseType> build() {
        nextUpdate();
        return getUpdatesObjectFactory().createUpdatesResponse(response);
    }
    
    @Override
    public ResponseAccessor asAccessor() {
        return super.asAccessor().asUpdates();
    }
    
}
