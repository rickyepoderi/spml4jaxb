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

import es.rickyepoderi.spml4jaxb.accessor.Accessor;

/**
 * <p>The builder interface is a simple class that builds any object T. 
 * Builders can be transformed into accessors with the asAccessor method.</p>
 * 
 * @author ricky
 * @param <T> The object to build
 * @param <A> The accessor related to the same object T
 */
public interface Builder<T, A extends Accessor> {
    
    /**
     * Method to obtain the object which the builder builds.
     * 
     * @return The real object
     */
    public T build();
    
    /**
     * Transform the current builder (with the object inside) to an accessor.
     * 
     * @return The accessor for the inner object T
     */
    public A asAccessor();
    
}
