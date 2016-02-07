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

import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>SpmlException to be used inside clients. The idea of the exception is
 * making the client as generic as possible.</p>
 * 
 * @author ricky
 */
public class SpmlException extends Exception {
    
    /**
     * Empty constructor.
     */
    public SpmlException() {
        super();
    }
    
    /**
     * Constructor using the message.
     * 
     * @param message The message for the exception
     */
    public SpmlException(String message) {
        super(message);
    }
    
    /**
     * Constructor with a cause.
     * 
     * @param t The cause exception
     */
    public SpmlException(Throwable t) {
        super(t);
    }
    
    /**
     * Constructor with a cause but using another message.
     * 
     * @param message The message for the exception
     * @param t The cause exception
     */
    public SpmlException(String message, Throwable t) {
        super(message, t);
    }
    
}
