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
package es.rickyepoderi.spml4jaxb.server;

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import java.util.Map;
import javax.xml.bind.JAXBContext;

/**
 *
 * @author ricky
 */
public interface SpmlMapperExecutorFactory {
    
    Map<Class, SpmlExecutor> createMapper(JAXBContext ctx) throws SpmlException;
    
}
