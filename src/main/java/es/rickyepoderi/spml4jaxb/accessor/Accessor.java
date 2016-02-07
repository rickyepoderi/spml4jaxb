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

import es.rickyepoderi.spml4jaxb.builder.Builder;

/**
 * <p>The accessor interface is a simple class that access any object T. 
 * Accessors can be transformed into builders with the toBuilder method.</p>
 * 
 * @author ricky
 * @param <T> The object to access
 * @param <A> The same Accessor defined
 * @param <B> The builder conterpart for the same object T
 */
public interface Accessor<T, A extends Accessor, B extends Builder> {
    
    /**
     * Return the internal type that access.
     * @return The internal object T
     */
    public T getInternalType();
    
    /**
     * Method to create the builder for the same inner object.
     * @return The builder counterpart
     */
    public B toBuilder();
    
    /**
     * Create the same accessor using the inner object.
     * @param type The type to create the accessor
     * @return A copy of this accessor
     */
    public A asAccessor(T type);
    
}
