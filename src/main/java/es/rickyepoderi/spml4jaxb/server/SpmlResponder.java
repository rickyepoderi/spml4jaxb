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

/**
 * <p>The SpmlResponder is a simple interface used in a server to write a
 * response (JAXB should be used to convert into a XML document) to whatever
 * channel</p>
 * 
 * @author ricky
 */
public interface SpmlResponder {
    
    /**
     * Main method to write the response to anything.
     * 
     * @param response The response to be written (JAXB needed)
     * @throws SpmlException Some error writing the response
     */
    public void write(Object response) throws SpmlException;
    
}
