/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 */
public class CloneResponseBuilder extends ResponseBuilder<CloneResponseType, CloneResponseBuilder, CloneResponseAccessor> {

    protected CloneResponseBuilder() {
        super(new CloneResponseType());
    }
    
    public ObjectFactory getCloneObjectFactory() {
        return CloneRequestBuilder.cloneObjectFactory;
    }
    
    static public CloneResponseBuilder builderForClone() {
        return new CloneResponseBuilder();
    }
    
    @Override
    public JAXBElement<CloneResponseType> build() {
        response.setPso(pso);
        return getCloneObjectFactory().createCloneResponseType(response);
    }

    @Override
    public CloneResponseAccessor asAccessor() {
        return BaseResponseAccessor.accessorForResponse(response).asAccessor(new CloneResponseAccessor());
    }
    
    @Override
    public CloneResponseBuilder fromResponse(CloneResponseType response) {
        this.response = response;
        this.pso = response.getPso();
        return this;
    }
    
}
