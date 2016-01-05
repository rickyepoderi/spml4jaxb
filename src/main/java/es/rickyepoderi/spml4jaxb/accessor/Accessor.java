/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.accessor;

import es.rickyepoderi.spml4jaxb.builder.Builder;

/**
 *
 * @author ricky
 * @param <T>
 * @param <B>
 */
public interface Accessor<T, B extends Builder> {
    
    public T getInternalType();
    
    public B toBuilder();
    
}
