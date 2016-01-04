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

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.server.HandlerImpl;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricky
 */
public class HttpServer extends Thread {

    private com.sun.net.httpserver.HttpServer server;
    private TestFactory factory;
    private List<WorkExecutor> asyncExecs;

    public HttpServer(String path, int port, int numAsyncExecs) throws SpmlException {
        this(path, port, numAsyncExecs, true);
    }
    
    public HttpServer(String path, int port, int numAsyncExecs, boolean append, Class... factories) throws SpmlException {
        try {
            asyncExecs = new ArrayList<>(numAsyncExecs);
            factory = new TestFactory();
            server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 1024);
            server.createContext(path, new HandlerImpl(factory, append, factories));
            server.setExecutor(null);
            for (int i = 0; i < numAsyncExecs; i++) {
                asyncExecs.add(new WorkExecutor(factory));
            }
        } catch (IOException e) {
            throw new SpmlException(e);
        }
    }
    
    @Override
    public void start() {
        super.start();
        for (WorkExecutor exec: asyncExecs) {
            exec.start();
        }
    }

    @Override
    public void run() {
        server.start();
    }

    public void shutdown(int seconds) {
        server.stop(seconds);
        for (WorkExecutor exec: asyncExecs) {
            exec.shutdown();
        }
        for (WorkExecutor exec: asyncExecs) {
            while (exec.isAlive()) {
                try {
                    exec.join();
                } catch (InterruptedException e) {}
            }
        }
    }
    
    public void addAsyncExecutor() {
        WorkExecutor exec = new WorkExecutor(factory);
        this.asyncExecs.add(exec);
        exec.start();
    }
    
}
