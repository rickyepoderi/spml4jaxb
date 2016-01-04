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

import es.rickyepoderi.spml4jaxb.accessor.AddRequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.AddResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import java.sql.SQLException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.hsqldb.jdbc.JDBCDataSource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class WorkQueueTest {
    
    private WorkQueue queue;
    
    public WorkQueueTest() throws SQLException, JAXBException {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setDatabase("jdbc:hsqldb:mem:mydb");
        dataSource.setUser("SA");
        dataSource.setPassword("");
        this.queue = new WorkQueue(
                JAXBContext.newInstance(es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                        es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                        es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class),
                dataSource);
    }
    
    @Test
    public void testFinish() throws Exception {
        // insert an add request to the queue
        AddRequestAccessor addreq = RequestBuilder.builderForAdd()
                .requestId()
                .asynchronous()
                .returnData()
                .psoId("testadd")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .asAccessor()
                .asAdd();
        queue.insert(addreq.getRequestId(), addreq);
        // find that the request exists as pending
        WorkQueue.WorkItem item = queue.status(addreq.getRequestId());
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getId(), addreq.getRequestId());
        Assert.assertTrue(item.isPending());
        Assert.assertNotNull(item.getRequestAccessor());
        Assert.assertNull(item.getResponseAccessor());
        // get the request
        item = queue.retrieve(2000L);
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getId(), addreq.getRequestId());
        Assert.assertTrue(item.isPending());
        Assert.assertNotNull(item.getRequestAccessor());
        Assert.assertNull(item.getResponseAccessor());
        // finish the request
        AddResponseBuilder addres = ResponseBuilder.builderForAdd()
                .requestId(addreq.getRequestId())
                .failure()
                .noSuchIdentifier();
        queue.finish(item, addres);
        // find again and check it's finish
        item = queue.status(addreq.getRequestId());
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getId(), addreq.getRequestId());
        Assert.assertEquals(item.getStatus(), WorkQueue.WorkItemStatus.FINISHED);
        Assert.assertNotNull(item.getRequestAccessor());
        Assert.assertNotNull(item.getResponseAccessor());
        // check nothing is returned
        item = queue.retrieve(2000L);
        Assert.assertNull(item);
    }
    
    @Test
    public void testCancel() throws Exception {
        // insert an add request to the queue
        AddRequestAccessor addreq = RequestBuilder.builderForAdd()
                .requestId()
                .asynchronous()
                .returnData()
                .psoId("testadd")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .asAccessor()
                .asAdd();
        queue.insert(addreq.getRequestId(), addreq);
        // find that the request exists as pending
        WorkQueue.WorkItem item = queue.status(addreq.getRequestId());
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getId(), addreq.getRequestId());
        Assert.assertTrue(item.isPending());
        Assert.assertNotNull(item.getRequestAccessor());
        Assert.assertNull(item.getResponseAccessor());
        // cancel the request
        queue.cancel(item.getId());
        // find again and check it's finish
        item = queue.status(addreq.getRequestId());
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getId(), addreq.getRequestId());
        Assert.assertEquals(item.getStatus(), WorkQueue.WorkItemStatus.CANCELED);
        Assert.assertNotNull(item.getRequestAccessor());
        Assert.assertNull(item.getResponseAccessor());
        // check nothing is returned
        item = queue.retrieve(2000L);
        Assert.assertNull(item);
    }
    
    @Test
    public void testInsertFinish() throws Exception {
        AddRequestAccessor addreq = RequestBuilder.builderForAdd()
                .requestId()
                .asynchronous()
                .returnData()
                .psoId("testadd")
                .psoTargetId("testtargetid")
                .targetId("testtargetid")
                .containerId("testcontainerid")
                .containerTargetId("testcontainertargetid")
                .dsmlAttribute("name1", "value11", "value12", "value13")
                .dsmlAttribute("name2", "value21")
                .asAccessor()
                .asAdd();
        AddResponseBuilder addres = ResponseBuilder.builderForAdd()
                .requestId(addreq.getRequestId())
                .failure()
                .noSuchIdentifier();
        WorkQueue.WorkItem item = new WorkQueue.WorkItem(addreq.getRequestId(), addreq);
        queue.finish(item, addres);
        // find again and check it's finish
        item = queue.status(addreq.getRequestId());
        Assert.assertNotNull(item);
        Assert.assertEquals(item.getId(), addreq.getRequestId());
        Assert.assertEquals(item.getStatus(), WorkQueue.WorkItemStatus.FINISHED);
        Assert.assertNotNull(item.getRequestAccessor());
        Assert.assertNotNull(item.getResponseAccessor());
        // check nothing is returned
        item = queue.retrieve(2000L);
        Assert.assertNull(item);
    }
}
