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
package es.rickyepoderi.spml4jaxb.client;

import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author ricky
 */
public abstract class SpmlClient implements Closeable, SpmlRequestor {
    
    protected JAXBContext ctx = null;
    
    public SpmlClient() throws SpmlException {
        this(true);
    }
    
    public SpmlClient(boolean append, Class... factories) throws SpmlException {
        try {
            if (append) {
                Set<Class> facts = new HashSet();
                facts.addAll(Arrays.asList(RequestBuilder.getAllSpmlv2ObjectFactoryClasses()));
                facts.addAll(Arrays.asList(factories));
                ctx = JAXBContext.newInstance(facts.toArray(new Class[0]));
            } else {
                ctx = JAXBContext.newInstance(factories);
            }
        } catch(JAXBException e) {
            throw new SpmlException(e);
        }
    }
    
    @Override
    public abstract ResponseAccessor send(Object request) throws SpmlException;
    
    @Override
    public abstract void close() throws IOException;
}
