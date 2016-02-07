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
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ErrorCode;
import es.rickyepoderi.spml4jaxb.msg.core.ExtensibleType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.StatusCodeType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.server.SpmlResponder;
import java.util.Arrays;
import java.util.UUID;
import javax.xml.bind.JAXBElement;

/**
 * <p>The ResponseBuilder is the base class for a SPMLv2 response builder (the
 * builder that should be used for constructing responses). It is based on
 * generics in order to easily concatenate the construction.</p>
 * 
 * <p>This class should be extended to create any SPMLv2 response (covered
 * by the standard or custom extensions). The builder always return the same
 * builder in order to concatenate the use of the builder.</p>
 * 
 * @author ricky
 * @param <R> The real SPMLv2 response object (JAXB obtained)
 * @param <B> The builder itself (used for the returning)
 * @param <A> The corresponding accessor for this builder (the counterpart)
 */
public abstract class ResponseBuilder<R extends ResponseType, B extends ResponseBuilder, 
        A extends BaseResponseAccessor> implements Builder<JAXBElement<R>, A> {

    /**
     * The real response object in SPMLv2.
     */
    protected R response = null;
    
    /**
     * The PSO type is created because a lot of responses uses it. This way
     * repetition of code is avoided.
     */
    protected PSOType pso = null;

    /**
     * Constructor based on the response.
     * @param response 
     */
    protected ResponseBuilder(R response) {
        this.response = response;
    }
    
    //
    // static method to create any standard response builder
    //
    
    /**
     * Returns a new builder to create an Add response.
     * @return A new builder for add
     */
    static public AddResponseBuilder builderForAdd() {
        return new AddResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a ListTargets response.
     * @return A new builder for list targets
     */
    static public ListTargetsResponseBuilder builderForListTargets() {
        return new ListTargetsResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a modify response.
     * @return A new builder for modify
     */
    static public ModifyResponseBuilder builderForModify() {
        return new ModifyResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Lookup response.
     * @return A new builder for lookup
     */
    static public LookupResponseBuilder builderForLookup() {
        return new LookupResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Delete response.
     * @return A new builder for delete
     */
    static public DeleteResponseBuilder builderForDelete() {
        return new DeleteResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Cancel response.
     * @return A new builder for cancel
     */
    static public CancelResponseBuilder builderForCancel() {
        return new CancelResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Status response.
     * @return A new builder for status
     */
    static public StatusResponseBuilder builderForStatus() {
        return new StatusResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a SetPassword response.
     * @return A new builder for set password
     */
    static public SetPasswordResponseBuilder builderForSetPassword() {
        return new SetPasswordResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a ExpirePassword response.
     * @return A new builder for response password
     */
    static public ExpirePasswordResponseBuilder builderForExpirePassword() {
        return new ExpirePasswordResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a ResetPassword response.
     * @return A new builder for reset password
     */
    static public ResetPasswordResponseBuilder builderForResetPassword() {
        return new ResetPasswordResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a ValidatePassword response.
     * @return A new builder for validate password
     */
    static public ValidatePasswordResponseBuilder builderForValidatePassword() {
        return new ValidatePasswordResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Suspend response.
     * @return A new builder for suspend
     */
    static public SuspendResponseBuilder builderForSuspend() {
        return new SuspendResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Resume response.
     * @return A new builder for resume
     */
    static public ResumeResponseBuilder builderForResume() {
        return new ResumeResponseBuilder();
    }
    
    /**
     * Returns a new builder to create an Active response.
     * @return A new builder for active
     */
    static public ActiveResponseBuilder builderForActive() {
        return new ActiveResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Search response.
     * @return A new builder for search
     */
    static public SearchResponseBuilder builderForSearch() {
        return new SearchResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a CloseIterator response.
     * @return A new builder for close iterator
     */
    static public CloseIteratorResponseBuilder builderForCloseIterator() {
        return new CloseIteratorResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a Target. The target is used in the 
     * ListTargets response to define the target the server handles.
     * @return A new builder for a target
     */
    static public TargetBuilder builderForTarget() {
        return new TargetBuilder();
    }
    
    /**
     * Returns a new builder to create a PSO Identifier. The PSO identifier is
     * used in a lot of responses that returns objects.
     * @return A new builder for pso identifier
     */
    static public PsoIdentifierBuilder builderForPsoIdentifier() {
        return new PsoIdentifierBuilder();
    }
    
    /**
     * Returns a new builder to create an Schema object. The schema should be
     * defined in any target in the ListTargets response.
     * @return A new builder for Schema
     */
    static public SchemaBuilder builderForSchema() {
        return new SchemaBuilder();
    }
    
    /**
     * Returns a new builder to create a Batch response.
     * @return A new builder for batch
     */
    static public BatchResponseBuilder builderForBatch() {
        return new BatchResponseBuilder();
    }
    
    /**
     * Returns a new builder to create an Updates response.
     * @return A new builder for updates
     */
    static public UpdatesResponseBuilder builderForUpdates() {
        return new UpdatesResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a BulkDelete response.
     * @return A new builder for bulk delete
     */
    static public BulkDeleteResponseBuilder builderForBulkDelete() {
        return new BulkDeleteResponseBuilder();
    }
    
    /**
     * Returns a new builder to create a BulkModify response.
     * @return A new builder for bulk modify
     */
    static public BulkModifyResponseBuilder builderForBulkModify() {
        return new BulkModifyResponseBuilder();
    }
    
    /**
     * Returns a new builder to create an UpdatesCloseIterator response.
     * @return A new builder for updates close iterator
     */
    static public UpdatesCloseIteratorResponseBuilder builderForUpdatesCloseIterator() {
        return new UpdatesCloseIteratorResponseBuilder();
    }

    /**
     * Method that changes the inner object of the builder. Sometimes it is 
     * needed to update the internal response object with a specific one. Some
     * builders that extends this should override this to set pso
     * or any other property managed for easy access.
     * 
     * @param response The new request to create the builder
     * @return The same builder
     */
    public B fromResponse(R response) {
        this.response = response;
        return (B) this;
    }
    
    //
    // Object Builders
    //

    /**
     * ObjectFactory for the core capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory getCoreObjectFactory() {
        return RequestBuilder.coreObjectFactory;
    }

    /**
     * ObjectFactory for the password capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory getPasswordObjectFactory() {
        return RequestBuilder.passwordObjectFactory;
    }

    /**
     * ObjectFactory for the suspend capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory getSuspendObjectFactory() {
        return RequestBuilder.suspendObjectFactory;
    }

    /**
     * ObjectFactory for the batch capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory getBatchObjectFactory() {
        return RequestBuilder.batchObjectFactory;
    }

    /**
     * ObjectFactory for the async capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory getAsyncObjectFactory() {
        return RequestBuilder.asyncObjectFactory;
    }

    /**
     * ObjectFactory for the bulk capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory getBulkObjectFactory() {
        return RequestBuilder.bulkObjectFactory;
    }

    /**
     * ObjectFactory for the reference capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory getReferenceObjectFactory() {
        return RequestBuilder.referenceObjectFactory;
    }

    /**
     * ObjectFactory for the search capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory getSearchObjectFactory() {
        return RequestBuilder.searchObjectFactory;
    }

    /**
     * ObjectFactory for the updates capability messages.
     */
    protected es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory getUpdatesObjectFactory() {
        return RequestBuilder.updatesObjectFactory;
    }
    
    /**
     * ObjectFactory for the dsml profiles data objects related to SPML.
     */
    protected es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory getSpmldsmlObjectFactory() {
        return RequestBuilder.spmldsmlObjectFactory;
    }
    
    /**
     * ObjectFactory for the dsml profiles data objects.
     */
    protected es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory getDsmlv2ObjectFactory() {
        return RequestBuilder.dsmlv2ObjectFactory;
    }
    
    //
    // Common method for the builder
    //
    
    /**
     * Create a request identifier for the request.
     * @param id The new identifier for the requests
     * @return The same builder
     */
    public B requestId(String id) {
        response.setRequestID(id);
        return (B) this;
    }
    
    /**
     * Create a default request identifier. UUID is used to create a 
     * unique id.
     * @return The same builder
     */
    public B requestId() {
        return this.requestId(UUID.randomUUID().toString());
    }

    /**
     * Sets the error code to the response.
     * @param error The error to set
     * @return The same builder
     */
    public B error(ErrorCode error) {
        response.setError(error);
        return (B) this;
    }

    /**
     * Sets the malformed error response.
     * @return The same builder
     */
    public B malformedRequest() {
        return error(ErrorCode.MALFORMED_REQUEST);
    }

    /**
     * Sets the unsupported error response.
     * @return The same builder
     */
    public B unsupportedOperation() {
        return error(ErrorCode.UNSUPPORTED_OPERATION);
    }

    /**
     * Sets the unsupported identifier error response.
     * @return The same builder
     */
    public B unsupportedIdentifierType() {
        return error(ErrorCode.UNSUPPORTED_IDENTIFIER_TYPE);
    }

    /**
     * Sets the no such identifier error response.
     * @return The same builder
     */
    public B noSuchIdentifier() {
        return error(ErrorCode.NO_SUCH_IDENTIFIER);
    }

    /**
     * Sets the custom error error response.
     * @return The same builder
     */
    public B customError() {
        return error(ErrorCode.CUSTOM_ERROR);
    }

    /**
     * Sets the custom error error response.
     * @return The same builder
     */
    public B unsupportedExecutionMode() {
        return error(ErrorCode.UNSUPPORTED_EXECUTION_MODE);
    }

    /**
     * Sets the custom error error response.
     * @return The same builder
     */
    public B invalidContainment() {
        return error(ErrorCode.INVALID_CONTAINMENT);
    }

    /**
     * Sets the custom error error response.
     * @return The same builder
     */
    public B noSuchRequest() {
        return error(ErrorCode.NO_SUCH_REQUEST);
    }

    /**
     * Sets the custom error error response.
     * @return The same builder
     */
    public B unsupportedSelectionType() {
        return error(ErrorCode.UNSUPPORTED_SELECTION_TYPE);
    }

    /**
     * Sets the result set too large error response.
     * @return The same builder
     */
    public B resultSetToLarge() {
        return error(ErrorCode.RESULT_SET_TO_LARGE);
    }

    /**
     * Sets the unsupported profile error response.
     * @return The same builder
     */
    public B unsupportedProfile() {
        return error(ErrorCode.UNSUPPORTED_PROFILE);
    }

    /**
     * Sets the invalid identifier error response.
     * @return The same builder
     */
    public B invalidIdentifier() {
        return error(ErrorCode.INVALID_IDENTIFIER);
    }

    /**
     * Sets the already exists error response.
     * @return The same builder
     */
    public B alreadyExists() {
        return error(ErrorCode.ALREADY_EXISTS);
    }

    /**
     * Sets the container not empty error response.
     * @return The same builder
     */
    public B containerNotEmpty() {
        return error(ErrorCode.CONTAINER_NOT_EMPTY);
    }
    
    /**
     * Set the status of the response.
     * @param status The status to set
     * @return The same builder
     */
    public B status(StatusCodeType status) {
        response.setStatus(status);
        return(B) this;
    }
    
    /**
     * set the status to success,
     * @return The same builder
     */
    public B success() {
        return status(StatusCodeType.SUCCESS);
    }
    
    /**
     * set the status to failure,
     * @return The same builder
     */
    public B failure() {
        return status(StatusCodeType.FAILURE);
    }
    
    /**
     * set the status to pending,
     * @return The same builder
     */
    public B pending() {
        return status(StatusCodeType.PENDING);
    }
    
    /**
     * Adds a new error message to the response.
     * @param mess The new message to add
     * @return The same builder
     */
    public B errorMessage(String mess) {
        response.getErrorMessage().add(mess);
        return (B) this;
    }
    
    //
    // PSO Type methods
    //
    
    /**
     * Assign a full PSO type to the PSO.
     * @param pso The pso to assign
     * @return The same builder
     */
    public B pso(PSOType pso) {
        this.pso = pso;
        return (B) this;
    }
    
    /**
     * Assigns the PSO od to the PSO type.
     * @param psoId The pso id to assign
     * @return The same builder
     */
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

    /**
     * Assigns the target identifier to the pso type.
     * @param psoTargetId The target id to assign
     * @return The same builder
     */
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
    
    /**
     * Assign a pso identifier to the type using the builder.
     * @param psoId The pso identifier builder
     * @return The same builder
     */
    public B psoIdentifier(PsoIdentifierBuilder psoId) {
        if (pso == null) {
            pso = new PSOType();
        }
        pso.setPsoID(psoId.build());
        return (B) this;
    }
    
    /**
     * Assigns a XSD data object as the data for the PSO type. Obviously
     * it should be used in XSD profile targets.
     * @param o The object to assign in the pso type
     * @return The same builder
     */
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
    
    /**
     * Adds some DSML attributes to the PSO type. It should be used for the 
     * DSML profile targets.
     * @param attrs The list of DSML attributes to add to the pso type
     * @return The same builder
     */
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
    
    /**
     * Adds a new DSML attribute to the PSO type. It should be used for the 
     * DSML profile targets.
     * @param name The name of the attribute
     * @param values The values of the attribute
     * @return The same builder
     */
    public B dsmlAttribute(String name, String... values) {
        DsmlAttr attr = new DsmlAttr();
        attr.setName(name);
        attr.getValue().addAll(Arrays.asList(values));
        this.dsmlAttribute(attr);
        return (B) this;
    }
    
    public B operationalObject(Object o) {
        this.response.getAny().add(o);
        return (B) this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    abstract public JAXBElement<R> build();
    
    public B send(SpmlResponder writer) throws SpmlException {
        writer.write(this.build());
        return (B) this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    abstract public A asAccessor();
    
}
