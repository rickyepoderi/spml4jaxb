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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.msg.core.LookupResponseType;

/**
 *
 * @author ricky
 */
public class LookupResponseAccessor extends ResponseAccessor<LookupResponseType> {

    protected LookupResponseAccessor(LookupResponseType response) {
        super(response, response.getPso());
    }
    
}
