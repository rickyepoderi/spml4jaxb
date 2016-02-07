/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 * <p>In case of accessors a unknown accessor is used when the implementation
 * does not know what specific accessor has to return. For example the 
 * sendGeneric method of a request builder returns an accessor of an unknown
 * type (the sendGeneric method is generic and therefore does not know
 * what is the exact type of response that is returned). Then the developer can 
 * transform it to a knwon type using the asXXX  methods. So this class 
 * represents an accessor of an unknown internal response type and throws an 
 * exception (IllegalStateException) in any particular method.</p>
 * 
 * @author ricky
 */
final public class ResponseAccessor extends BaseResponseAccessor<ResponseType, BaseResponseAccessor, ResponseBuilder> {
 
    /**
     * Constructor for a response builder receiving the base response type
     * as defined in the standard.
     * @param response The base response type specified in SPMLv2 and generated by JAXB
     */
    public ResponseAccessor(ResponseType response) {
        super(response, null);
    }

    /**
     * This is the unknown response accessor so it does not know the specific
     * builder to use. It throws an IllegalStateException
     * @return nothing, it throws an IllegalStateException
     */
    @Override
    public ResponseBuilder toBuilder() {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }

    /**
     * This is the unknown response accessor so it does not know the specific
     * accessor to use. It throws an IllegalStateException
     * @return nothing, it throws an IllegalStateException
     */
    @Override
    public BaseResponseAccessor asAccessor(ResponseType response) {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }
    
}
