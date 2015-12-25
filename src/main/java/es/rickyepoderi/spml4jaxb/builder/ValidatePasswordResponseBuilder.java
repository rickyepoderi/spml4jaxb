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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordResponseType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ValidatePasswordResponseBuilder extends ResponseBuilder<ValidatePasswordResponseType, ValidatePasswordResponseBuilder> {

    protected ValidatePasswordResponseBuilder() {
        super(new ValidatePasswordResponseType());
    }
    
    public ValidatePasswordResponseBuilder valid(boolean valid) {
        response.setValid(valid);
        return this;
    }
    
    public ValidatePasswordResponseBuilder valid() {
        response.setValid(true);
        return this;
    }
    
    public ValidatePasswordResponseBuilder notValid() {
        response.setValid(false);
        return this;
    }

    @Override
    public JAXBElement<ValidatePasswordResponseType> build() {
        return getPasswordObjectFactory().createValidatePasswordResponse(response);
    }
    
}
