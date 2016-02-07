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
 * <p>In case of accessors a unknown accessor is used when the implementation
 * does not know what specific accessor has to return. For example when a
 * request is received by a server implementation this RequestAccessor
 * is generated, this class does not know what is the exact type of the internal
 * request. Then the developer can transform it to a knwon type using the asXXX 
 * methods. So this class represents an accessor of an unknown internal request
 * type and throws an exception (IllegalStateException) in any particular 
 * method.</p>
 * 
 * @author ricky
 */
final public class RequestAccessor extends BaseRequestAccessor<RequestType, BaseRequestAccessor, RequestBuilder> {

    /**
     * Constructor for a request accessor given the parent request.
     * @param request The base request type as specified in the standard
     */
    protected RequestAccessor(RequestType request) {
        super(request, null, null);
    }

    /**
     * This is the unknown request accessor so it does not know the specific
     * builder to use. It throws an IllegalStateException
     * @return nothing, it throws an IllegalStateException
     */
    @Override
    public RequestBuilder toBuilder() {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }

    /**
     * This is the unknown request accessor so it does not know the specific
     * accessor to use. It throws an IllegalStateException
     * @return nothing, it throws an IllegalStateException
     */
    @Override
    public BaseRequestAccessor asAccessor(RequestType request) {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }

    /**
     * This is the unknown request accessor so it does not know the specific
     * response builder to use. It throws an IllegalStateException
     * @return nothing, it throws an IllegalStateException
     */
    @Override
    public ResponseBuilder responseBuilder() {
        throw new IllegalStateException("Method not usable at this point, please use one of the \"as\" methods to transform into a real accessor.");
    }
    
}
