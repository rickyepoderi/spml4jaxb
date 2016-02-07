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
 * <p>Accessor for the SPMLv2 ValidatePassword operation response. The validate password
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to verify the value of an object
 * that supports the password capability (user). The response only has the
 * valid boolean field (which indicates if the password send was ok or not).</p>
 * 
 * @author ricky
 */
public class ValidatePasswordResponseAccessor extends BaseResponseAccessor<ValidatePasswordResponseType, ValidatePasswordResponseAccessor, ValidatePasswordResponseBuilder> {

    /**
     * Constructor for a new validate password response accessor.
     */
    protected ValidatePasswordResponseAccessor() {
        this(new ValidatePasswordResponseType());
    }
    
    /**
     * Constructor for a validate password response accessor using the internal type.
     * @param response The internal validate password response type as defined in SPMLv2
     */
    protected ValidatePasswordResponseAccessor(ValidatePasswordResponseType response) {
        super(response, null);
    }
    
    /**
     * Getter for the password validation result. Obviously first the
     * response should be a Success (if it is a failure this property maybe is
     * not filled).
     * 
     * @return if true the password sent was ok, false the password was wrong
     */
    public boolean isValid() {
        return response.isValid() != null && response.isValid();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("  valid: ").append(isValid()).append(nl);
        return sb.toString();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordResponseBuilder toBuilder() {
        return ResponseBuilder.builderForValidatePassword().fromResponse(this.response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordResponseAccessor asAccessor(ValidatePasswordResponseType response) {
        return new ValidatePasswordResponseAccessor(response);
    }
    
}