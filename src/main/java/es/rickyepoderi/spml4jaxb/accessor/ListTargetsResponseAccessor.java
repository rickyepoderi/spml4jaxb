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

import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsResponseType;
import es.rickyepoderi.spml4jaxb.msg.core.TargetType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricky
 */
public class ListTargetsResponseAccessor extends ResponseAccessor<ListTargetsResponseType> {

    protected ListTargetsResponseAccessor(ListTargetsResponseType response) {
        super(response, null);
    }
    
    public TargetAccessor[] getTargets() {
        List<TargetAccessor> res = new ArrayList<>();
        for (TargetType t: response.getTarget()) {
            res.add(new TargetAccessor(t));
        }
        return res.toArray(new TargetAccessor[0]);
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder(super.toString());
        for (TargetAccessor target: getTargets()) {
            sb.append(target);
        }
        return sb.toString();
    }
}
