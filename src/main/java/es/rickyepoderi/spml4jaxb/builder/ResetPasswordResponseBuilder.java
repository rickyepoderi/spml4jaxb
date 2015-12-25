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

import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordResponseType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class ResetPasswordResponseBuilder extends ResponseBuilder<ResetPasswordResponseType, ResetPasswordResponseBuilder> {

    protected ResetPasswordResponseBuilder() {
        super(new ResetPasswordResponseType());
    }
    
    public ResetPasswordResponseBuilder password(String password) {
        response.setPassword(password);
        return this;
    }

    @Override
    public JAXBElement<ResetPasswordResponseType> build() {
        return getPasswordObjectFactory().createResetPasswordResponse(response);
    }
    
}
