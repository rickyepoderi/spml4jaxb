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
package es.rickyepoderi.spml4jaxb.test;

/**
 *
 * @author ricky
 */
public class WorkExecutor extends Thread {
    
    private TestFactory fact;
    private boolean stop;
    
    public WorkExecutor(TestFactory fact) {
        this.fact = fact;
        this.stop = false;
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                WorkQueue.WorkItem item = fact.getWorkQueue().retrieve(5000L);
                System.err.println("Retrieved " + item);
                if (item != null) {
                    AsyncSpmlExecutor exec = (AsyncSpmlExecutor) fact.getMapper().get(item.getRequestAccessor().getRequestClass());
                    if (exec != null) {
                        // execute the request and the response builder
                        exec.realExecute(item);
                        System.err.println("Job finished!!!");
                    } else {
                        // mark builder as error
                        throw new Exception("Operation not supported!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void shutdown() {
        this.stop = true;
    }
}
