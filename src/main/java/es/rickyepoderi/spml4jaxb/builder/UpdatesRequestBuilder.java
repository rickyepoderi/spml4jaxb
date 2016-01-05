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

import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.UpdatesRequestAccessor;
import static es.rickyepoderi.spml4jaxb.builder.RequestBuilder.dataTypeFactory;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class UpdatesRequestBuilder extends RequestBuilder<UpdatesRequestType, UpdatesRequestBuilder, UpdatesRequestAccessor> {

    protected UpdatesRequestBuilder() {
        super(new UpdatesRequestType());
    }
    
    public UpdatesRequestBuilder query(SearchQueryBuilder query) {
        request.setQuery(query.build());
        return this;
    }
    
    public UpdatesRequestBuilder updatedByCapability(String... capability) {
        request.getUpdatedByCapability().addAll(Arrays.asList(capability));
        return this;
    }
    
    public UpdatesRequestBuilder updatedSince(Date since) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(since);
        return updatedSince(c);
    }
    
    public UpdatesRequestBuilder updatedSince(Calendar since) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(since.getTime());
        return updatedSince(c);
    }
    
    public UpdatesRequestBuilder updatedSince(GregorianCalendar since) {
        request.setUpdatedSince(dataTypeFactory.newXMLGregorianCalendar(since));
        return this;
    }
    
    public UpdatesRequestBuilder token(String token) {
        request.setToken(token);
        return this;
    }
    
    public UpdatesRequestBuilder maxSelect(int size) {
        request.setMaxSelect(size);
        return this;
    }

    @Override
    public JAXBElement<UpdatesRequestType> build() {
        return getUpdatesObjectFactory().createUpdatesRequest(request);
    }
    
    @Override
    public UpdatesRequestAccessor asAccessor() {
        return RequestAccessor.accessorForRequest(request).asUpdates();
    }

    @Override
    public UpdatesRequestBuilder fromRequest(UpdatesRequestType request) {
        this.request = request;
        return this;
    }
    
}
