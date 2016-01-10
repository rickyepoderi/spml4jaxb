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
 *
 * @author ricky
 * @param <R>
 * @param <A>
 * @param <B>
 */
public abstract class BaseRequestAccessor<R extends RequestType, A extends BaseRequestAccessor, B extends RequestBuilder> implements Accessor<R, A, B>{
    
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
    
    protected BaseRequestAccessor(R request, PSOIdentifierType pso, ReturnDataType returnData) {
        this.request = request;
        this.pso = pso;
        this.returnData = returnData;
    }
    
    static public RequestAccessor accessorForRequest(RequestType request) {
        return new RequestAccessor(request);
    }
    
    static public BaseRequestAccessor accessorForRequest(RequestType request, Class<? extends BaseRequestAccessor> accessor) {
        try {
            Constructor cons = accessor.getDeclaredConstructor(request.getClass());
            cons.setAccessible(true);
            return (BaseRequestAccessor) cons.newInstance(request);
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
    
    public abstract ResponseBuilder responseBuilder();
    
    @Override
    public abstract B toBuilder();

    @Override
    public abstract A asAccessor(R request);
    
    //
    // AS METHODS
    //
    
    public <T extends BaseRequestAccessor> T asAccessor(T accessor) {
        if (request.getClass().equals(accessor.request.getClass())) {
            return (T) accessor.asAccessor(request);
        } else {
            return null;
        }
    }
    
    public ListTargetsRequestAccessor asListTargets() {
        return this.asAccessor(new ListTargetsRequestAccessor());
    }
    
    public RequestAccessor asUnknown() {
        return accessorForRequest(request);
    }
    
    public AddRequestAccessor asAdd() {
        return this.asAccessor(new AddRequestAccessor());
    }
    
    public LookupRequestAccessor asLookup() {
        return this.asAccessor(new LookupRequestAccessor());
    }
    
    public ModifyRequestAccessor asModify() {
        return this.asAccessor(new ModifyRequestAccessor());
    }
    
    public DeleteRequestAccessor asDelete() {
        return this.asAccessor(new DeleteRequestAccessor());
    }
    
    public CancelRequestAccessor asCancel() {
        return this.asAccessor(new CancelRequestAccessor());
    }
    
    public StatusRequestAccessor asStatus() {
        return this.asAccessor(new StatusRequestAccessor());
    }
    
    public SetPasswordRequestAccessor asSetPassword() {
        return this.asAccessor(new SetPasswordRequestAccessor());
    }
    
    public ExpirePasswordRequestAccessor asExpirePassword() {
        return this.asAccessor(new ExpirePasswordRequestAccessor());
    }
    
    public ResetPasswordRequestAccessor asResetPassword() {
        return this.asAccessor(new ResetPasswordRequestAccessor());
    }
    
    public ValidatePasswordRequestAccessor asValidatePassword() {
        return this.asAccessor(new ValidatePasswordRequestAccessor());
    }
    
    public ActiveRequestAccessor asActive() {
        return this.asAccessor(new ActiveRequestAccessor());
    }
    
    public ResumeRequestAccessor asResume() {
        return this.asAccessor(new ResumeRequestAccessor());
    }
    
    public SuspendRequestAccessor asSuspend() {
        return this.asAccessor(new SuspendRequestAccessor());
    }
    
    public SearchRequestAccessor asSearch() {
        return this.asAccessor(new SearchRequestAccessor());
    }
    
    public IterateRequestAccessor asIterate() {
        return this.asAccessor(new IterateRequestAccessor());
    }
    
    public CloseIteratorRequestAccessor asCloseIterator() {
        return this.asAccessor(new CloseIteratorRequestAccessor());
    }
    
    public BulkModifyRequestAccessor asBulkModify() {
        return this.asAccessor(new BulkModifyRequestAccessor());
    }
    
    public BulkDeleteRequestAccessor asBulkDelete() {
        return this.asAccessor(new BulkDeleteRequestAccessor());
    }
    
    public BatchRequestAccessor asBatch() {
        return this.asAccessor(new BatchRequestAccessor());
    }
    
    public UpdatesRequestAccessor asUpdates() {
        return this.asAccessor(new UpdatesRequestAccessor());
    }
    
    public UpdatesIterateRequestAccessor asUpdatesIterate() {
        return this.asAccessor(new UpdatesIterateRequestAccessor());
    }
    
    public UpdatesCloseIteratorRequestAccessor asUpdatesCloseIterator() {
        return this.asAccessor(new UpdatesCloseIteratorRequestAccessor());
    }
    
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
