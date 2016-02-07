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
import es.rickyepoderi.spml4jaxb.client.SpmlRequestor;
import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.BaseResponseAccessor;
import es.rickyepoderi.spml4jaxb.accessor.ResponseAccessor;
import es.rickyepoderi.spml4jaxb.msg.core.ExecutionModeType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ReturnDataType;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * <p>The RequestBuilder is the base class for a SPMLv2 request builder (the
 * builder that should be used for constructing requests). It is based on
 * generics in order to easily concatenate the construction.</p>
 * 
 * <p>This class should be extended to create any SPMLv2 request (covered
 * by the standard or custom extensions). The builder always return the same
 * builder in order to concatenate the use of the builder.</p>
 * 
 * @author ricky
 * @param <R> The real SPMLv2 request object (JAXB obtained)
 * @param <B> The builder itself (used for the returning)
 * @param <A> The corresponding accessor for this builder (the counterpart)
 * @param <RA> The response accessor for the request (it is mainly used to return this in the send method)
 */
public abstract class RequestBuilder<R extends RequestType, B extends RequestBuilder, 
        A extends BaseRequestAccessor, RA extends BaseResponseAccessor> implements Builder<JAXBElement<R>, A> {
    
    /**
     * Logger for the class.
     */
    protected static final Logger log = Logger.getLogger(RequestBuilder.class.getName());
    
    /**
     * Standard profile URI for the DSML profile.
     */
    static public final String DSML_PROFILE_URI = "urn:oasis:names:tc:SPML:2:0:DSML";
    
    /**
     * Standard profile URI for the XSD profile.
     */
    static public final String XSD_PROFILE_URI = "urn:oasis:names:tc:SPML:2.0:profiles:XSD";
    
    /**
     * DatatypeFactory to create XML data.
     */
    static protected DatatypeFactory dataTypeFactory;
    
    /**
     * The document builder for DOM elements.
     */
    static protected final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    
    //
    // Default factories for the standard objects defined by the SPMLv2 standard
    //
    
    /**
     * ObjectFactory for the core capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory coreObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory();
    
    /**
     * ObjectFactory for the password capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory passwordObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory();
    
    /**
     * ObjectFactory for the suspend capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory suspendObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory();
    
    /**
     * ObjectFactory for the batch capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory batchObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory();
    
    /**
     * ObjectFactory for the async capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory asyncObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory();
    
    /**
     * ObjectFactory for the bulk capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory bulkObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory();
    
    /**
     * ObjectFactory for the reference capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory referenceObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory();
    
    /**
     * ObjectFactory for the search capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory searchObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory();
    
    /**
     * ObjectFactory for the updates capability messages.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory updatesObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory();
    
    /**
     * ObjectFactory for the dsml profiles data objects related to SPML.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory spmldsmlObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory();
    
    /**
     * ObjectFactory for the dsml profiles data objects.
     */
    static protected final es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory dsmlv2ObjectFactory = 
            new es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory();
    
    static {
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
        } catch(DatatypeConfigurationException e) {
            log.log(Level.SEVERE, "Error initializing the data type factory", e);
        }
    }
    
    /**
     * The real request as defined in SPMLv2.
     */
    protected R request = null;
    
    /**
     * The PSO is used by a lot of requests in SPMLv2. So the idea is that
     * the pso is managed by the base builder to avoid repetition in the
     * builders that extend this one.
     */
    protected PSOIdentifierType pso = null;
    
    /**
     * The return data is also used by a lot of requests. Same idea, put
     * it in the base class to avoid repetition of code.
     */
    protected ReturnDataType returnData = null;
    
    /**
     * Constructor using the request.
     * 
     * @param request The request that this builder constructs.
     */
    protected RequestBuilder(R request) {
        this.request = request;
        this.pso = null;
        this.returnData = null;
    }
    
    //
    // static method to create any standard request builder
    //
    
    /**
     * Returns a new builder to create an Add request.
     * @return A new builder for add
     */
    static public AddRequestBuilder builderForAdd() {
        return new AddRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a ListTargets request.
     * @return A new builder for ListTargets
     */
    static public ListTargetsRequestBuilder builderForListTargets() {
        return new ListTargetsRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Delete request.
     * @return A new builder for delete
     */
    static public DeleteRequestBuilder builderForDelete() {
        return new DeleteRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Modify request.
     * @return A new builder for modify
     */
    static public ModifyRequestBuilder builderForModify() {
        return new ModifyRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Lookup request.
     * @return A new builder for lookup
     */
    static public LookupRequestBuilder builderForLookup() {
        return new LookupRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Status request.
     * @return A new builder for status
     */
    static public StatusRequestBuilder builderForStatus() {
        return new StatusRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Cancel request.
     * @return A new builder for cancel
     */
    static public CancelRequestBuilder builderForCancel() {
        return new CancelRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a SetPassword request.
     * @return A new builder for set password
     */
    static public SetPasswordRequestBuilder builderForSetPassword() {
        return new SetPasswordRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a ExpirePassword request.
     * @return A new builder for expire password
     */
    static public ExpirePasswordRequestBuilder builderForExpirePassword() {
        return new ExpirePasswordRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a ResetPassword request.
     * @return A new builder for reset password
     */
    static public ResetPasswordRequestBuilder builderForResetPassword() {
        return new ResetPasswordRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a ValidatePassword request.
     * @return A new builder for validate password
     */
    static public ValidatePasswordRequestBuilder builderForValidatePassword() {
        return new ValidatePasswordRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Search request.
     * @return A new builder for search
     */
    static public SearchRequestBuilder builderForSearch() {
        return new SearchRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Iterate request.
     * @return A new builder for iterate
     */
    static public IterateRequestBuilder builderForIterate() {
        return new IterateRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a CloseIterator request.
     * @return A new builder for close iterator
     */
    static public CloseIteratorRequestBuilder builderForCloseIterator() {
        return new CloseIteratorRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Suspend request.
     * @return A new builder for suspend
     */
    static public SuspendRequestBuilder builderForSuspend() {
        return new SuspendRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Resume request.
     * @return A new builder for resume
     */
    static public ResumeRequestBuilder builderForResume() {
        return new ResumeRequestBuilder();
    }
    
    /**
     * Returns a new builder to create an Active request.
     * @return A new builder for request
     */
    static public ActiveRequestBuilder builderForActive() {
        return new ActiveRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Query object. This is used by requests
     * like search or updates.
     * @return A new builder for query
     */
    static public SearchQueryBuilder builderForQuery() {
        return new SearchQueryBuilder();
    }
    
    /**
     * Returns a new builder to create a PsoIdentifier. The pso identifier
     * is used by a lot of requests to identify the object to use in the 
     * operation.
     * @return A new builder for pso identifier
     */
    static public PsoIdentifierBuilder builderForPsoIdentifier() {
        return new PsoIdentifierBuilder();
    }
    
    /**
     * Returns a new builder to create a BulkModify request.
     * @return A new builder for bulk modify
     */
    static public BulkModifyRequestBuilder builderForBulkModify() {
        return new BulkModifyRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a BulkDelete request.
     * @return A new builder for bulk delete
     */
    static public BulkDeleteRequestBuilder builderForBulkDelete() {
        return new BulkDeleteRequestBuilder();
    }
    
    /**
     * Returns a new builder to create a Batch request.
     * @return A new builder for batch
     */
    static public BatchRequestBuilder builderForBatch() {
        return new BatchRequestBuilder();
    }
    
    /**
     * Returns a new builder to create an Updates request.
     * @return A new builder for updates
     */
    static public UpdatesRequestBuilder builderForUpdates() {
        return new UpdatesRequestBuilder();
    }
    
    /**
     * Returns a new builder to create an UpdatesIterate request.
     * @return A new builder for updates iterate
     */
    static public UpdatesIterateRequestBuilder builderForUpdatesIterate() {
        return new UpdatesIterateRequestBuilder();
    }
    
    /**
     * Returns a new builder to create an UpdatesCloseIterator request.
     * @return A new builder for updates close iterator
     */
    static public UpdatesCloseIteratorRequestBuilder builderForUpdatesCloseIterator() {
        return new UpdatesCloseIteratorRequestBuilder();
    }
    
    //
    // Methods to obtain the object factories for the standard capabilities
    //
    
    /**
     * The JAXB factory class for the core capability messages.
     * @return The class that is the ObjectFactory for messages in core
     */
    static public Class getCoreObjectFactoryClass() {
        return coreObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the password capability messages.
     * @return The class that is the ObjectFactory for messages in password
     */
    static public Class getPasswordObjectFactoryClass() {
        return passwordObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the suspend capability messages.
     * @return The class that is the ObjectFactory for messages in suspend
     */
    static public Class getSuspendObjectFactoryClass() {
        return suspendObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the batch capability messages.
     * @return The class that is the ObjectFactory for messages in batch
     */
    static public Class getBatchObjectFactoryClass() {
        return batchObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the async capability messages.
     * @return The class that is the ObjectFactory for messages in async
     */
    static public Class getAsyncObjectFactoryClass() {
        return asyncObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the bulk capability messages.
     * @return The class that is the ObjectFactory for messages in bulk
     */
    static public Class getBulkObjectFactoryClass() {
        return bulkObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the reference capability messages.
     * @return The class that is the ObjectFactory for messages in reference
     */
    static public Class getReferenceObjectFactoryClass() {
        return referenceObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the search capability messages.
     * @return The class that is the ObjectFactory for messages in search
     */
    static public Class getSearchObjectFactoryClass() {
        return searchObjectFactory.getClass();
    }

    /**
     * The JAXB factory class for the updates capability messages.
     * @return The class that is the ObjectFactory for messages in updates
     */
    static public Class getUpdatesObjectFactoryClass() {
        return updatesObjectFactory.getClass();
    }
    
    /**
     * The JAXB factory class for the spml dsml profile.
     * @return The class that is the ObjectFactory for messages in spml dsml
     */
    static public Class getSpmldsmlObjectFactoryClass() {
        return spmldsmlObjectFactory.getClass();
    }
    
    /**
     * The JAXB factory class for the DSMLv2 profile.
     * @return The class that is the ObjectFactory for messages in DSMLv2
     */
    static public Class getDsmlv2ObjectFactoryClass() {
        return dsmlv2ObjectFactory.getClass();
    }
    
    /**
     * Returns the list of classes of all the JAXB factories for the 
     * standard SPMLv2 capability messages.
     * @return The array of classes for the factories
     */
    static public Class[] getAllSpmlv2ObjectFactoryClasses() {
        return new Class[]{
            getCoreObjectFactoryClass(),
            getPasswordObjectFactoryClass(),
            getSuspendObjectFactoryClass(),
            getBatchObjectFactoryClass(),
            getAsyncObjectFactoryClass(),
            getBulkObjectFactoryClass(),
            getReferenceObjectFactoryClass(),
            getSearchObjectFactoryClass(),
            getUpdatesObjectFactoryClass(),
            getSpmldsmlObjectFactoryClass(),
            getDsmlv2ObjectFactoryClass()
        };
    }
    
    //
    // Object Builders to use unside the underlaying extensions
    //

    /**
     * Returns the Object Factory to create all the messages in the core capability.
     * @return The object factory for core
     */
    protected es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory getCoreObjectFactory() {
        return coreObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the password capability.
     * @return The object factory for password
     */
    protected es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory getPasswordObjectFactory() {
        return passwordObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the suspend capability.
     * @return The object factory for suspend
     */
    protected es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory getSuspendObjectFactory() {
        return suspendObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the batch capability.
     * @return The object factory for batch
     */
    protected es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory getBatchObjectFactory() {
        return batchObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the async capability.
     * @return The object factory for async
     */
    protected es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory getAsyncObjectFactory() {
        return asyncObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the bulk capability.
     * @return The object factory for bulk
     */
    protected es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory getBulkObjectFactory() {
        return bulkObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the reference capability.
     * @return The object factory for reference
     */
    protected es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory getReferenceObjectFactory() {
        return referenceObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the search capability.
     * @return The object factory for search
     */
    protected es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory getSearchObjectFactory() {
        return searchObjectFactory;
    }

    /**
     * Returns the Object Factory to create all the messages in the updates capability.
     * @return The object factory for updates
     */
    protected es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory getUpdatesObjectFactory() {
        return updatesObjectFactory;
    }
    
    /**
     * Returns the Object Factory to create all the messages in the spml dsml.
     * @return The object factory for spml dsml
     */
    protected es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory getSpmldsmlObjectFactory() {
        return spmldsmlObjectFactory;
    }
    
    /**
     * Returns the Object Factory to create all the messages in the DSMLv2 profile.
     * @return The object factory for DSMLv2
     */
    protected es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory getDsmlv2ObjectFactory() {
        return dsmlv2ObjectFactory;
    }
    
    /**
     * Method that changes the inner object of the builder. Sometimes it is 
     * needed to update the internal request object with a specific one. Some
     * builders that extends this should override this to set pso, return data
     * or any other property managed for easy access.
     * 
     * @param request The new request to create the builder
     * @return The same builder
     */
    public B fromRequest(R request) {
        this.request = request;
        return (B) this;
    }
    
    //
    // common request builder operations
    //
    
    /**
     * Create a request identifier for the request.
     * @param id The new identifier for the requests
     * @return The same builder
     */
    public B requestId(String id) {
        request.setRequestID(id);
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
     * Set the request as synchronous.
     * @return The same builder
     */
    public B synchronous() {
        request.setExecutionMode(ExecutionModeType.SYNCHRONOUS);
        return (B) this;
    }
    
    /**
     * Set the request as asynchronous.
     * @return The same builder
     */
    public B asynchronous() {
        request.setExecutionMode(ExecutionModeType.ASYNCHRONOUS);
        return (B) this;
    }
    
    //
    // PSO methods
    //
    
    /**
     * Assign a pso identifier just using the identifier of the object.
     * @param psoId The object identifier to set into the pso
     * @return The same builder
     */
    public B psoId(String psoId) {
        if (pso == null) {
            pso = new PSOIdentifierType();
        }
        pso.setID(psoId);
        return (B) this;
    }

    /**
     * Assign a target identifier in the pso.
     * @param psoTargetId The target id to set into the pso
     * @return The same builder
     */
    public B psoTargetId(String psoTargetId) {
        if (pso == null) {
            pso = new PSOIdentifierType();
        }
        this.pso.setTargetID(psoTargetId);
        return (B) this;
    }
    
    /**
     * Assign a complex pso identifier using a PSO identifier builder.
     * @param psoId The builder that represents the pso identifier
     * @return The same builder
     */
    public B psoIdentifier(PsoIdentifierBuilder psoId) {
        this.pso = psoId.build();
        return (B) this;
    }
    
    //
    // RETURN DATA
    //
    
    /**
     * Set the request to return everything (in add, lookup, modify or any
     * operation related to an object).
     * @return The same builder
     */
    public B returnEverything() {
        returnData = ReturnDataType.EVERYTHING;
        return (B) this;
    }

    /**
     * Set the request to return only the data of the object (in add, lookup,
     * modify or any operation related to an object).
     * @return The same builder
     */
    public B returnData() {
        returnData = ReturnDataType.DATA;
        return (B) this;
    }

    /**
     * set the request ti return only the PSO identifier (in add, lookup,
     * modify or any operation related to an object).
     * @return The same builder
     */
    public B returnIdentifier() {
        returnData = ReturnDataType.IDENTIFIER;
        return (B) this;
    }
    
    //
    // ANY ATTRIBUTES
    //
    
    /**
     * The SPMLv2 let us extend the standard messages with custom XML data. 
     * This method is used to add new objects in the ANY property. For example
     * Waveset server uses this to send the username and password in the 
     * ListTargets request.
     * @param o The object to add (JAXB)
     * @return The same builder
     */
    public B operationalObject(Object o) {
        this.request.getAny().add(o);
        return (B) this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    abstract public JAXBElement<R> build();
    
    /**
     * Generic method to send the request an obtain the ResponseAccessor. This
     * method return the generic (unknown) accessor that should be transformed
     * later to a known response (asAdd, asDelete and so on). There is a 
     * send method that does this transformation for you.
     * 
     * @param client The requestor used to send the request
     * @return The unknown accessor that should be transformed into the specific one
     * @throws SpmlException Some error in the send
     */
    public ResponseAccessor sendGeneric(SpmlRequestor client) throws SpmlException {
        return client.send(this.build());
    }
    
    /**
     * This method is implemented by all the request builders in order to
     * return the specific accessor for the response. The idea is just
     * calling the internal send method and perform the corresponding
     * asXXX method.
     * 
     * @param client The requestor used to send the request
     * @return The specific response accessor for this request
     * @throws SpmlException Some error in the send 
     */
    abstract public RA send(SpmlRequestor client) throws SpmlException;
    
    /**
     * {@inheritDoc}
     */
    @Override
    abstract public A asAccessor();
    
}
