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
import es.rickyepoderi.spml4jaxb.msg.async.CancelRequestType;
import es.rickyepoderi.spml4jaxb.msg.async.StatusRequestType;
import es.rickyepoderi.spml4jaxb.msg.batch.BatchRequestType;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkDeleteRequestType;
import es.rickyepoderi.spml4jaxb.msg.bulk.BulkModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.AddRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.DeleteRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ListTargetsRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.LookupRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.ModifyRequestType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.password.ExpirePasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.password.ResetPasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.password.SetPasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.password.ValidatePasswordRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.CloseIteratorRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.IterateRequestType;
import es.rickyepoderi.spml4jaxb.msg.search.SearchRequestType;
import es.rickyepoderi.spml4jaxb.msg.suspend.ActiveRequestType;
import es.rickyepoderi.spml4jaxb.msg.suspend.ResumeRequestType;
import es.rickyepoderi.spml4jaxb.msg.suspend.SuspendRequestType;
import es.rickyepoderi.spml4jaxb.msg.updates.UpdatesRequestType;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import es.rickyepoderi.spml4jaxb.server.SpmlMapperExecutorFactory;
import es.rickyepoderi.spml4jaxb.test.clone.CloneRequestType;
import es.rickyepoderi.spml4jaxb.user.UserManager;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import org.hsqldb.jdbc.JDBCDataSource;

/**
 *
 * @author ricky
 */
public class TestFactory implements SpmlMapperExecutorFactory {

    private UserManager um;
    private WorkQueue queue;
    private Map<Class<? extends RequestType>, SpmlExecutor> mapper;
    
    @Override
    public Map<Class<? extends RequestType>, SpmlExecutor> createMapper(JAXBContext ctx) throws SpmlException {
        try {
            JDBCDataSource dataSource = new JDBCDataSource();
            dataSource.setDatabase("jdbc:hsqldb:mem:mydb");
            dataSource.setUser("SA");
            dataSource.setPassword("");
            queue = new WorkQueue(ctx, dataSource);
            um = new UserManager(dataSource);
            um.initialize();
            mapper = new HashMap<>();
            // core
            mapper.put(ListTargetsRequestType.class, new ListTargetsExecutor());
            mapper.put(AddRequestType.class, new AddExecutor(um, queue));
            mapper.put(LookupRequestType.class, new LookupExecutor(um, queue));
            mapper.put(ModifyRequestType.class, new ModifyExecutor(um, queue, ctx));
            mapper.put(DeleteRequestType.class, new DeleteExecutor(um, queue));
            // async
            mapper.put(StatusRequestType.class, new StatusExecutor(queue));
            mapper.put(CancelRequestType.class, new CancelExecutor(queue));
            // password
            mapper.put(SetPasswordRequestType.class, new SetPasswordExecutor(um, queue));
            mapper.put(ExpirePasswordRequestType.class, new ExpirePasswordExecutor(um, queue));
            mapper.put(ResetPasswordRequestType.class, new ResetPasswordExecutor(um, queue));
            mapper.put(ValidatePasswordRequestType.class, new ValidatePasswordExecutor(um, queue));
            // suspend
            mapper.put(SuspendRequestType.class, new SuspendExecutor(um, queue));
            mapper.put(ResumeRequestType.class, new ResumeExecutor(um, queue));
            mapper.put(ActiveRequestType.class, new ActiveExecutor(um, queue));
            // search
            mapper.put(SearchRequestType.class, new SearchExecutor(um, queue, ctx, 2));
            mapper.put(IterateRequestType.class, new IterateExecutor(queue, 2));
            mapper.put(CloseIteratorRequestType.class, new CloseIteratorExecutor(queue));
            // bulk
            mapper.put(BulkDeleteRequestType.class, new BulkDeleteExecutor(um, queue));
            mapper.put(BulkModifyRequestType.class, new BulkModifyExecutor(um, queue, ctx));
            // batch
            mapper.put(BatchRequestType.class, new BatchExecutor(queue, mapper));
            // updates
            mapper.put(UpdatesRequestType.class, new UpdatesExecutor(um, queue, 2));
            mapper.put(es.rickyepoderi.spml4jaxb.msg.updates.IterateRequestType.class, new UpdatesIterateExecutor(queue, 2));
            mapper.put(es.rickyepoderi.spml4jaxb.msg.updates.CloseIteratorRequestType.class, new UpdatesCloseIterator(queue));
            // clone extension
            mapper.put(CloneRequestType.class, new CloneExecutor(um, queue));
            return mapper;
        } catch (Exception e) {
            throw new SpmlException(e);
        }
    }
    
    public UserManager getUserManager() {
        return um;
    }
    
    public WorkQueue getWorkQueue() {
        return queue;
    }
    
    public Map<Class<? extends RequestType>, SpmlExecutor> getMapper() {
        return mapper;
    }
    
}
