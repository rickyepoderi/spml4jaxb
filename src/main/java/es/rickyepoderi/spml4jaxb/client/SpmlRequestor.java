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

/**
 * <p>The SpmlRequestor is just an interface that should implement any class
 * that wants to be used inside a RequestBuilder to send a SPML request
 * to a server.</p>
 * 
 * @author ricky
 */
public interface SpmlRequestor {
    
    /**
     * The send method that receives any object (JAXB is used to transform
     * it into a XML document) and sends it to a server. The response is
     * a generic response accessor which can be transformed to any specific
     * response later.
     * 
     * @param request The request object (JAXB needed to convert into XML)
     * @return A generic response accessor returned by the server
     * @throws SpmlException Some error sending the request or receiving the response
     */
    public abstract ResponseAccessor send(Object request) throws SpmlException;
}
