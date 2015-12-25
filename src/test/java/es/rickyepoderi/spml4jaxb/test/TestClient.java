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
package es.rickyepoderi.spml4jaxb.test;

import es.rickyepoderi.spml4jaxb.client.SOAPClient;
import es.rickyepoderi.spml4jaxb.builder.RequestBuilder;
import es.rickyepoderi.spml4jaxb.user.User;

/**
 *
 * @author ricky
 */
public class TestClient {

    static public void main(String[] args) throws Exception {
        try (SOAPClient client = new SOAPClient("http://localhost:8080/SpmlServlet/rpcrouter2",
                es.rickyepoderi.spml4jaxb.msg.dsmlv2.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.core.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.msg.async.ObjectFactory.class,
                es.rickyepoderi.spml4jaxb.user.ObjectFactory.class)) {
            
            System.err.println(RequestBuilder.builderForListTargets()
                    .synchronous()
                    .requestId()
                    .profileXsd()
                    .send(client)
                    .asListTargets());
            
            User u = new User();
            u.setUid("ricky");
            u.setDescription("me");
            u.setCn("Ricardo Martin");
            u.setPassword("ricky");
            u.getRole().add("User");
            u.getRole().add("Admin");
            
            System.err.println(RequestBuilder.builderForAdd()
                    .requestId("add1")
                    .synchronous()
                    .targetId("ddbb-spml-xsd")
                    .xsdObject(u)
                    .send(client)
                    .asAdd()
                    .toString(User.class));
            /*
            System.err.println(RequestBuilder.builderForStatus()
                    .requestId()
                    .synchronous()
                    .asyncRequestId("add1")
                    .send(client)
                    .asStatus().getNestedResponse().asAdd().toString(User.class));
            
            System.err.println(RequestBuilder.builderForCancel()
                    .requestId()
                    .synchronous()
                    .asyncRequestId("add1")
                    .send(client)
                    .asCancel());
            
            System.err.println(RequestBuilder.builderForStatus()
                    .requestId()
                    .synchronous()
                    .asyncRequestId("add1")
                    .send(client)
                    .asStatus());
            */
            
            System.err.println(RequestBuilder.builderForLookup()
                    .requestId()
                    .synchronous()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-xsd")
                    .send(client)
                    .asLookup()
                    .toString(User.class));
            
            
            System.err.println(RequestBuilder.builderForModify()
                    .requestId()
                    .synchronous()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-xsd")
                    .xsdAdd("/user", "<usr:role xmlns:usr=\"urn:ddbb-spml-dsml:user\">Other</usr:role>")
                    .xsdDelete("/user/role[text()='Admin']")
                    .xsdReplace("/usr:user/usr:cn", "<usr:cn xmlns:usr=\"urn:ddbb-spml-dsml:user\">Ricardo Martin Camarero</usr:cn>")
                    .send(client)
                    .asModify()
                    .toString(User.class));            
            
            u.setDescription(null);
            u.setPassword("ricky123");
            u.getRole().add("Other2");
            
            System.err.println(RequestBuilder.builderForModify()
                    .requestId()
                    .synchronous()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-xsd")
                    .xsdReplace("/user", u)
                    .send(client)
                    .asModify()
                    .toString(User.class));
            
            System.err.println(RequestBuilder.builderForLookup()
                    .requestId()
                    .synchronous()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-xsd")
                    .send(client)
                    .asLookup()
                    .toString(User.class));
            
            System.err.println(RequestBuilder.builderForDelete()
                    .requestId()
                    .synchronous()
                    .psoId("ricky")
                    .psoTargetId("ddbb-spml-xsd")
                    .send(client));
        }
    }
}
