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
import es.rickyepoderi.spml4jaxb.accessor.TargetAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.CapabilitiesListType;
import es.rickyepoderi.spml4jaxb.msg.core.CapabilityType;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaEntityRefType;
import es.rickyepoderi.spml4jaxb.msg.core.TargetType;

/**
 *
 * @author ricky
 */
public class TargetBuilder implements Builder<TargetType, TargetAccessor> {
    
    protected TargetType target = null;
    
    protected TargetBuilder() {
        this.target = new TargetType();
    }
    
    public TargetBuilder(TargetType target) {
        this.target = target;
    }
    
    public TargetBuilder targetId(String id) {
        target.setTargetID(id);
        return this;
    }
    
    public TargetBuilder profileDsml() {
        target.setProfile(ListTargetsRequestBuilder.DSML_PROFILE_URI);
        return this;
    }
    
    public TargetBuilder profileXsd() {
        target.setProfile(ListTargetsRequestBuilder.XSD_PROFILE_URI);
        return this;
    }
    
    public TargetBuilder schema(SchemaBuilder... schema) {
        for (SchemaBuilder s : schema) {
            target.getSchema().add(s.build());
        }
        return this;
    }
    
    public TargetBuilder capability(String uri) {
        return capability(uri, new String[0]);
    }
    
    public TargetBuilder capability(String uri, String... entities) {
        CapabilitiesListType capabilities = target.getCapabilities();
        if (capabilities == null) {
            capabilities = new CapabilitiesListType();
            target.setCapabilities(capabilities);
        }
        CapabilityType capability = new CapabilityType();
        capability.setNamespaceURI(uri);
        if (entities != null) {
            for (String entity: entities) {
                SchemaEntityRefType ref = new SchemaEntityRefType();
                ref.setEntityName(entity);
                capability.getAppliesTo().add(ref);
            }
        }
        capabilities.getCapability().add(capability);
        return this;
    }
    
    public TargetBuilder capabilityAsync() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_ASYNC_URI);
    }
    
    public TargetBuilder capabilityPassword() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI);
    }
    
    public TargetBuilder capabilityPassword(String... entities) {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_PASSWORD_URI, entities);
    }
    
    public TargetBuilder capabilitySuspend() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI);
    }
    
    public TargetBuilder capabilitySuspend(String... entities) {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_SUSPEND_URI, entities);
    }
    
    public TargetBuilder capabilityBulk() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_BULK_URI);
    }
    
    public TargetBuilder capabilityBatch() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_BATCH_URI);
    }
    
    public TargetBuilder capabilitySearch() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_SEARCH_URI);
    }
    
    public TargetBuilder capabilityUpdates() {
        return capability(BaseRequestAccessor.SPML_CAPABILITY_UPDATES_URI);
    }
    
    @Override
    public TargetType build() {
        return target;
    }

    @Override
    public TargetAccessor asAccessor() {
        return new TargetAccessor(target);
    }
    
}
