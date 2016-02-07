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
 * <p>SpmlClient is an abstract class to be used as a base SPMLv2 client.
 * The client should implement the Closeable and the SPMLRequestor 
 * interfaces. As spml4jaxb is based on JAXB the client uses a JAXBContext
 * constructed with several factories. The client let the developer to add
 * other factories to the ones specified in the standard and the library or
 * just using the ones passed as argument.</p>
 * 
 * @author ricky
 */
public abstract class SpmlClient implements Closeable, SpmlRequestor {
    
    /**
     * JAXBContext to send and receive SPMLv2 messages.
     */
    protected JAXBContext ctx = null;
    
    /**
     * Empty constructor that creates the JAXB context with the default
     * factories of the library.
     * 
     * @throws SpmlException Some error with JAXB
     */
    public SpmlClient() throws SpmlException {
        this(true);
    }
    
    /**
     * Constructor giving the factories to add to the JAXB context. The first
     * boolean argument means if the given factories should be added to the
     * default ones or used alone.
     * 
     * @param append if true the factories are appended to the default ones, 
     *               if false just the passed as argument are used
     * @param factories The factories to be used (appended or alone)
     * @throws SpmlException Some error with JAXB
     */
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
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ResponseAccessor send(Object request) throws SpmlException;
    
    /**
     * Close method for closeable.
     */
    @Override
    public abstract void close() throws IOException;
}
