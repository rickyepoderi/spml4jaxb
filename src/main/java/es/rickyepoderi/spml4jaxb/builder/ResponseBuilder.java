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

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import static es.rickyepoderi.spml4jaxb.builder.RequestBuilder.coreObjectFactory;
import es.rickyepoderi.spml4jaxb.msg.core.ErrorCode;
import es.rickyepoderi.spml4jaxb.msg.core.ExtensibleType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.StatusCodeType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.server.SpmlResponder;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 * @param <R>
 * @param <B>
 */
public abstract class ResponseBuilder<R extends ResponseType, B extends ResponseBuilder> implements Builder<JAXBElement<R>> {

    protected R response = null;
    protected PSOType pso = null;

    protected ResponseBuilder(R response) {
        this.response = response;
    }
    
    static public AddResponseBuilder builderForAdd() {
        return new AddResponseBuilder();
    }
    
    static public ListTargetsResponseBuilder builderForListTargets() {
        return new ListTargetsResponseBuilder();
    }
    
    static public ModifyResponseBuilder builderForModify() {
        return new ModifyResponseBuilder();
    }
    
    static public LookupResponseBuilder builderForLookup() {
        return new LookupResponseBuilder();
    }
    
    static public DeleteResponseBuilder builderForDelete() {
        return new DeleteResponseBuilder();
    }
    
    static public CancelResponseBuilder builderForCancel() {
        return new CancelResponseBuilder();
    }
    
    static public StatusResponseBuilder builderForStatus() {
        return new StatusResponseBuilder();
    }
    
    static public SetPasswordResponseBuilder builderForSetPassword() {
        return new SetPasswordResponseBuilder();
    }
    
    static public ExpirePasswordResponseBuilder builderForExpirePassword() {
        return new ExpirePasswordResponseBuilder();
    }
    
    static public ResetPasswordResponseBuilder builderForResetPassword() {
        return new ResetPasswordResponseBuilder();
    }
    
    static public ValidatePasswordResponseBuilder builderForValidatePassword() {
        return new ValidatePasswordResponseBuilder();
    }
    
    static public SuspendResponseBuilder builderForSuspend() {
        return new SuspendResponseBuilder();
    }
    
    static public ResumeResponseBuilder builderForResume() {
        return new ResumeResponseBuilder();
    }
    
    static public ActiveResponseBuilder builderForActive() {
        return new ActiveResponseBuilder();
    }
    
    static public SearchResponseBuilder builderForSearch() {
        return new SearchResponseBuilder();
    }
    
    static public CloseIteratorResponseBuilder builderForCloseIterator() {
        return new CloseIteratorResponseBuilder();
    }
    
    static public TargetBuilder builderForTarget() {
        return new TargetBuilder();
    }
    
    static public SchemaBuilder builderForSchema() {
        return new SchemaBuilder();
    }
    
    static public BatchResponseBuilder builderForBatch() {
        return new BatchResponseBuilder();
    }
    
    static public UpdatesResponseBuilder builderForUpdates() {
        return new UpdatesResponseBuilder();
    }
    
    static public BulkDeleteResponseBuilder builderForBulkDelete() {
        return new BulkDeleteResponseBuilder();
    }
    
    static public BulkModifyResponseBuilder builderForBulkModify() {
        return new BulkModifyResponseBuilder();
    }
    
    static public UpdatesCloseIteratorResponseBuilder builderForUpdatesCloseIterator() {
        return new UpdatesCloseIteratorResponseBuilder();
    }

    public B fromResponse(R response) {
        this.response = response;
        return (B) this;
    }
    
    //
    // Object Builders
    //

    protected es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory getCoreObjectFactory() {
        return coreObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory getPasswordObjectFactory() {
        return RequestBuilder.passwordObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory getSuspendObjectFactory() {
        return RequestBuilder.suspendObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory getBatchObjectFactory() {
        return RequestBuilder.batchObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory getAsyncObjectFactory() {
        return RequestBuilder.asyncObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory getBulkObjectFactory() {
        return RequestBuilder.bulkObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory getReferenceObjectFactory() {
        return RequestBuilder.referenceObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory getSearchObjectFactory() {
        return RequestBuilder.searchObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory getUpdatesObjectFactory() {
        return RequestBuilder.updatesObjectFactory;
    }
    
    protected es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory getSpmldsmlObjectFactory() {
        return RequestBuilder.spmldsmlObjectFactory;
    }
    
    protected es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory getDsmlv2ObjectFactory() {
        return RequestBuilder.dsmlv2ObjectFactory;
    }
    
    public B requestId(String id) {
        response.setRequestID(id);
        return (B) this;
    }

    public B error(ErrorCode error) {
        response.setError(error);
        return (B) this;
    }

    public B malformedRequest() {
        return error(ErrorCode.MALFORMED_REQUEST);
    }

    public B unsupportedOperation() {
        return error(ErrorCode.UNSUPPORTED_OPERATION);
    }

    public B unsupportedIdentifierType() {
        return error(ErrorCode.UNSUPPORTED_IDENTIFIER_TYPE);
    }

    public B noSuchIdentifier() {
        return error(ErrorCode.NO_SUCH_IDENTIFIER);
    }

    public B customError() {
        return error(ErrorCode.CUSTOM_ERROR);
    }

    public B unsupportedExecutionMode() {
        return error(ErrorCode.UNSUPPORTED_EXECUTION_MODE);
    }

    public B invalidContainment() {
        return error(ErrorCode.INVALID_CONTAINMENT);
    }

    public B noSuchRequest() {
        return error(ErrorCode.NO_SUCH_REQUEST);
    }

    public B unsupportedSelectionType() {
        return error(ErrorCode.UNSUPPORTED_SELECTION_TYPE);
    }

    public B resultSetToLarge() {
        return error(ErrorCode.RESULT_SET_TO_LARGE);
    }

    public B unsupportedProfile() {
        return error(ErrorCode.UNSUPPORTED_PROFILE);
    }

    public B invalidIdentifier() {
        return error(ErrorCode.INVALID_IDENTIFIER);
    }

    public B alreadyExists() {
        return error(ErrorCode.ALREADY_EXISTS);
    }

    public B containerNotEmpty() {
        return error(ErrorCode.CONTAINER_NOT_EMPTY);
    }
    
    public B status(StatusCodeType status) {
        response.setStatus(status);
        return(B) this;
    }
    
    public B success() {
        return status(StatusCodeType.SUCCESS);
    }
    
    public B failure() {
        return status(StatusCodeType.FAILURE);
    }
    
    public B pending() {
        return status(StatusCodeType.PENDING);
    }
    
    public B errorMessage(String mess) {
        response.getErrorMessage().add(mess);
        return (B) this;
    }
    
    public B pso(PSOType pso) {
        this.pso = pso;
        return (B) this;
    }
    
    public B psoId(String psoId) {
        if (pso == null) {
            pso = new PSOType();
        }
        if (pso.getPsoID() == null) {
            pso.setPsoID(new PSOIdentifierType());
        }
        pso.getPsoID().setID(psoId);
        return (B) this;
    }

    public B psoTargetId(String psoTargetId) {
        if (pso == null) {
            pso = new PSOType();
        }
        if (pso.getPsoID() == null) {
            pso.setPsoID(new PSOIdentifierType());
        }
        pso.getPsoID().setTargetID(psoTargetId);
        return (B) this;
    }
    
    public B xsdObject(Object o) {
        if (pso == null) {
            pso = new PSOType();
        }
        if (pso.getData() == null) {
            pso.setData(new ExtensibleType());
        }
        ExtensibleType ext = pso.getData();
        ext.getAny().add(o);
        return (B) this;
    }
    
    public B dsmlAttribute(DsmlAttr... attrs) {
        if (pso == null) {
            pso = new PSOType();
        }
        if (pso.getData() == null) {
            pso.setData(new ExtensibleType());
        }
        ExtensibleType ext = pso.getData();
        for (DsmlAttr attr : attrs) {
            if (ext == null) {
                ext = new ExtensibleType();
                pso.setData(ext);
            }
            ext.getAny().add(getDsmlv2ObjectFactory().createAttr(attr));
        }
        return (B) this;
    }
    
    public B dsmlAttribute(String name, String... values) {
        DsmlAttr attr = new DsmlAttr();
        attr.setName(name);
        attr.getValue().addAll(Arrays.asList(values));
        this.dsmlAttribute(attr);
        return (B) this;
    }
    
    @Override
    abstract public JAXBElement<R> build();
    
    public B send(SpmlResponder writer) throws SpmlException {
        writer.write(this.build());
        return (B) this;
    }
    
    public ResponseAccessor asAccessor() {
        return ResponseAccessor.accessorForResponse(response);
    }
}
