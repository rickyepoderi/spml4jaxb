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
 * <p>The BaseResponseAccesor is the base class for a SPMLv2 response accessor (the
 * accessor that should be used for reading responses). It is based on
 * generics in order to get a better getter methods.</p>
 * 
 * <p>This class should be extended to create any SPMLv2 response (covered
 * by the standard or custom extensions).</p>
 * 
 * @author ricky
 * @param <R> The real SPMLv2 response object (JAXB obtained)
 * @param <A> The accessor itself to be used in some methods
 * @param <B> The builder to see the response in the builder side
 */
public abstract class BaseResponseAccessor<R extends ResponseType, A extends BaseResponseAccessor, 
        B extends ResponseBuilder> implements Accessor<R, A, B> {
    
    /**
     * The real response object in SPMLv2.
     */
    protected R response;
    
    /**
     * The PSO type. This is an extension because a lot of responses has 
     * the PSO type (no repetition of code).
     */
    protected PSOType pso;
    
    /**
     * Constructor that gets the response and the pso type.
     * 
     * @param response The response type to access
     * @param pso The pso type if the response manages this data
     */
    protected BaseResponseAccessor(R response, PSOType pso) {
        this.response = response;
        this.pso = pso;
    }
    
    /**
     * Creates an unknown response accessor for any response type. The
     * accessor should then converted to the specific one using asXXX methods.
     * 
     * @param response The response to get the accessor for
     * @return An unknown response accessor over the response
     */
    static public ResponseAccessor accessorForResponse(ResponseType response) {
        return new ResponseAccessor(response);
    }
    
    /**
     * Similar to the previous creator this one creates a specific accessor
     * using a response and the response accessor. The class of the accessor
     * is passed as the second argument.
     * 
     * @param response The response to get the accessor for
     * @param accessor A specific accessor of the class specified
     * @return A specific accessor over the request type
     */
    static public BaseResponseAccessor accessorForResponse(ResponseType response, Class<? extends BaseResponseAccessor> accessor) {
        try {
            Constructor cons = accessor.getDeclaredConstructor(response.getClass());
            cons.setAccessible(true);
            return (BaseResponseAccessor) cons.newInstance(response);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract B toBuilder();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract A asAccessor(R response);
    
    /**
     * Return the class of the response type. It is to know what type is
     * the response class.
     * 
     * @return The class of the response inside the accessor 
     */
    public Class getResponseClass() {
        return response.getClass();
    }
    
    /**
     * Returns the status of the response.
     * @return the status of the response
     */
    public StatusCodeType getStatus() {
        return response.getStatus();
    }
    
    /**
     * Returns if the status of the response is a success.
     * @return true if status is success, false otherwise
     */
    public boolean isSuccess() {
        return StatusCodeType.SUCCESS.equals(response.getStatus());
    }
    
    /**
     * Returns if the status of the response is a pending.
     * @return true if pending is success, false otherwise
     */
    public boolean isPending() {
        return StatusCodeType.PENDING.equals(response.getStatus());
    }
    
    /**
     * Returns if the status of the response is a failure.
     * @return true if failure is success, false otherwise
     */
    public boolean isFailure() {
        return StatusCodeType.FAILURE.equals(response.getStatus());
    }
    
    /**
     * Returns the request id of the response.
     * @return the id of the request
     */
    public String getRequestId() {
        return response.getRequestID();
    }
    
    /**
     * Checks if the request id is the passed as argument.
     * @param requestId The request id to check
     * @return true if the request in the response is equals to the passed one, false otherwise
     */
    public boolean isRequestId(String requestId) {
        return requestId.equals(getRequestId());
    }
    
    /**
     * gets the error code of the response.
     * @return The error code
     */
    public ErrorCode getError() {
        return response.getError();
    }
    
    /**
     * Checks if the request is a malformed request error.
     * @return true if the request is this type of error
     */
    public boolean isMalformedRequest() {
        return ErrorCode.MALFORMED_REQUEST.equals(response.getError());
    }
    
    /**
     * Checks if the request is a unsupported operation error.
     * @return true if the request is this type of error
     */
    public boolean isUnsupportedOperation() {
        return ErrorCode.UNSUPPORTED_OPERATION.equals(response.getError());
    }
    
    /**
     * Checks if the request is a unsupported identifier error.
     * @return true if the request is this type of error
     */
    public boolean isUnsupportedIdentifierType() {
        return ErrorCode.UNSUPPORTED_IDENTIFIER_TYPE.equals(response.getError());
    }
    
    /**
     * Checks if the request is a no such identifier error.
     * @return true if the request is this type of error
     */
    public boolean isNoSuchIdentifier() {
        return ErrorCode.NO_SUCH_IDENTIFIER.equals(response.getError());
    }
    
    /**
     * Checks if the request is a custom error.
     * @return true if the request is this type of error
     */
    public boolean isCustomError() {
        return ErrorCode.CUSTOM_ERROR.equals(response.getError());
    }
    
    /**
     * Checks if the request is a unsupported execution mode error.
     * @return true if the request is this type of error
     */
    public boolean isUnsupportedExecutionMode() {
        return ErrorCode.UNSUPPORTED_EXECUTION_MODE.equals(response.getError());
    }
    
    /**
     * Checks if the request is an invalid containment error.
     * @return true if the request is this type of error
     */
    public boolean isInvalidContaiment() {
        return ErrorCode.INVALID_CONTAINMENT.equals(response.getError());
    }
    
    /**
     * Checks if the request is a no such request error.
     * @return true if the request is this type of error
     */
    public boolean isNoSuchRequest() {
        return ErrorCode.NO_SUCH_REQUEST.equals(response.getError());
    }
    
    /**
     * Checks if the request is a unsupported selection error.
     * @return true if the request is this type of error
     */
    public boolean isUnsupportedSelectionType() {
        return ErrorCode.UNSUPPORTED_SELECTION_TYPE.equals(response.getError());
    }
    
    /**
     * Checks if the request is a result set too large error.
     * @return true if the request is this type of error
     */
    public boolean isResultSetToLarge() {
        return ErrorCode.RESULT_SET_TO_LARGE.equals(response.getError());
    }
    
    /**
     * Checks if the request is a unsupported profile error.
     * @return true if the request is this type of error
     */
    public boolean isUnsupportedProfile() {
        return ErrorCode.UNSUPPORTED_PROFILE.equals(response.getError());
    }
    
    /**
     * Checks if the request is a invalid identifier error.
     * @return true if the request is this type of error
     */
    public boolean isInvalidIdentifier() {
        return ErrorCode.INVALID_IDENTIFIER.equals(response.getError());
    }
    
    /**
     * Checks if the request is a already exists error.
     * @return true if the request is this type of error
     */
    public boolean isAlreadyExists() {
        return ErrorCode.ALREADY_EXISTS.equals(response.getError());
    }
    
    /**
     * Checks if the request is a container not empty error.
     * @return true if the request is this type of error
     */
    public boolean isContainerNotEmpty() {
        return ErrorCode.CONTAINER_NOT_EMPTY.equals(response.getError());
    }
    
    /**
     * Return the list of error messages the response has.
     * @return The list of error messages or empty array
     */
    public String[] getErrorMessages() {
        return response.getErrorMessage().toArray(new String[0]);
    }
    
    /**
     * Return a string with the errors one per line.
     * @return String error representation (one error per line)
     */
    public String getErrorMessagesInOne() {
        StringBuilder sb = new StringBuilder();
        for (String error: getErrorMessages()) {
            sb.append(error).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
    
    /**
     * Gets the pso type.
     * @return The pso type
     */
    public PSOType getPso() {
        return pso;
    }
    
    /**
     * Gets the accessor of the pso identifier in the type.
     * @return The pso accessor or null
     */
    public PsoIdentifierAccessor getPsoAccessor() {
        if (pso != null && pso.getPsoID() != null) {
            return new PsoIdentifierAccessor(pso.getPsoID());
        } else {
            return null;
        }
    }
    
    /**
     * Gets the pso identifier of the pso type.
     * @return The pso identifier or null
     */
    public String getPsoId() {
        if (pso != null && pso.getPsoID() != null) {
            return pso.getPsoID().getID();
        } else {
            return null;
        }
    }
    
    /**
     * Gets the target identifier of the pso type.
     * @return the target id or null
     */
    public String getPsoTargetId() {
        if (pso != null && pso.getPsoID() != null) {
            return pso.getPsoID().getTargetID();
        } else {
            return null;
        }
    }
    
    /**
     * Gets the PSO identifier of the pso type.
     * @return The pso identifier or nul
     */
    public PSOIdentifierType getPsoIdentifier() {
        if (pso != null) {
            return pso.getPsoID();
        } else {
            return null;
        }
    }
    
    /**
     * Gets the DSML attribute of the PSO type (used in a DSML profile target).
     * @return The array with all the attributes or empty array
     */
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
    
    /**
     * Get the DSML attributes but in a map keyed by the attribute name
     * (used in DSML profile targets).
     * @return The map of attributes or empty map
     */
    public Map<String,DsmlAttr> getDsmlAttributesMap() {
        DsmlAttr[] attrs = getDsmlAttributes();
        Map<String,DsmlAttr> res = new HashMap<>(attrs.length);
        for (DsmlAttr attr: attrs) {
            res.put(attr.getName(), attr);
        }
        return res;
    }
    
    /**
     * Gets the attribute for a specific name (used in DSML profile targets).
     * @param name The name of the attribute
     * @return The DSML attribute or null
     */
    public DsmlAttr getDsmlAttribute(String name) {
        return this.getDsmlAttributesMap().get(name);
    }
    
    /**
     * The values of a DSML attribute (used in DSML profile targets).
     * @param name The attribute name
     * @return The array of values or null
     */
    public String[] getDsmlAttributeValues(String name) {
        DsmlAttr attr = this.getDsmlAttribute(name);
        if (attr != null) {
            return attr.getValue().toArray(new String[0]);
        } else {
            return null;
        }
    }
    
    /**
     * The first value of a DSML attribute (used in DSML profile targets).
     * @param name The attribute name
     * @return The first value of null
     */
    public String getDsmlAttributeFirstValue(String name) {
        String[] values = this.getDsmlAttributeValues(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }
    
    /**
     * Gets the object in a XSD profile target associated a class (the 
     * method should be used in XSD profile targets).
     * 
     * @param clazz The class of the object that the target manages
     * @return The object or null
     */
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
    
    /**
     * The generic method to return an accessor in the form of the
     * passed accessor.
     * 
     * @param <T> The specific response accessor class
     * @param accessor The accessor to create
     * @return The accessor of the specific type
     */
    public <T extends BaseResponseAccessor> T asAccessor(T accessor) {
        if (response.getClass().equals(accessor.response.getClass())) {
            return (T) accessor.asAccessor(response);
        } else {
            return null;
        }
    }
    
    /**
     * Converts the accessor into an unknown accessor.
     * @return The unknown accessor
     */
    public ResponseAccessor asUnknown() {
        return accessorForResponse(response);
    }
    
    /**
     * Converts the current accessor into a ListTargets.
     * @return The specific accessor
     */
    public ListTargetsResponseAccessor asListTargets() {
        return this.asAccessor(new ListTargetsResponseAccessor());
    }
    
    /**
     * Converts the current accessor into an Add.
     * @return The specific accessor
     */
    public AddResponseAccessor asAdd() {
        return this.asAccessor(new AddResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Delete.
     * @return The specific accessor
     */
    public DeleteResponseAccessor asDelete() {
        return this.asAccessor(new DeleteResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a ExpirePassword.
     * @return The specific accessor
     */
    public ExpirePasswordResponseAccessor asExpirePassword() {
        return this.asAccessor(new ExpirePasswordResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a SetPassword.
     * @return The specific accessor
     */
    public SetPasswordResponseAccessor asSetPassword() {
        return this.asAccessor(new SetPasswordResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Suspend.
     * @return The specific accessor
     */
    public SuspendResponseAccessor asSuspend() {
        return this.asAccessor(new SuspendResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Resume.
     * @return The specific accessor
     */
    public ResumeResponseAccessor asResume() {
        return this.asAccessor(new ResumeResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a BulkDelete.
     * @return The specific accessor
     */
    public BulkDeleteResponseAccessor asBulkDelete() {
        return this.asAccessor(new BulkDeleteResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a BulkModify.
     * @return The specific accessor
     */
    public BulkModifyResponseAccessor asBulkModify() {
        return this.asAccessor(new BulkModifyResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a UpdatesCloseIterator.
     * @return The specific accessor
     */
    public UpdatesCloseIteratorResponseAccessor asUpdatesCloseIterator() {
        return this.asAccessor(new UpdatesCloseIteratorResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a CloseIterator.
     * @return The specific accessor
     */
    public CloseIteratorResponseAccessor asCloseIterator() {
        return this.asAccessor(new CloseIteratorResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Lookup.
     * @return The specific accessor
     */
    public LookupResponseAccessor asLookup() {
        return this.asAccessor(new LookupResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Modify.
     * @return The specific accessor
     */
    public ModifyResponseAccessor asModify() {
        return this.asAccessor(new ModifyResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Cancel.
     * @return The specific accessor
     */
    public CancelResponseAccessor asCancel() {
        return this.asAccessor(new CancelResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Status.
     * @return The specific accessor
     */
    public StatusResponseAccessor asStatus() {
        return this.asAccessor(new StatusResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a ResetPassword.
     * @return The specific accessor
     */
    public ResetPasswordResponseAccessor asResetPassword() {
        return this.asAccessor(new ResetPasswordResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a ValidatePassword.
     * @return The specific accessor
     */
    public ValidatePasswordResponseAccessor asValidatePassword() {
        return this.asAccessor(new ValidatePasswordResponseAccessor());
    }
    
    /**
     * Converts the current accessor into an Active.
     * @return The specific accessor
     */
    public ActiveResponseAccessor asActive() {
        return this.asAccessor(new ActiveResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Search.
     * @return The specific accessor
     */
    public SearchResponseAccessor asSearch() {
        return this.asAccessor(new SearchResponseAccessor());
    }
    
    /**
     * Converts the current accessor into a Batch.
     * @return The specific accessor
     */
    public BatchResponseAccessor asBatch() {
        return this.asAccessor(new BatchResponseAccessor());
    }
    
    /**
     * Converts the current accessor into an Updates.
     * @return The specific accessor
     */
    public UpdatesResponseAccessor asUpdates() {
        return this.asAccessor(new UpdatesResponseAccessor());
    }
    
    /**
     * The SPMLv2 permits extensions inside the response. This method
     * let read a object put in the Any property.
     * 
     * @param <T> The type of the object to read (JAXB)
     * @param clazz The class of the objects to read
     * @return The array of objects found
     */
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
    
    /**
     * String representation with the specific class for the XSD data.
     * @param clazz The class of the objects
     * @return The string representation
     */
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
    
    /**
     * String presentation of the internal type.
     * @return The string representation of the response
     */
    @Override
    public String toString() {
        return toString(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R getInternalType() {
        return response;
    }
    
}
