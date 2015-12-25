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
package es.rickyepoderi.spml4jaxb.client;

import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import java.io.Closeable;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author ricky
 */
public abstract class SpmlClient implements Closeable, SpmlRequestor {
    
    protected JAXBContext ctx = null;
    
    public SpmlClient() throws SpmlException {
        // default for DSML
        this(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class);
    }
    
    public SpmlClient(Class... factories) throws SpmlException {
        try {
            ctx = JAXBContext.newInstance(factories);
        } catch(JAXBException e) {
            throw new SpmlException(e);
        }
    }
    
    @Override
    public abstract ResponseAccessor send(Object request) throws SpmlException;
    
    @Override
    public abstract void close() throws IOException;
}
