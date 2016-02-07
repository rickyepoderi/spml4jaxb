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

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesResponseAccessor;
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
 * <p>Builder for the SPMLv2 Updates response. The updates
 * operation is defined inside the updates capability. The updates capability 
 * allow a requestor to obtain in a scalable manner every recorded update (i.e., 
 * modification to an object) that matches specified selection criteria. The
 * updates operates in the same manner than search, it return the update 
 * records in pages. So again the same technique is used, the updates return
 * the first page of records and, if there are more to come, it appends a
 * iterator identifier in the response. With that id the requestor can
 * request the next page (Iterate operation). If it is needed a third page
 * another iterator id is used in the second page. Finally a CloseIterator
 * in this capability frees any resources in the server related to the updates
 * operation.</p>
 * 
 * <p>In the response the updates operation return a list of updates
 * and the iterator id (if there are more pages). Again this builder
 * uses a current update to be filled and the the <em>nextUpdate</em>
 * method store it in the list and prepares for the new.</p>
 * 
 * @author ricky
 */
public class UpdatesResponseBuilder extends ResponseBuilder<UpdatesResponseType, 
        UpdatesResponseBuilder, UpdatesResponseAccessor> {

    /**
     * The current update managed by the builder.
     */
    protected UpdateType update = null;
    
    /**
     * Constructor for a new updates response builder.
     */
    public UpdatesResponseBuilder() {
        super(new UpdatesResponseType());
    }
    
    /**
     * Setter for the iterator identifier if more pages are needed.
     * 
     * @param id The iterator id that will use the requestor for iterate over the next page
     * @return The same builder
     */
    public UpdatesResponseBuilder iteratorId(String id) {
        ResultsIteratorType iter = response.getIterator();
        if (iter == null) {
            iter = new ResultsIteratorType();
            response.setIterator(iter);
        }
        iter.setID(id);
        return this;
    }
    
    /**
     * Method to write the current update that assigns the PSO identifier.
     * 
     * @param psoId The PSO identifier of the update.
     * @return The same builder
     */
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
    
    /**
     * Method to write the current update that assigns the PSO target identifier.
     * 
     * @param targetId The PSO target identifier of the update.
     * @return the same builder
     */
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
    
    /**
     * Method to write the current update that assigns the PSO identifier using the builder.
     * 
     * @param pso The PSO builder to assign the PSO identifier in the current target.
     * @return the same builder
     */
    public UpdatesResponseBuilder updatePso(PsoIdentifierBuilder pso) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setPsoID(pso.build());
        return this;
    }
    
    /**
     * Method to write the current update that assigns the timestamp using a long.
     * 
     * @param timestamp The timestamp of the current update
     * @return The same builder
     */
    public UpdatesResponseBuilder updateTimestamp(long timestamp) {
        return updateTimestamp(new Date(timestamp));
    }
    
    /**
     * Method to write the current update that assigns the timestamp using a date.
     * 
     * @param timestamp The timestamp of the current update
     * @return The same builder
     */
    public UpdatesResponseBuilder updateTimestamp(Date timestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(timestamp);
        return updateTimestamp(c);
    }
    
    /**
     * Method to write the current update that assigns the timestamp using a calendar.
     * 
     * @param timestamp The timestamp of the current update
     * @return The same builder
     */
    public UpdatesResponseBuilder updateTimestamp(Calendar timestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(timestamp.getTime());
        return updateTimestamp(c);
    }
    
    /**
     * Method to write the current update that assigns the timestamp using a gregorian calendar.
     * 
     * @param timestamp The timestamp of the current update
     * @return The same builder
     */
    public UpdatesResponseBuilder updateTimestamp(GregorianCalendar timestamp) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setTimestamp(RequestBuilder.dataTypeFactory.newXMLGregorianCalendar(timestamp));
        return this;
    }
    
    /**
     * Method to write the current update that assigns the kind of the update.
     * 
     * @param kind The update kind of the current update
     * @return The same builder
     */
    public UpdatesResponseBuilder updateKind(UpdateKindType kind) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setUpdateKind(kind);
        return this;
    }
    
    /**
     * Method to write the current update kind as an ADD.
     * 
     * @return The same builder
     */
    public UpdatesResponseBuilder updateKindAdd() {
        return updateKind(UpdateKindType.ADD);
    }
    
    /**
     * Method to write the current update kind as a MODIFY.
     * 
     * @return The same builder
     */
    public UpdatesResponseBuilder updateKindModify() {
        return updateKind(UpdateKindType.MODIFY);
    }
    
    /**
     * Method to write the current update kind as a DELETE.
     * 
     * @return The same builder
     */
    public UpdatesResponseBuilder updateKindDelete() {
        return updateKind(UpdateKindType.DELETE);
    }
    
    /**
     * Method to write the current update kind as a CAPABILITY.
     * 
     * @return The same builder
     */
    public UpdatesResponseBuilder updateKindCapability() {
        return updateKind(UpdateKindType.CAPABILITY);
    }
    
    /**
     * Method to write the current update that assign the capability that
     * produced the update.
     * 
     * @param capability The capability that produced the current update
     * @return The same builder
     */
    public UpdatesResponseBuilder updateByCapability(String capability) {
        if (update == null) {
            update = new UpdateType();
        }
        update.setWasUpdatedByCapability(capability);
        return this;
    }
    
    /**
     * Method that adds the current update in the list of updates and
     * prepares for the next one.
     * @return The same builder
     */
    public UpdatesResponseBuilder nextUpdate() {
        if (update != null) {
            response.getUpdate().add(update);
            update = null;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<UpdatesResponseType> build() {
        nextUpdate();
        return getUpdatesObjectFactory().createUpdatesResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asUpdates();
    }
    
}