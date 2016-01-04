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
package es.rickyepoderi.spml4jaxb.user;

/**
 *
 * @author ricky
 */
public class AlreadyExistsException extends ManagerException {

    public AlreadyExistsException() {
        super("User already exists!");
    }

    public AlreadyExistsException(Throwable t) {
        super("User already exists!", t);
    }
}
