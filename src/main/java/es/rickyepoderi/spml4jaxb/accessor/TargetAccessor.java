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

import es.rickyepoderi.spml4jaxb.builder.ListTargetsRequestBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.CapabilitiesListType;
import es.rickyepoderi.spml4jaxb.msg.core.CapabilityType;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaEntityRefType;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaType;
import es.rickyepoderi.spml4jaxb.msg.core.TargetType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricky
 */
public class TargetAccessor {
    
    protected TargetType target;
    
    protected TargetAccessor(TargetType target) {
        this.target = target;
    }
    
    public String getTargetId() {
        return target.getTargetID();
    }
    
    public String getProfile() {
        return target.getProfile();
    }
    
    public boolean isDsml() {
        return ListTargetsRequestBuilder.DSML_PROFILE_URI.equals(getProfile());
    }
    
    public boolean isXsd() {
        return ListTargetsRequestBuilder.XSD_PROFILE_URI.equals(getProfile());
    }
    
    public SchemaAccessor[] getSchemas() {
        List<SchemaAccessor> res = new ArrayList<>();
        for (SchemaType schema: target.getSchema()) {
            res.add(new SchemaAccessor(schema));
        }
        return res.toArray(new SchemaAccessor[0]);
    }
    
    public boolean hasCapability(String uri) {
        return hasCapabilityFor(uri, null);
    }
    
    public boolean hasCapabilityFor(String uri, String entity) {
        CapabilitiesListType capabilities = target.getCapabilities();
        if (capabilities != null) {
            for (CapabilityType capability: capabilities.getCapability()) {
                if (uri.equals(capability.getNamespaceURI())) {
                    if (entity == null) {
                        // generic capability is checked
                        return true;
                    } else if (capability.getAppliesTo().isEmpty()) {
                        // if no entity is defined, it is generic for all entitied
                        return true;
                    } else {
                        // check if the entity is specified
                        for (SchemaEntityRefType ref : capability.getAppliesTo()) {
                            if (entity.equals(ref.getEntityName())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean hasAsyncCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_ASYNC_URI);
    }
    
    public boolean hasPasswordCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_PASSWORD_URI);
    }
    
    public boolean hasPasswordCapabilityFor(String entity) {
        return hasCapabilityFor(RequestAccessor.SPML_CAPABILITY_PASSWORD_URI, entity);
    }
    
    public boolean hasSuspendCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_SUSPEND_URI);
    }
    
    public boolean hasSuspendCapabilityFor(String entity) {
        return hasCapabilityFor(RequestAccessor.SPML_CAPABILITY_SUSPEND_URI, entity);
    }
    
    public boolean hasBulkCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_BULK_URI);
    }
    
    public boolean hasBatchCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_BATCH_URI);
    }
    
    public boolean hasUpdatesCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_UPDATES_URI);
    }
    
    public boolean hasSearchCapability() {
        return hasCapability(RequestAccessor.SPML_CAPABILITY_SEARCH_URI);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder("  Target ").append(" ")
                .append(getTargetId()).append(" - ").append(getProfile())
                .append(nl);
        for (SchemaAccessor s : getSchemas()) {
            sb.append(s);
        }
        return sb.toString();
    }
}
