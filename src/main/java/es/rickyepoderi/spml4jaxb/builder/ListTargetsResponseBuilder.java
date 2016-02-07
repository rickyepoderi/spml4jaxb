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

import es.rickyepoderi.spml4jaxb.accessor.ListTargetsResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the ListTargets response. The ListTargets operation is a function 
 * in the core capability as defined in SPMLv2. This operation is the first
 * operation that a client should send to a SPMLv2 server. The response gives
 * information about the SPMLv2 server, mainly the server manages one or more
 * targets. Each target represents a repository of objects to be managed
 * using SPMLv2. The ListTargets response returns the targets the server
 * manages and information about each one:</p>
 * 
 * <ol>
 * <li>The target should follow a profile. The profile describes how the data
 * should be represented inside the target. SPMLv2 defines two profiles:
 * XSD (data is managed using XML information based on a custom XSD file) and
 * DSML (data is managed using attributes more or less like in an LDAP 
 * repository).</li>
 * <li>The target should contain the schema of the data managed. In XSD profile
 * the schema is the XSD definition file of the interchanged objects. In DSML
 * profile the schema is the list of attributes and types of objects managed
 * (similar to objectclasses and attributes in an LDAP server).</li>
 * <li>Finally the list of capabilities supported by the target are exposed in
 * the response. SPMLv2 groups operations in different capabilities (the core
 * capability -CRUD- should be implemented by all SPMLv2 compatible servers but 
 * the rest of them are optional and only needed to cover more specific 
 * characteristics), the target information should inform what optional
 * capabilities are supported.</li>
 * </ol>
 * 
 * <p>The idea of the ListTargets is giving enough information about the target(s)
 * in the server to permit an easy access to the client. That is the reason why
 * this operation should be the first one requested by a client. Some servers
 * implementations (for the previous reason) extends the functionality of the
 * ListTargets method to perform a login.</p>
 * 
 * <p>The ListTargets information is quite complicated in the response, each
 * target has a specific builder to examine the data in the ListTargets.</p>
 * 
 * @author ricky
 */
public class ListTargetsResponseBuilder extends ResponseBuilder<ListTargetsResponseType, ListTargetsResponseBuilder, ListTargetsResponseAccessor> {

    /**
     * Constructor for an empty ListTargets response buidler.
     */
    protected ListTargetsResponseBuilder() {
        super(new ListTargetsResponseType());
    }
    
    /**
     * Adds a new target to the response.
     * @param target The target builder to be added
     * @return The same builder
     */
    public ListTargetsResponseBuilder target(TargetBuilder... target) {
        for (TargetBuilder t: target) {
            response.getTarget().add(t.build());
        }
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ListTargetsResponseType> build() {
        return getCoreObjectFactory().createListTargetsResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ListTargetsResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asListTargets();
    }
}
