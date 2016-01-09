/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;

/**
 *
 * @author ricky
 */
final public class RequestAccessor extends BaseRequestAccessor<RequestType, BaseRequestAccessor, RequestBuilder> {

    protected RequestAccessor(RequestType request) {
        super(request, null, null);
    }

    @Override
    public RequestBuilder toBuilder() {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }

    @Override
    public BaseRequestAccessor asAccessor(RequestType request) {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }

    @Override
    public ResponseBuilder responseBuilder() {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }
    
}
