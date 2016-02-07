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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ValidatePasswordResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordResponseType;
import javax.xml.bind.JAXBElement;

/**
 * <p>Builder for the SPMLv2 ValidatePassword operation response. The validate password
 * operation is defined inside the password capability (capability to perform
 * password management). This operation is used to verify the value of an object
 * that supports the password capability (user). The response only has the
 * valid boolean field (which indicates if the password send was ok or not).</p>
 * 
 * @author ricky
 */
public class ValidatePasswordResponseBuilder extends ResponseBuilder<ValidatePasswordResponseType, ValidatePasswordResponseBuilder, ValidatePasswordResponseAccessor> {

    /**
     * Constructor for a new validate password response builder.
     */
    protected ValidatePasswordResponseBuilder() {
        super(new ValidatePasswordResponseType());
    }
    
    /**
     * Setter for the valid response (the password was ok or not).
     * @param valid The check result (true=ok, false=ko)
     * @return The same buidler
     */
    public ValidatePasswordResponseBuilder valid(boolean valid) {
        response.setValid(valid);
        return this;
    }
    
    /**
     * Setter for the validation to ok.
     * @return The same builder
     */
    public ValidatePasswordResponseBuilder valid() {
        response.setValid(true);
        return this;
    }
    
    /**
     * Setter for the validation to ko.
     * @return The same builder
     */
    public ValidatePasswordResponseBuilder notValid() {
        response.setValid(false);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAXBElement<ValidatePasswordResponseType> build() {
        return getPasswordObjectFactory().createValidatePasswordResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatePasswordResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asValidatePassword();
    }
    
}