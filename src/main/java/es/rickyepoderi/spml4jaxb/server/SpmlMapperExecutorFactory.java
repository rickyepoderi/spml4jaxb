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
package es.rickyepoderi.spml4jaxb.server;

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import java.util.Map;
import javax.xml.bind.JAXBContext;

/**
 * <p>A mapper that connects a RequestType class to a SPML executor. The idea
 * is this mapper gives to a server the executor for a specified request.
 * This class is just a factory for this map.</p>
 * @author ricky
 */
public interface SpmlMapperExecutorFactory {
    
    /**
     * Creates a mapper for SPMLv2 operations. The map is keyed by the RequestType
     * class (the request received) and the value is the executor that deals
     * with this kind of request.
     * 
     * @param ctx The JAXBContext used for JAXB (parsing requests and responses)
     * @return The mapper of requests and executors
     * @throws SpmlException Some error creating the mapper
     */
    Map<Class<? extends RequestType>, SpmlExecutor> createMapper(JAXBContext ctx) throws SpmlException;
    
}
