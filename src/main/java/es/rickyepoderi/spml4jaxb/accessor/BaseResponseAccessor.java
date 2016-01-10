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

import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.msg.core.ErrorCode;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.StatusCodeType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 * @param <R>
 * @param <A>
 * @param <B>
 */
public abstract class BaseResponseAccessor<R extends ResponseType, A extends BaseResponseAccessor, B extends ResponseBuilder> implements Accessor<R, A, B> {
    
    protected R response;
    protected PSOType pso;
    
    protected BaseResponseAccessor(R response, PSOType pso) {
        this.response = response;
        this.pso = pso;
    }
    
    static public ResponseAccessor accessorForResponse(ResponseType response) {
        return new ResponseAccessor(response);
    }
    
    static public BaseResponseAccessor accessorForResponse(ResponseType response, Class<? extends BaseResponseAccessor> accessor) {
        try {
            Constructor cons = accessor.getDeclaredConstructor(response.getClass());
            cons.setAccessible(true);
            return (BaseResponseAccessor) cons.newInstance(response);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public abstract B toBuilder();

    @Override
    public abstract A asAccessor(R response);
    
    public Class getResponseClass() {
        return response.getClass();
    }
    
    public StatusCodeType getStatus() {
        return response.getStatus();
    }
    
    public boolean isSuccess() {
        return StatusCodeType.SUCCESS.equals(response.getStatus());
    }
    
    public boolean isPending() {
        return StatusCodeType.PENDING.equals(response.getStatus());
    }
    
    public boolean isFailure() {
        return StatusCodeType.FAILURE.equals(response.getStatus());
    }
    
    public String getRequestId() {
        return response.getRequestID();
    }
    
    public boolean isRequestId(String requestId) {
        return requestId.equals(getRequestId());
    }
    
    public ErrorCode getError() {
        return response.getError();
    }
    
    public boolean isMalformedRequest() {
        return ErrorCode.MALFORMED_REQUEST.equals(response.getError());
    }
    
    public boolean isUnsupportedOperation() {
        return ErrorCode.UNSUPPORTED_OPERATION.equals(response.getError());
    }
    
    public boolean isUnsupportedIdentifierType() {
        return ErrorCode.UNSUPPORTED_IDENTIFIER_TYPE.equals(response.getError());
    }
    
    public boolean isNoSuchIdentifier() {
        return ErrorCode.NO_SUCH_IDENTIFIER.equals(response.getError());
    }
    
    public boolean isCustomError() {
        return ErrorCode.CUSTOM_ERROR.equals(response.getError());
    }
    
    public boolean isUnsupportedExecutionMode() {
        return ErrorCode.UNSUPPORTED_EXECUTION_MODE.equals(response.getError());
    }
    
    public boolean isInvalidContaiment() {
        return ErrorCode.INVALID_CONTAINMENT.equals(response.getError());
    }
    
    public boolean isNoSuchRequest() {
        return ErrorCode.NO_SUCH_REQUEST.equals(response.getError());
    }
    
    public boolean isUnsupportedSelectionType() {
        return ErrorCode.UNSUPPORTED_SELECTION_TYPE.equals(response.getError());
    }
    
    public boolean isResultSetToLarge() {
        return ErrorCode.RESULT_SET_TO_LARGE.equals(response.getError());
    }
    
    public boolean isUnsupportedProfile() {
        return ErrorCode.UNSUPPORTED_PROFILE.equals(response.getError());
    }
    
    public boolean isInvalidIdentifier() {
        return ErrorCode.INVALID_IDENTIFIER.equals(response.getError());
    }
    
    public boolean isAlreadyExists() {
        return ErrorCode.ALREADY_EXISTS.equals(response.getError());
    }
    
    public boolean isContainerNotEmpty() {
        return ErrorCode.CONTAINER_NOT_EMPTY.equals(response.getError());
    }
    
    public String[] getErrorMessages() {
        return response.getErrorMessage().toArray(new String[0]);
    }
    
    public String getErrorMessagesInOne() {
        StringBuilder sb = new StringBuilder();
        for (String error: getErrorMessages()) {
            sb.append(error).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
    
    public PSOType getPso() {
        return pso;
    }
    
    public PsoIdentifierAccessor getPsoAccessor() {
        if (pso != null && pso.getPsoID() != null) {
            return new PsoIdentifierAccessor(pso.getPsoID());
        } else {
            return null;
        }
    }
    
    public String getPsoId() {
        if (pso != null && pso.getPsoID() != null) {
            return pso.getPsoID().getID();
        } else {
            return null;
        }
    }
    
    public String getPsoTargetId() {
        if (pso != null && pso.getPsoID() != null) {
            return pso.getPsoID().getTargetID();
        } else {
            return null;
        }
    }
    
    public PSOIdentifierType getPsoIdentifier() {
        if (pso != null) {
            return pso.getPsoID();
        } else {
            return null;
        }
    }
    
    public DsmlAttr[] getDsmlAttributes() {
        List<DsmlAttr> res = new ArrayList<>();
        if (pso != null && pso.getData() != null) {
            List<Object> l = pso.getData().getAny();
            for (Object o: l) {
                if (o instanceof DsmlAttr) {
                    res.add((DsmlAttr) o);
                } else if (o instanceof JAXBElement) {
                    JAXBElement el = (JAXBElement) o;
                    if (el.getValue() instanceof DsmlAttr) {
                        res.add((DsmlAttr) el.getValue());
                    }
                }
            }
        }
        return res.toArray(new DsmlAttr[0]);
    }
    
    public Map<String,DsmlAttr> getDsmlAttributesMap() {
        DsmlAttr[] attrs = getDsmlAttributes();
        Map<String,DsmlAttr> res = new HashMap<>(attrs.length);
        for (DsmlAttr attr: attrs) {
            res.put(attr.getName(), attr);
        }
        return res;
    }
    
    public Object getXsdObject(Class clazz) {
        if (pso != null && pso.getData() != null) {
            List<Object> l = pso.getData().getAny();
            for (Object o: l) {
                if (clazz.isInstance(o)) {
                    return o;
                } else if (o instanceof JAXBElement) {
                    JAXBElement el = (JAXBElement) o;
                    if (clazz.isInstance(el.getValue())) {
                        return el.getValue();
                    }
                }
            }
        }
        return null;
    }
    
    //
    // AS METHODS
    //
    
    public <T extends BaseResponseAccessor> T asAccessor(T accessor) {
        if (response.getClass().equals(accessor.response.getClass())) {
            return (T) accessor.asAccessor(response);
        } else {
            return null;
        }
    }
    
    public ResponseAccessor asUnknown() {
        return accessorForResponse(response);
    }
    
    public ListTargetsResponseAccessor asListTargets() {
        return this.asAccessor(new ListTargetsResponseAccessor());
    }
    
    public AddResponseAccessor asAdd() {
        return this.asAccessor(new AddResponseAccessor());
    }
    
    public DeleteResponseAccessor asDelete() {
        return this.asAccessor(new DeleteResponseAccessor());
    }
    
    public ExpirePasswordResponseAccessor asExpirePassword() {
        return this.asAccessor(new ExpirePasswordResponseAccessor());
    }
    
    public SetPasswordResponseAccessor asSetPassword() {
        return this.asAccessor(new SetPasswordResponseAccessor());
    }
    
    public SuspendResponseAccessor asSuspend() {
        return this.asAccessor(new SuspendResponseAccessor());
    }
    
    public ResumeResponseAccessor asResume() {
        return this.asAccessor(new ResumeResponseAccessor());
    }
    
    public BulkDeleteResponseAccessor asBulkDelete() {
        return this.asAccessor(new BulkDeleteResponseAccessor());
    }
    
    public BulkModifyResponseAccessor asBulkModify() {
        return this.asAccessor(new BulkModifyResponseAccessor());
    }
    
    public UpdatesCloseIteratorResponseAccessor asUpdatesCloseIterator() {
        return this.asAccessor(new UpdatesCloseIteratorResponseAccessor());
    }
    
    public CloseIteratorResponseAccessor asCloseIterator() {
        return this.asAccessor(new CloseIteratorResponseAccessor());
    }
    
    public LookupResponseAccessor asLookup() {
        return this.asAccessor(new LookupResponseAccessor());
    }
    
    public ModifyResponseAccessor asModify() {
        return this.asAccessor(new ModifyResponseAccessor());
    }
    
    public CancelResponseAccessor asCancel() {
        return this.asAccessor(new CancelResponseAccessor());
    }
    
    public StatusResponseAccessor asStatus() {
        return this.asAccessor(new StatusResponseAccessor());
    }
    
    public ResetPasswordResponseAccessor asResetPassword() {
        return this.asAccessor(new ResetPasswordResponseAccessor());
    }
    
    public ValidatePasswordResponseAccessor asValidatePassword() {
        return this.asAccessor(new ValidatePasswordResponseAccessor());
    }
    
    public ActiveResponseAccessor asActive() {
        return this.asAccessor(new ActiveResponseAccessor());
    }
    
    public SearchResponseAccessor asSearch() {
        return this.asAccessor(new SearchResponseAccessor());
    }
    
    public BatchResponseAccessor asBatch() {
        return this.asAccessor(new BatchResponseAccessor());
    }
    
    public UpdatesResponseAccessor asUpdates() {
        return this.asAccessor(new UpdatesResponseAccessor());
    }
    
    public <T> T[] getOperationalObjects(Class<T> clazz) {
        List<T> res = new ArrayList<>();
        for (Object o : response.getAny()) {
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
    
    public String toString(Class clazz) {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append(response.getClass().getSimpleName()).append(" - ").append(getRequestId()).append(nl)
                .append("  status: ").append(getStatus()).append(nl)
                .append("  Error: ").append(getError()).append(nl);
        for (String error: getErrorMessages()) {
            sb.append("    ").append(error).append(nl);
        }
        if (pso != null) {
            if (pso.getPsoID() != null) {
                sb.append("  PSO: ").append(pso.getPsoID().getID())
                        .append(" - ").append(pso.getPsoID().getTargetID()).append(nl);
            }
            DsmlAttr[] attrs = getDsmlAttributes();
            if (attrs != null && attrs.length > 0) {
                sb.append("  Data: ").append(nl);
                for (DsmlAttr attr: getDsmlAttributes()) {
                    sb.append("    ").append(attr.getName()).append(": ")
                            .append(attr.getValue()).append(nl);
                }                
            }
        }
        if (clazz != null) {
            Object o = getXsdObject(clazz);
            if (o != null) {
                sb.append("  Data: ").append(nl);
                sb.append(o);
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toString(null);
    }

    @Override
    public R getInternalType() {
        return response;
    }
    
}
