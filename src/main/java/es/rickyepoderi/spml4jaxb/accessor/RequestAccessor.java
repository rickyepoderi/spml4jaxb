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
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkDeleteRequestType;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.AddRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.DeleteRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ExecutionModeType;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.LookupRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ReturnDataType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.Filter;
import es.rickyepoderi.spml4jaxb.msg.password.ExpirePasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.password.SetPasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchQueryType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveRequestType;
import es.rickyepoderi.spml4jaxb.msg.suspend.ResumeRequestType;
import es.rickyepoderi.spml4jaxb.msg.suspend.SuspendRequestType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType;
import java.lang.reflect.Constructor;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 * @author ricky
 * @param <R>
 * @param <B>
 */
public class RequestAccessor<R extends RequestType, B extends RequestBuilder> implements Accessor<R, B>{
    
    static public final String SPML_CAPABILITY_CORE_URI = "urn:oasis:names:tc:SPML:2:0";
    static public final String SPML_CAPABILITY_ASYNC_URI = "urn:oasis:names:tc:SPML:2:0:async";
    static public final String SPML_CAPABILITY_PASSWORD_URI = "urn:oasis:names:tc:SPML:2:0:password";
    static public final String SPML_CAPABILITY_SUSPEND_URI = "urn:oasis:names:tc:SPML:2:0:suspend";
    static public final String SPML_CAPABILITY_BULK_URI = "urn:oasis:names:tc:SPML:2:0:bulk";
    static public final String SPML_CAPABILITY_BATCH_URI = "urn:oasis:names:tc:SPML:2:0:batch";
    static public final String SPML_CAPABILITY_UPDATES_URI = "urn:oasis:names:tc:SPML:2:0:updates";
    static public final String SPML_CAPABILITY_REFERENCE_URI = "urn:oasis:names:tc:SPML:2:0:reference";
    static public final String SPML_CAPABILITY_SEARCH_URI = "urn:oasis:names:tc:SPML:2:0:search";
    
    static protected final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    
    protected R request = null;
    protected PSOIdentifierType pso = null;
    protected ReturnDataType returnData = null;
    
    protected RequestAccessor(R request, PSOIdentifierType pso, ReturnDataType returnData) {
        this.request = request;
        this.pso = pso;
        this.returnData = returnData;
    }
    
    static public RequestAccessor accessorForRequest(RequestType request) {
        return new RequestAccessor(request, null, null);
    }
    
    static public RequestAccessor accessorForRequest(RequestType request, Class<? extends RequestAccessor> accessor) {
        try {
            Constructor cons = accessor.getDeclaredConstructor(request.getClass());
            cons.setAccessible(true);
            return (RequestAccessor) cons.newInstance(request);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    static public SearchQueryAccessor accessorForSearchQuery(SearchQueryType query) {
        return new SearchQueryAccessor(query);
    }
    
    static public FilterAccessor accessorForFilter(Filter filter) {
        return new FilterAccessor(filter);
    }
    
    public Class getRequestClass() {
        return request.getClass();
    }
    
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
    
    public String getRequestId() {
        return request.getRequestID();
    }
    
    public boolean isRequestId(String requestId) {
        return requestId.equals(getRequestId());
    }
    
    public ExecutionModeType getExecutionMode() {
        return request.getExecutionMode();
    }
    
    public boolean isSynchronous() {
        return ExecutionModeType.SYNCHRONOUS.equals(request.getExecutionMode());
    }
 
    public boolean isAsynchronous() {
        return ExecutionModeType.ASYNCHRONOUS.equals(request.getExecutionMode());
    }
    
    public ReturnDataType getReturnData() {
        return returnData;
    }
    
    public boolean isReturnEverything() {
        return returnData == null || ReturnDataType.EVERYTHING.equals(returnData);
    }
    
    public boolean isReturnData() {
        return ReturnDataType.DATA.equals(returnData);
    }
    
    public boolean isReturnIdentifier() {
        return ReturnDataType.IDENTIFIER.equals(returnData);
    }
    
    public String getPsoId() {
        if (pso != null) {
            return pso.getID();
        } else {
            return null;
        }
    }
    
    public String getPsoTargetId() {
        if (pso != null) {
            return pso.getTargetID();
        } else {
            return null;
        }
    }
    
    public boolean isPsoTargetId(String targetId) {
        if (pso != null) {
            return targetId.equals(pso.getTargetID());
        } else {
            return false;
        }
    }
    
    public PsoIdentifierAccessor getPsoAccessor() {
        if (pso != null) {
            return new PsoIdentifierAccessor(pso);
        } else {
            return null;
        }
    }
    
    public ResponseBuilder responseBuilder() {
        throw new IllegalStateException("You should never use the accessor at this level");
    }
    
    @Override
    public B toBuilder() {
        throw new IllegalStateException("You should never use the accessor at this level");
    }
    
    public ListTargetsRequestAccessor asListTargets() {
        if (request instanceof ListTargetsRequestType) {
            return new ListTargetsRequestAccessor((ListTargetsRequestType) request);
        } else {
            return null;
        }
    }
    
    //
    // AS METHODS
    //
    
    public AddRequestAccessor asAdd() {
        if (request instanceof AddRequestType) {
            return new AddRequestAccessor((AddRequestType) request);
        } else {
            return null;
        }
    }
    
    public LookupRequestAccessor asLookup() {
        if (request instanceof LookupRequestType) {
            return new LookupRequestAccessor((LookupRequestType) request);
        } else {
            return null;
        }
    }
    
    public ModifyRequestAccessor asModify() {
        if (request instanceof ModifyRequestType) {
            return new ModifyRequestAccessor((ModifyRequestType) request);
        } else {
            return null;
        }
    }
    
    public DeleteRequestAccessor asDelete() {
        if (request instanceof DeleteRequestType) {
            return new DeleteRequestAccessor((DeleteRequestType) request);
        } else {
            return null;
        }
    }
    
    public CancelRequestAccessor asCancel() {
        if (request instanceof CancelRequestType) {
            return new CancelRequestAccessor((CancelRequestType) request);
        } else {
            return null;
        }
    }
    
    public StatusRequestAccessor asStatus() {
        if (request instanceof StatusRequestType) {
            return new StatusRequestAccessor((StatusRequestType) request);
        } else {
            return null;
        }
    }
    
    public SetPasswordRequestAccessor asSetPassword() {
        if (request instanceof SetPasswordRequestType) {
            return new SetPasswordRequestAccessor((SetPasswordRequestType) request);
        } else {
            return null;
        }
    }
    
    public ExpirePasswordRequestAccessor asExpirePassword() {
        if (request instanceof ExpirePasswordRequestType) {
            return new ExpirePasswordRequestAccessor((ExpirePasswordRequestType) request);
        } else {
            return null;
        }
    }
    
    public ResetPasswordRequestAccessor asResetPassword() {
        if (request instanceof ResetPasswordRequestType) {
            return new ResetPasswordRequestAccessor((ResetPasswordRequestType) request);
        } else {
            return null;
        }
    }
    
    public ValidatePasswordRequestAccessor asValidatePassword() {
        if (request instanceof ValidatePasswordRequestType) {
            return new ValidatePasswordRequestAccessor((ValidatePasswordRequestType) request);
        } else {
            return null;
        }
    }
    
    public ActiveRequestAccessor asActive() {
        if (request instanceof ActiveRequestType) {
            return new ActiveRequestAccessor((ActiveRequestType) request);
        } else {
            return null;
        }
    }
    
    public ResumeRequestAccessor asResume() {
        if (request instanceof ResumeRequestType) {
            return new ResumeRequestAccessor((ResumeRequestType) request);
        } else {
            return null;
        }
    }
    
    public SuspendRequestAccessor asSuspend() {
        if (request instanceof SuspendRequestType) {
            return new SuspendRequestAccessor((SuspendRequestType) request);
        } else {
            return null;
        }
    }
    
    public SearchRequestAccessor asSearch() {
        if (request instanceof SearchRequestType) {
            return new SearchRequestAccessor((SearchRequestType) request);
        } else {
            return null;
        }
    }
    
    public IterateRequestAccessor asIterate() {
        if (request instanceof IterateRequestType) {
            return new IterateRequestAccessor((IterateRequestType) request);
        } else {
            return null;
        }
    }
    
    public CloseIteratorRequestAccessor asCloseIterator() {
        if (request instanceof CloseIteratorRequestType) {
            return new CloseIteratorRequestAccessor((CloseIteratorRequestType) request);
        } else {
            return null;
        }
    }
    
    public BulkModifyRequestAccessor asBulkModify() {
        if (request instanceof BulkModifyRequestType) {
            return new BulkModifyRequestAccessor((BulkModifyRequestType) request);
        } else {
            return null;
        }
    }
    
    public BulkDeleteRequestAccessor asBulkDelete() {
        if (request instanceof BulkDeleteRequestType) {
            return new BulkDeleteRequestAccessor((BulkDeleteRequestType) request);
        } else {
            return null;
        }
    }
    
    public BatchRequestAccessor asBatch() {
        if (request instanceof BatchRequestType) {
            return new BatchRequestAccessor((BatchRequestType) request);
        } else {
            return null;
        }
    }
    
    public UpdatesRequestAccessor asUpdates() {
        if (request instanceof UpdatesRequestType) {
            return new UpdatesRequestAccessor((UpdatesRequestType) request);
        } else {
            return null;
        }
    }
    
    public UpdatesIterateRequestAccessor asUpdatesIterate() {
        if (request instanceof es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType) {
            return new UpdatesIterateRequestAccessor((es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType) request);
        } else {
            return null;
        }
    }
    
    public UpdatesCloseIteratorRequestAccessor asUpdatesCloseIterator() {
        if (request instanceof es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType) {
            return new UpdatesCloseIteratorRequestAccessor((es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType) request);
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        return sb.append(request.getClass().getSimpleName()).append(" - ").append(getRequestId())
                .append(" - ").append(getReturnData()).append(nl)
                .append("  psoId: ").append(getPsoId()).append(nl)
                .append("  psoTargetId: ").append(getPsoTargetId()).append(nl).toString();
    }

    @Override
    public R getInternalType() {
        return request;
    }
}
