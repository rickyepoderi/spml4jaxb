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

import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 *
 * @author ricky
 */
public class SpmlException extends Exception {
    
    private ResponseType response = null;
    
    public SpmlException() {
        super();
    }
    
    public SpmlException(String message) {
        super(message);
    }
    
    public SpmlException(Throwable t) {
        super(t);
    }
    
    public SpmlException(String message, Throwable t) {
        super(message, t);
    }
    
    public SpmlException(ResponseType response) {
        this.response = response;
    }
    
    public boolean hasResponse() {
        return this.response != null;
    }
}
