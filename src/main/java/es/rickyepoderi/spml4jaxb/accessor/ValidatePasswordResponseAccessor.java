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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ValidatePasswordResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordResponseType;

/**
 *
 * @author ricky
 */
public class ValidatePasswordResponseAccessor extends ResponseAccessor<ValidatePasswordResponseType, ValidatePasswordResponseAccessor, ValidatePasswordResponseBuilder> {

    protected ValidatePasswordResponseAccessor() {
        this(new ValidatePasswordResponseType());
    }
    
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
    
    @Override
    public ValidatePasswordResponseBuilder toBuilder() {
        return ResponseBuilder.builderForValidatePassword().fromResponse(this.response);
    }

    @Override
    public ValidatePasswordResponseAccessor asAccessor(ValidatePasswordResponseType response) {
        return new ValidatePasswordResponseAccessor(response);
    }
    
}
