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
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ExecutionModeType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ReturnDataType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.Filter;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * <p>The BaseRequestAccesor is the base class for a SPMLv2 request accessor (the
 * accessor that should be used for reading the requests). It is based on
 * generics in order to get a better getter methods.</p>
 * 
 * <p>This class should be extended to create any SPMLv2 response (covered
 * by the standard or custom extensions).</p>
 * 
 * @author ricky
 * @param <R> The real SPMLv2 request object (JAXB obtained)
 * @param <A> The accessor itself to be used in some methods
 * @param <B> The builder to see the request in the builder side
 */
public abstract class BaseRequestAccessor<R extends RequestType, A extends BaseRequestAccessor, 
        B extends RequestBuilder> implements Accessor<R, A, B>{
    
    /**
     * The URI for the core capability.
     */
    static public final String SPML_CAPABILITY_CORE_URI = "urn:oasis:names:tc:SPML:2:0";
    
    /**
     * The URI for the async capability.
     */
    static public final String SPML_CAPABILITY_ASYNC_URI = "urn:oasis:names:tc:SPML:2:0:async";
    
    /**
     * The URI for the password capability.
     */
    static public final String SPML_CAPABILITY_PASSWORD_URI = "urn:oasis:names:tc:SPML:2:0:password";
    
    /**
     * The URI for the suspend capability.
     */
    static public final String SPML_CAPABILITY_SUSPEND_URI = "urn:oasis:names:tc:SPML:2:0:suspend";
    
    /**
     * The URI for the bilk capability.
     */
    static public final String SPML_CAPABILITY_BULK_URI = "urn:oasis:names:tc:SPML:2:0:bulk";
    
    /**
     * The URI for the batch capability.
     */
    static public final String SPML_CAPABILITY_BATCH_URI = "urn:oasis:names:tc:SPML:2:0:batch";
    
    /**
     * The URI for the updates capability.
     */
    static public final String SPML_CAPABILITY_UPDATES_URI = "urn:oasis:names:tc:SPML:2:0:updates";
    
    /**
     * The URI for the reference capability.
     */
    static public final String SPML_CAPABILITY_REFERENCE_URI = "urn:oasis:names:tc:SPML:2:0:reference";
    
    /**
     * The URI for the search capability.
     */
    static public final String SPML_CAPABILITY_SEARCH_URI = "urn:oasis:names:tc:SPML:2:0:search";
    
    /**
     * The document builder factory for DOM access.
     */
    static protected final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    
    /**
     * The inner request object.
     */
    protected R request = null;
    
    /**
     * The PSO identifier type. The pso is used in a lot of requests and it
     * is placed here to reuse it.
     */
    protected PSOIdentifierType pso = null;
    
    /**
     * The return data. The return data is used also in several requests and
     * it is placed here to reuse it.
     */
    protected ReturnDataType returnData = null;
    
    /**
     * Constructor using the three elements.
     * 
     * @param request The request itself.
     * @param pso The pso identifier
     * @param returnData The return data
     */
    protected BaseRequestAccessor(R request, PSOIdentifierType pso, ReturnDataType returnData) {
        this.request = request;
        this.pso = pso;
        this.returnData = returnData;
    }
    
    /**
     * Creates an unknown request accessor for any request type. The
     * accessor should then converted to the specific one using asXXX methods.
     * 
     * @param request The request to get the accessor for
     * @return An unknown request accessor over the request
     */
    static public RequestAccessor accessorForRequest(RequestType request) {
        return new RequestAccessor(request);
    }
    
    /**
     * Similar to the previous creator this one creates a specific accessor
     * using a request and the request accessor. The class of the accessor
     * is passed as the second argument.
     * 
     * @param request The request to get the accessor for
     * @param accessor A specific accessor of the class specified
     * @return A specific accessor over the request type
     */
    static public BaseRequestAccessor accessorForRequest(RequestType request, Class<? extends BaseRequestAccessor> accessor) {
        try {
            Constructor cons = accessor.getDeclaredConstructor(request.getClass());
            cons.setAccessible(true);
            return (BaseRequestAccessor) cons.newInstance(request);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Method that return an accessor over a query type. 
     * 
     * @param query The query type to create the accessor for
     * @return The accessor for the query type
     */
    static public SearchQueryAccessor accessorForSearchQuery(SearchQueryType query) {
        return new SearchQueryAccessor(query);
    }
    
    /**
     * Method that creates an accessor for a filter.
     * 
     * @param filter The filter type to create the accessor for
     * @return The accessor for the filter
     */
    static public FilterAccessor accessorForFilter(Filter filter) {
        return new FilterAccessor(filter);
    }
    
    /**
     * Gets the request class.
     * @return The class for the request
     */
    public Class getRequestClass() {
        return request.getClass();
    }
    
    /**
     * Returns the capability of the inner request.
     * @return The capability associated to the inner request
     */
    public String getCapability() {
        Class clazz = getRequestClass();
        if (es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_CORE_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_ASYNC_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.suspend.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_SUSPEND_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.password.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_PASSWORD_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.bulk.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_BULK_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.batch.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_BATCH_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.updates.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_UPDATES_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.reference.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_REFERENCE_URI;
        } else if (es.rickyepoderi.spml4jaxb.msg.search.ObjectFactory.class.getPackage().equals(clazz.getPackage())) {
            return SPML_CAPABILITY_SEARCH_URI;
        } else {
            // it is a custom capability, if the request accessor is extended to
            // supply a custom request override this method to return your custom string
            return null;
        }
    }
    
    /**
     * Gets the request id.
     * @return The request id
     */
    public String getRequestId() {
        return request.getRequestID();
    }
    
    /**
     * Checks if the request id id the one specified.
     * @param requestId The request id to check
     * @return true if the request id is the one passed
     */
    public boolean isRequestId(String requestId) {
        return requestId.equals(getRequestId());
    }
    
    /**
     * Gets the execution mode of the request.
     * @return The execution mode
     */
    public ExecutionModeType getExecutionMode() {
        return request.getExecutionMode();
    }
    
    /**
     * Checks if the execution mode is synchronous.
     * @return true if the request is synchronous
     */
    public boolean isSynchronous() {
        return ExecutionModeType.SYNCHRONOUS.equals(request.getExecutionMode());
    }
 
    /**
     * Checks if the execution mode is asynchronous.
     * @return true if the request is asynchronous
     */
    public boolean isAsynchronous() {
        return ExecutionModeType.ASYNCHRONOUS.equals(request.getExecutionMode());
    }
    
    /**
     * Gets the return data of the requests.
     * @return the return data of the requests
     */
    public ReturnDataType getReturnData() {
        return returnData;
    }
    
    /**
     * Checks if the return data is everything
     * @return true if the return data is everything
     */
    public boolean isReturnEverything() {
        return returnData == null || ReturnDataType.EVERYTHING.equals(returnData);
    }
    
    /**
     * Checks if the return data is data
     * @return true if the return data is data
     */
    public boolean isReturnData() {
        return ReturnDataType.DATA.equals(returnData);
    }
    
    /**
     * Checks if the return data is identifier
     * @return true if the return data is identifier
     */
    public boolean isReturnIdentifier() {
        return ReturnDataType.IDENTIFIER.equals(returnData);
    }
    
    /**
     * Gets the pso if of the identifier
     * @return The pso id or null
     */
    public String getPsoId() {
        if (pso != null) {
            return pso.getID();
        } else {
            return null;
        }
    }
    
    /**
     * gets the target id ofthe identifier
     * @return The target id or null
     */
    public String getPsoTargetId() {
        if (pso != null) {
            return pso.getTargetID();
        } else {
            return null;
        }
    }
    
    /**
     * Checks if the target id is the one specified.
     * @param targetId The target id to check
     * @return true if they are the same, false otherwise
     */
    public boolean isPsoTargetId(String targetId) {
        if (pso != null) {
            return targetId.equals(pso.getTargetID());
        } else {
            return false;
        }
    }
    
    /**
     * Gets the identifier as an accessor.
     * @return The pso identifier accessor
     */
    public PsoIdentifierAccessor getPsoAccessor() {
        if (pso != null) {
            return new PsoIdentifierAccessor(pso);
        } else {
            return null;
        }
    }
    
    /**
     * Gets the response builder that corresponds to this request.
     * @return The response builder that corresponds to this request
     */
    public abstract ResponseBuilder responseBuilder();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract B toBuilder();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract A asAccessor(R request);
    
    //
    // AS METHODS
    //
    
    /**
     * The generic method to return an accessor in the form of the
     * passed accessor.
     * 
     * @param <T> The specific request accessor class
     * @param accessor The accessor to create
     * @return The accessor of the specific type
     */
    public <T extends BaseRequestAccessor> T asAccessor(T accessor) {
        if (request.getClass().equals(accessor.request.getClass())) {
            return (T) accessor.asAccessor(request);
        } else {
            return null;
        }
    }
    
    /**
     * Converts the accessor into an unknown accessor.
     * @return The unknown accessor
     */
    public RequestAccessor asUnknown() {
        return accessorForRequest(request);
    }
    
    /**
     * Converts the current accessor into a ListTargets.
     * @return The specific accessor
     */
    public ListTargetsRequestAccessor asListTargets() {
        return this.asAccessor(new ListTargetsRequestAccessor());
    }
    
    /**
     * Converts the current accessor into an Add.
     * @return The specific accessor
     */
    public AddRequestAccessor asAdd() {
        return this.asAccessor(new AddRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Lookup.
     * @return The specific accessor
     */
    public LookupRequestAccessor asLookup() {
        return this.asAccessor(new LookupRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Modify.
     * @return The specific accessor
     */
    public ModifyRequestAccessor asModify() {
        return this.asAccessor(new ModifyRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Delete.
     * @return The specific accessor
     */
    public DeleteRequestAccessor asDelete() {
        return this.asAccessor(new DeleteRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Cancel.
     * @return The specific accessor
     */
    public CancelRequestAccessor asCancel() {
        return this.asAccessor(new CancelRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Status.
     * @return The specific accessor
     */
    public StatusRequestAccessor asStatus() {
        return this.asAccessor(new StatusRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a SetPassword.
     * @return The specific accessor
     */
    public SetPasswordRequestAccessor asSetPassword() {
        return this.asAccessor(new SetPasswordRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a ExpirePassword.
     * @return The specific accessor
     */
    public ExpirePasswordRequestAccessor asExpirePassword() {
        return this.asAccessor(new ExpirePasswordRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a ResetPassword.
     * @return The specific accessor
     */
    public ResetPasswordRequestAccessor asResetPassword() {
        return this.asAccessor(new ResetPasswordRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a ValidatePassword.
     * @return The specific accessor
     */
    public ValidatePasswordRequestAccessor asValidatePassword() {
        return this.asAccessor(new ValidatePasswordRequestAccessor());
    }
    
    /**
     * Converts the current accessor into an Active.
     * @return The specific accessor
     */
    public ActiveRequestAccessor asActive() {
        return this.asAccessor(new ActiveRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Resume.
     * @return The specific accessor
     */
    public ResumeRequestAccessor asResume() {
        return this.asAccessor(new ResumeRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Suspend.
     * @return The specific accessor
     */
    public SuspendRequestAccessor asSuspend() {
        return this.asAccessor(new SuspendRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Search.
     * @return The specific accessor
     */
    public SearchRequestAccessor asSearch() {
        return this.asAccessor(new SearchRequestAccessor());
    }
    
    /**
     * Converts the current accessor into an Iterate.
     * @return The specific accessor
     */
    public IterateRequestAccessor asIterate() {
        return this.asAccessor(new IterateRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a CloseIterator.
     * @return The specific accessor
     */
    public CloseIteratorRequestAccessor asCloseIterator() {
        return this.asAccessor(new CloseIteratorRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a BulkModify.
     * @return The specific accessor
     */
    public BulkModifyRequestAccessor asBulkModify() {
        return this.asAccessor(new BulkModifyRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a BulkDelete.
     * @return The specific accessor
     */
    public BulkDeleteRequestAccessor asBulkDelete() {
        return this.asAccessor(new BulkDeleteRequestAccessor());
    }
    
    /**
     * Converts the current accessor into a Batch.
     * @return The specific accessor
     */
    public BatchRequestAccessor asBatch() {
        return this.asAccessor(new BatchRequestAccessor());
    }
    
    /**
     * Converts the current accessor into an Updates.
     * @return The specific accessor
     */
    public UpdatesRequestAccessor asUpdates() {
        return this.asAccessor(new UpdatesRequestAccessor());
    }
    
    /**
     * Converts the current accessor into an UpdatesIterate.
     * @return The specific accessor
     */
    public UpdatesIterateRequestAccessor asUpdatesIterate() {
        return this.asAccessor(new UpdatesIterateRequestAccessor());
    }
    
    /**
     * Converts the current accessor into an UpdatesCloseIterator.
     * @return The specific accessor
     */
    public UpdatesCloseIteratorRequestAccessor asUpdatesCloseIterator() {
        return this.asAccessor(new UpdatesCloseIteratorRequestAccessor());
    }
    
    /**
     * Method to get the objects associated to the Any data in the
     * SPMLv2. The standard let to use another data inside any requests.
     * 
     * @param <T> The type of the data to obtain
     * @param clazz The class to get the associated objects
     * @return The array of objects found with that class
     */
    public <T> T[] getOperationalObjects(Class<T> clazz) {
        List<T> res = new ArrayList<>();
        for (Object o : request.getAny()) {
            if (clazz.isInstance(o)) {
                res.add(clazz.cast(o));
            } else if (o instanceof JAXBElement) {
                JAXBElement el = (JAXBElement) o;
                if (clazz.isInstance(el.getValue())) {
                    res.add(clazz.cast(el.getValue()));
                }
            }
        }
        return res.toArray((T[]) Array.newInstance(clazz, 0));
    }
    
    /**
     * String representation of the request.
     * @return The string representation of the request
     */
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        return sb.append(request.getClass().getSimpleName()).append(" - ").append(getRequestId())
                .append(" - ").append(getReturnData()).append(nl)
                .append("  psoId: ").append(getPsoId()).append(nl)
                .append("  psoTargetId: ").append(getPsoTargetId()).append(nl).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R getInternalType() {
        return request;
    }
}
