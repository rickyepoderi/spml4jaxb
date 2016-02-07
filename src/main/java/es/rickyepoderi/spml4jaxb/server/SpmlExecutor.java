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

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;

/**
 * <p>Interface to execute a SPMLv2 operation. The idea is a server uses
 * different SPML executors to execute each supported operation. For example
 * a server that wants to handle the core SPMLv2 capability needs a 
 * ListTargetsExecutor, AddExecutor, ModifyExecutor, DeleteExecutor and
 * Lookupexecutor. With those five executors the server knows to answer
 * any operation in the core capability eschema.</p>
 * 
 * @author ricky
 */
public interface SpmlExecutor {
    
    /**
     * Main method that executes the SPMLv2 operation. It receives any 
     * request and returns the response builder ready to be written.
     * 
     * @param request The SPMLv2 request to execute as an accessor
     * @return The response builder fully filled and ready to be written
     */
    public ResponseBuilder execute(RequestAccessor request);
}
