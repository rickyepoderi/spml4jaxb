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

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.accessor.ListTargetsRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ListTargetsResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.builder.TargetBuilder;
import es.rickyepoderi.spml4jaxb.server.SpmlExecutor;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 *
 * @author ricky
 */
public class ListTargetsExecutor implements SpmlExecutor {

    static public final String DSML_TARGET_ID = "ddbb-spml-dsml";
    
    static public final String XSD_TARGET_ID = "ddbb-spml-xsd";
    
    static public TargetBuilder dsml;
    
    static public TargetBuilder xsd;
            
    static {
        try {
        dsml = ResponseBuilder.builderForTarget()
                    .profileDsml()
                    .targetId(DSML_TARGET_ID)
                    .capabilityAsync()
                    .capabilityPassword("user")
                    .capabilitySearch()
                    .capabilitySuspend("user")
                    .schema(ResponseBuilder.builderForSchema()
                            .supportedSchemaEntity("user")
                            .attributeDefinition("uid", "User name")
                            .attributeDefinition("password", "password for the user")
                            .attributeDefinition("cn", "Common name")
                            .attributeDefinition("description", "description")
                            .attributeDefinition("role", "roles of the user", true)
                            .objectclassDefinition("user")
                            .objectclassDefinitionAttrDefRequired("user", "uid", "password", "cn")
                            .objectclassDefinitionAttrDef("user", "description", "role")
                    );
    
        xsd = ResponseBuilder.builderForTarget()
                    .profileXsd()
                    .targetId(XSD_TARGET_ID)
                    .capabilityAsync()
                    .capabilityPassword("user")
                    .capabilitySearch()
                    .capabilitySuspend("user")
                    .schema(ResponseBuilder.builderForSchema()
                            .supportedSchemaEntity("user")
                            .xsdSchema(new ByteArrayInputStream(
                                            ("<?xml version=\"1.0\"?>"
                                            + "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://www.w3.org/2001/XMLSchema\" xmlns:spml=\"urn:oasis:names:tc:SPML:2:0\" elementFormDefault=\"qualified\" targetNamespace=\"urn:ddbb-spml-dsml:user\" version=\"1.0\">"
                                            + "  <element name=\"user\">"
                                            + "    <complexType>"
                                            + "      <sequence>"
                                            + "        <element maxOccurs=\"1\" minOccurs=\"1\" name=\"uid\" type=\"string\"/>"
                                            + "        <element maxOccurs=\"1\" minOccurs=\"0\" name=\"password\" type=\"string\"/>"
                                            + "        <element maxOccurs=\"1\" minOccurs=\"1\" name=\"cn\" type=\"string\"/>"
                                            + "        <element maxOccurs=\"1\" minOccurs=\"0\" name=\"description\" type=\"string\"/>"
                                            + "        <element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"role\" type=\"string\"/>"
                                            + "      </sequence>"
                                            + "    </complexType>"
                                            + "  </element>"
                                            + "</xs:schema>").getBytes(StandardCharsets.UTF_8)))
                    );
        } catch (SpmlException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public ResponseBuilder execute(RequestAccessor request) {
        ListTargetsRequestAccessor req = request.asListTargets();
        String id = request.getRequestId() == null? UUID.randomUUID().toString() : req.getRequestId();
        ListTargetsResponseBuilder builder = ResponseBuilder.builderForListTargets().requestId(id);
        if (req.isAsynchronous()) {
            return builder.failure()
                    .unsupportedExecutionMode()
                    .errorMessage("ListTargets should be synchronous by the standard");
        }
        if (req.getProfile() == null || req.isDsml()) {
            builder.target(dsml);
        }
        if (req.getProfile() == null || req.isXsd()) {
            builder.target(xsd);
        }
        return builder.success();
    }
    
    
}
