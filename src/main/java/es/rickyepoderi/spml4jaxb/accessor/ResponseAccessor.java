/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;

/**
 *
 * @author ricky
 */
final public class ResponseAccessor extends BaseResponseAccessor<ResponseType, BaseResponseAccessor, ResponseBuilder> {

    public ResponseAccessor(ResponseType response) {
        super(response, null);
    }

    @Override
    public ResponseBuilder toBuilder() {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }

    @Override
    public BaseResponseAccessor asAccessor(ResponseType response) {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }
    
}
