/* 
 * Copyright (c) 2015 rickyepoderi <rickyepoderi@yahoo.es>
 * 
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  See the file COPYING included with this distribution for more
 *  information.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.msg.async.CancelResponseType;
import es.rickyepoderi.spml4jaxb.msg.async.StatusResponseType;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.AddResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.ErrorCode;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.LookupResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOIdentifierType;
import es.rickyepoderi.spml4jaxb.msg.core.PSOType;
import es.rickyepoderi.spml4jaxb.msg.core.ResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.StatusCodeType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlAttr;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordResponseType;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordResponseType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchResponseType;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveResponseType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesResponseType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author ricky
 * @param <R>
 */
public class ResponseAccessor<R extends ResponseType> {
    
    protected R response;
    protected PSOType pso;
    
    protected ResponseAccessor(R response, PSOType pso) {
        this.response = response;
        this.pso = pso;
    }
    
    static public ResponseAccessor accessorForResponse(ResponseType response) {
        return new ResponseAccessor(response, null);
    }
    
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
    
    public PSOType getPso() {
        return pso;
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
    
    public ListTargetsResponseAccessor asListTargets() {
        if (response instanceof ListTargetsResponseType) {
            return new ListTargetsResponseAccessor((ListTargetsResponseType) response);
        } else {
            return null;
        }
    }
    
    public AddResponseAccessor asAdd() {
        if (response instanceof AddResponseType) {
            return new AddResponseAccessor((AddResponseType) response);
        } else {
            return null;
        }
    }
    
    public LookupResponseAccessor asLookup() {
        if (response instanceof LookupResponseType) {
            return new LookupResponseAccessor((LookupResponseType) response);
        } else {
            return null;
        }
    }
    
    public ModifyResponseAccessor asModify() {
        if (response instanceof ModifyResponseType) {
            return new ModifyResponseAccessor((ModifyResponseType) response);
        } else {
            return null;
        }
    }
    
    public CancelResponseAccessor asCancel() {
        if (response instanceof CancelResponseType) {
            return new CancelResponseAccessor((CancelResponseType) response);
        } else {
            return null;
        }
    }
    
    public StatusResponseAccessor asStatus() {
        if (response instanceof StatusResponseType) {
            return new StatusResponseAccessor((StatusResponseType) response);
        } else {
            return null;
        }
    }
    
    public ResetPasswordResponseAccessor asResetPassword() {
        if (response instanceof ResetPasswordResponseType) {
            return new ResetPasswordResponseAccessor((ResetPasswordResponseType) response);
        } else {
            return null;
        }
    }
    
    public ValidatePasswordResponseAccessor asValidatePassword() {
        if (response instanceof ValidatePasswordResponseType) {
            return new ValidatePasswordResponseAccessor((ValidatePasswordResponseType) response);
        } else {
            return null;
        }
    }
    
    public ActiveResponseAccessor asActive() {
        if (response instanceof ActiveResponseType) {
            return new ActiveResponseAccessor((ActiveResponseType) response);
        } else {
            return null;
        }
    }
    
    public SearchResponseAccessor asSearch() {
        if (response instanceof SearchResponseType) {
            return new SearchResponseAccessor((SearchResponseType) response);
        } else {
            return null;
        }
    }
    
    public BatchResponseAccessor asBatch() {
        if (response instanceof BatchResponseType) {
            return new BatchResponseAccessor((BatchResponseType) response);
        } else {
            return null;
        }
    }
    
    public UpdatesResponseAccessor asUpdates() {
        if (response instanceof UpdatesResponseType) {
            return new UpdatesResponseAccessor((UpdatesResponseType) response);
        } else {
            return null;
        }
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
    
}
