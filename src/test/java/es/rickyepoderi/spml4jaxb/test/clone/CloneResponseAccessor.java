/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test.clone;

import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;

/**
 *
 * @author ricky
 */
public class CloneResponseAccessor extends BaseResponseAccessor<CloneResponseType, CloneResponseAccessor, CloneResponseBuilder> {

    protected CloneResponseAccessor() {
        this(new CloneResponseType());
    }
    
    protected CloneResponseAccessor(CloneResponseType response) {
        super(response, response.getPso());
    }
    
    static public CloneResponseAccessor emptyResponseAccessor() {
        return new CloneResponseAccessor();
    }
    
    @Override
    public CloneResponseBuilder toBuilder() {
        return CloneResponseBuilder.builderForClone().fromResponse(this.response);
    }

    @Override
    public CloneResponseAccessor asAccessor(CloneResponseType response) {
        return new CloneResponseAccessor(response);
    }
    
}
