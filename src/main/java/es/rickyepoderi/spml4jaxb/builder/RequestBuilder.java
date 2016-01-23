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
 *
 * @author ricky
 * @param <R> The request type the builder is about
 * @param <B> The request builder we are creating
 * @param <A>
 * @param <RA> 
 */
public abstract class RequestBuilder<R extends RequestType, B extends RequestBuilder, 
        A extends BaseRequestAccessor, RA extends BaseResponseAccessor> implements Builder<JAXBElement<R>, A> {
    
    protected static final Logger log = Logger.getLogger(RequestBuilder.class.getName());
    
    static public final String DSML_PROFILE_URI = "urn:oasis:names:tc:SPML:2:0:DSML";
    static public final String XSD_PROFILE_URI = "urn:oasis:names:tc:SPML:2.0:profiles:XSD";
    
    static protected DatatypeFactory dataTypeFactory;
    static protected final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    
    static protected final es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory coreObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory passwordObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory suspendObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory batchObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory asyncObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory bulkObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory referenceObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory searchObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory updatesObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory spmldsmlObjectFactory =
            new es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory();
    static protected final es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory dsmlv2ObjectFactory = 
            new es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory();
    
    static {
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
        } catch(DatatypeConfigurationException e) {
            log.log(Level.SEVERE, "Error initializing the data type factory", e);
        }
    }
    
    protected R request = null;
    protected PSOIdentifierType pso = null;
    protected ReturnDataType returnData = null;
    
    protected RequestBuilder(R request) {
        this.request = request;
        this.pso = null;
        this.returnData = null;
    }
    
    static public AddRequestBuilder builderForAdd() {
        return new AddRequestBuilder();
    }
    
    static public ListTargetsRequestBuilder builderForListTargets() {
        return new ListTargetsRequestBuilder();
    }
    
    static public DeleteRequestBuilder builderForDelete() {
        return new DeleteRequestBuilder();
    }
    
    static public ModifyRequestBuilder builderForModify() {
        return new ModifyRequestBuilder();
    }
    
    static public LookupRequestBuilder builderForLookup() {
        return new LookupRequestBuilder();
    }
    
    static public StatusRequestBuilder builderForStatus() {
        return new StatusRequestBuilder();
    }
    
    static public CancelRequestBuilder builderForCancel() {
        return new CancelRequestBuilder();
    }
    
    static public SetPasswordRequestBuilder builderForSetPassword() {
        return new SetPasswordRequestBuilder();
    }
    
    static public ExpirePasswordRequestBuilder builderForExpirePassword() {
        return new ExpirePasswordRequestBuilder();
    }
    
    static public ResetPasswordRequestBuilder builderForResetPassword() {
        return new ResetPasswordRequestBuilder();
    }
    
    static public ValidatePasswordRequestBuilder builderForValidatePassword() {
        return new ValidatePasswordRequestBuilder();
    }
    
    static public SearchRequestBuilder builderForSearch() {
        return new SearchRequestBuilder();
    }
    
    static public IterateRequestBuilder builderForIterate() {
        return new IterateRequestBuilder();
    }
    
    static public CloseIteratorRequestBuilder builderForCloseIterator() {
        return new CloseIteratorRequestBuilder();
    }
    
    static public SuspendRequestBuilder builderForSuspend() {
        return new SuspendRequestBuilder();
    }
    
    static public ResumeRequestBuilder builderForResume() {
        return new ResumeRequestBuilder();
    }
    
    static public ActiveRequestBuilder builderForActive() {
        return new ActiveRequestBuilder();
    }
    
    static public SearchQueryBuilder builderForQuery() {
        return new SearchQueryBuilder();
    }
    
    static public PsoIdentifierBuilder builderForPsoIdentifier() {
        return new PsoIdentifierBuilder();
    }
    
    static public BulkModifyRequestBuilder builderForBulkModify() {
        return new BulkModifyRequestBuilder();
    }
    
    static public BulkDeleteRequestBuilder builderForBulkDelete() {
        return new BulkDeleteRequestBuilder();
    }
    
    static public BatchRequestBuilder builderForBatch() {
        return new BatchRequestBuilder();
    }
    
    static public UpdatesRequestBuilder builderForUpdates() {
        return new UpdatesRequestBuilder();
    }
    
    static public UpdatesIterateRequestBuilder builderForUpdatesIterate() {
        return new UpdatesIterateRequestBuilder();
    }
    
    static public UpdatesCloseIteratorRequestBuilder builderForUpdatesCloseIterator() {
        return new UpdatesCloseIteratorRequestBuilder();
    }
    
    static public Class getCoreObjectFactoryClass() {
        return coreObjectFactory.getClass();
    }

    static public Class getPasswordObjectFactoryClass() {
        return passwordObjectFactory.getClass();
    }

    static public Class getSuspendObjectFactoryClass() {
        return suspendObjectFactory.getClass();
    }

    static public Class getBatchObjectFactoryClass() {
        return batchObjectFactory.getClass();
    }

    static public Class getAsyncObjectFactoryClass() {
        return asyncObjectFactory.getClass();
    }

    static public Class getBulkObjectFactoryClass() {
        return bulkObjectFactory.getClass();
    }

    static public Class getReferenceObjectFactoryClass() {
        return referenceObjectFactory.getClass();
    }

    static public Class getSearchObjectFactoryClass() {
        return searchObjectFactory.getClass();
    }

    static public Class getUpdatesObjectFactoryClass() {
        return updatesObjectFactory.getClass();
    }
    
    static public Class getSpmldsmlObjectFactoryClass() {
        return spmldsmlObjectFactory.getClass();
    }
    
    static public Class getDsmlv2ObjectFactoryClass() {
        return dsmlv2ObjectFactory.getClass();
    }
    
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
    // Object Builders
    //

    protected es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory getCoreObjectFactory() {
        return coreObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory getPasswordObjectFactory() {
        return passwordObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory getSuspendObjectFactory() {
        return suspendObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory getBatchObjectFactory() {
        return batchObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory getAsyncObjectFactory() {
        return asyncObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory getBulkObjectFactory() {
        return bulkObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory getReferenceObjectFactory() {
        return referenceObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory getSearchObjectFactory() {
        return searchObjectFactory;
    }

    protected es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory getUpdatesObjectFactory() {
        return updatesObjectFactory;
    }
    
    protected es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory getSpmldsmlObjectFactory() {
        return spmldsmlObjectFactory;
    }
    
    protected es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory getDsmlv2ObjectFactory() {
        return dsmlv2ObjectFactory;
    }
    
    public B fromRequest(R request) {
        this.request = request;
        return (B) this;
    }
    
    //
    // general request builder operations
    //
    
    public B requestId(String id) {
        request.setRequestID(id);
        return (B) this;
    }
    
    public B requestId() {
        return this.requestId(UUID.randomUUID().toString());
    }
    
    public B synchronous() {
        request.setExecutionMode(ExecutionModeType.SYNCHRONOUS);
        return (B) this;
    }
    
    public B asynchronous() {
        request.setExecutionMode(ExecutionModeType.ASYNCHRONOUS);
        return (B) this;
    }
    
    //
    // PSO methods
    //
    
    public B psoId(String psoId) {
        if (pso == null) {
            pso = new PSOIdentifierType();
        }
        pso.setID(psoId);
        return (B) this;
    }

    public B psoTargetId(String psoTargetId) {
        if (pso == null) {
            pso = new PSOIdentifierType();
        }
        this.pso.setTargetID(psoTargetId);
        return (B) this;
    }
    
    public B psoIdentifier(PsoIdentifierBuilder psoId) {
        this.pso = psoId.build();
        return (B) this;
    }
    
    //
    // RETURN DATA
    //
    
    public B returnEverything() {
        returnData = ReturnDataType.EVERYTHING;
        return (B) this;
    }

    public B returnData() {
        returnData = ReturnDataType.DATA;
        return (B) this;
    }

    public B returnIdentifier() {
        returnData = ReturnDataType.IDENTIFIER;
        return (B) this;
    }
    
    //
    // ANY ATTRIBUTES
    //
    
    public B operationalObject(Object o) {
        this.request.getAny().add(o);
        return (B) this;
    }
    
    @Override
    abstract public JAXBElement<R> build();
    
    protected ResponseAccessor sendInternal(SpmlRequestor client) throws SpmlException {
        return client.send(this.build());
    }
    
    abstract public RA send(SpmlRequestor client) throws SpmlException;
    
    @Override
    abstract public A asAccessor();
    
}
