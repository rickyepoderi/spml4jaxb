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

import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordResponseType;

/**
 *
 * @author ricky
 */
public class ValidatePasswordResponseAccessor extends ResponseAccessor<ValidatePasswordResponseType> {

    protected ValidatePasswordResponseAccessor(ValidatePasswordResponseType response) {
        super(response, null);
    }
    
    public boolean isValid() {
        return response.isValid() != null && response.isValid();
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  valid: ").append(isValid()).append(nl);
        return sb.toString();
    }
    
}
