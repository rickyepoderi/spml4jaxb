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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.UpdatesResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateKindType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesResponseType;
import java.util.Date;
import java.util.Iterator;

/**
 * <p>Accessor for the SPMLv2 Updates response. The updates
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
 * and the iterator id (if there are more pages). Again this accessor
 * implements the Iterator and Iterable interfaces in order to easily loop
 * the response. There are methods to access each property of the
 * update.</p>
 * 
 * @author ricky
 */
public class UpdatesResponseAccessor extends BaseResponseAccessor<UpdatesResponseType, 
        UpdatesResponseAccessor, UpdatesResponseBuilder> 
        implements Iterator<UpdatesResponseAccessor>, Iterable<UpdatesResponseAccessor> {

    /**
     * The iterator for the list tha contains the updates in the response.
     */
    protected Iterator<UpdateType> iterator;
    
    /**
     * The current update in the iteration.
     */
    protected UpdateType update;
    
    /**
     * COnstructor for a new updates response accessor.
     */
    protected UpdatesResponseAccessor() {
        this(new UpdatesResponseType());
    }
    
    /**
     * Constructor for a updates response using the internal type.
     * @param response The internal updates type as defined in the standard
     */
    protected UpdatesResponseAccessor(UpdatesResponseType response) {
        super(response, null);
        start();
    }
    
    /**
     * Getter for the iterator identifier in the response. If there is an
     * identifier means that this is just a page for the complete list
     * of updates that matched the request criteria. The requestor can Iterate
     * to retrieve the next page with this identifier.
     * 
     * @return  The identifier or null (no more pages)
     */
    public String getIteratorId() {
        if (response.getIterator() != null) {
            return response.getIterator().getID();
        } else {
            return null;
        }
    }
    
    /**
     * Method that starts the iterator. This method is automatically called
     * in the constructor but can be used to reset or restart the iterator.
     */
    public void start() {
        iterator = response.getUpdate().iterator();
    }
    
    /**
     * Iterator method to know if there are more updates in the iterator.
     * 
     * @return true if there are still more updates, false if not
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    
    /**
     * Iterator method to advance the iterator and put the next update as the
     * current one. It returns this same accessor cos the iterator iterates over it.
     * 
     * @return The same accessor but with the update advanced to the next
     */
    @Override
    public UpdatesResponseAccessor next() {
        if (iterator.hasNext()) {
            update = iterator.next();
        } else {
            update = null;
        }
        return this;
    }

    /**
     * Iterator method to remove the current update. But this is not implemented,
     * it has no sense and throws a UnsupportedOperationException.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Unmodifiable iterator");
    }

    /**
     * Iterable method to obtain the iterator which is the same accessor.
     * 
     * @return This same accessor cos it is the iterator
     */
    @Override
    public Iterator<UpdatesResponseAccessor> iterator() {
        start();
        return this;
    }
    
    //
    // UPDATE methods
    //
    
    /**
     * Method to access the current update in the iteration. This method 
     * gets the PSO identifier of the update.
     * 
     * @return The PSO identifier of the current update
     */
    public String getUpdatePsoId() {
        if (update.getPsoID() != null) {
            return update.getPsoID().getID();
        } else {
            return null;
        }
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * gets the PSO target identifier of the update.
     * 
     * @return The PSO target identifier of the current update
     */
    public String getUpdateTargetId() {
        if (update.getPsoID() != null) {
            return update.getPsoID().getTargetID();
        } else {
            return null;
        }
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * gets the PSO identifier as an accessor of the update.
     * 
     * @return The PSO identifier of the current update
     */
    public PsoIdentifierAccessor getUpdatePsoAccessor() {
        if (update.getPsoID() != null) {
            return new PsoIdentifierAccessor(update.getPsoID());
        } else {
            return null;
        }
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * gets the timestamp of the current update.
     * 
     * @return The timestamp of the update
     */
    public Date getUpdateTimestamp() {
        if (update.getTimestamp() != null) {
            return update.getTimestamp().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * gets the kind of the update.
     * 
     * @return The update kind of the current update
     */
    public UpdateKindType getUpdateKind() {
        return update.getUpdateKind();
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * checks id the update kind is an ADD.
     * 
     * @return true if the current update kind is an ADD
     */
    public boolean isUpdateKindAdd() {
        return UpdateKindType.ADD.equals(update.getUpdateKind());
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * checks id the update kind is a MODIFY.
     * 
     * @return true if the current update kind is a MODIFY
     */
    public boolean isUpdateKindModify() {
        return UpdateKindType.MODIFY.equals(update.getUpdateKind());
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * checks id the update kind is a DELETE.
     * 
     * @return true if the current update kind is a DELETE
     */
    public boolean isUpdateKindDelete() {
        return UpdateKindType.DELETE.equals(update.getUpdateKind());
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * checks id the update kind is a CAPABILITY.
     * 
     * @return true if the current update kind is a CAPABILITY
     */
    public boolean isUpdateKindCability() {
        return UpdateKindType.CAPABILITY.equals(update.getUpdateKind());
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * gets the capability that produced the updates.
     * 
     * @return The capability of the current update
     */
    public String getUpdateByCapability() {
        return update.getWasUpdatedByCapability();
    }
    
    /**
     * Method to access the current update in the iteration. This method 
     * checks if the update was produced by the passed capability.
     * 
     * @return true if the update capability is the one passed, false otherwise
     */
    public boolean isUpdatedByCapability(String capability) {
        return update.getWasUpdatedByCapability() != null && update.getWasUpdatedByCapability().equals(capability);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesResponseBuilder toBuilder() {
        return ResponseBuilder.builderForUpdates().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatesResponseAccessor asAccessor(UpdatesResponseType response) {
        return new UpdatesResponseAccessor(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  iteratorId: ").append(getIteratorId()).append(nl);
        sb.append("  updates: ").append(response.getUpdate().size()).append(nl);
        return sb.toString();
    }
    
}