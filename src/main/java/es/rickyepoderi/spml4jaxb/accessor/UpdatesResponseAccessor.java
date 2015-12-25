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

import es.rickyepoderi.spml4jaxb.msg.updates.UpdateKindType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdateType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesResponseType;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author ricky
 */
public class UpdatesResponseAccessor extends ResponseAccessor<UpdatesResponseType> 
        implements Iterator<UpdatesResponseAccessor>, Iterable<UpdatesResponseAccessor> {

    protected Iterator<UpdateType> iterator;
    protected UpdateType update;
    
    public UpdatesResponseAccessor(UpdatesResponseType response) {
        super(response, null);
        start();
    }
    
    public String getIteratorId() {
        if (response.getIterator() != null) {
            return response.getIterator().getID();
        } else {
            return null;
        }
    }
    
    public void start() {
        iterator = response.getUpdate().iterator();
    }
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    
    @Override
    public UpdatesResponseAccessor next() {
        if (iterator.hasNext()) {
            update = iterator.next();
        } else {
            update = null;
        }
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Unmodifiable iterator");
    }

    @Override
    public Iterator<UpdatesResponseAccessor> iterator() {
        start();
        return this;
    }
    
    public String getUpdatePsoId() {
        if (update.getPsoID() != null) {
            return update.getPsoID().getID();
        } else {
            return null;
        }
    }
    
    public String getUpdateTargetId() {
        if (update.getPsoID() != null) {
            return update.getPsoID().getTargetID();
        } else {
            return null;
        }
    }
    
    public Date getUpdateTimestamp() {
        if (update.getTimestamp() != null) {
            return update.getTimestamp().toGregorianCalendar().getTime();
        } else {
            return null;
        }
    }
    
    public UpdateKindType getUpdateKind() {
        return update.getUpdateKind();
    }
    
    public boolean isUpdateKindAdd() {
        return update.getUpdateKind() != null && UpdateKindType.ADD.equals(update.getUpdateKind());
    }
    
    public boolean isUpdateKindModify() {
        return update.getUpdateKind() != null && UpdateKindType.MODIFY.equals(update.getUpdateKind());
    }
    
    public boolean isUpdateKindDelete() {
        return update.getUpdateKind() != null && UpdateKindType.DELETE.equals(update.getUpdateKind());
    }
    
    public boolean isUpdateKindCability() {
        return update.getUpdateKind() != null && UpdateKindType.CAPABILITY.equals(update.getUpdateKind());
    }
    
    public String getUpdateByCapability() {
        return update.getWasUpdatedByCapability();
    }
    
    public boolean isUpdatedByCapability(String capability) {
        return update.getWasUpdatedByCapability() != null && update.getWasUpdatedByCapability().equals(capability);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  iteratorId: ").append(getIteratorId()).append(nl);
        sb.append("  updates: ").append(response.getUpdate().size()).append(nl);
        return sb.toString();
    }
    
}
