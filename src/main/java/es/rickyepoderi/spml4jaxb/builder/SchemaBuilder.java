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

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaEntityRefType;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.AttributeDefinitionReferenceType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.AttributeDefinitionReferencesType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.AttributeDefinitionType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectClassDefinitionType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author ricky
 */
public class SchemaBuilder implements Builder<SchemaType> {
    
    protected SchemaType schema;
    protected es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType dsmlSchema;
    
    protected SchemaBuilder() {
        this.schema = new SchemaType();
        this.dsmlSchema = new es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType();
    }
    
    public SchemaBuilder supportedSchemaEntity(String... names) {
        for (String name : names) {
            SchemaEntityRefType entity = new SchemaEntityRefType();
            entity.setEntityName(name);
            schema.getSupportedSchemaEntity().add(entity);
        }
        return this;
    }
    
    public SchemaBuilder supportedSchemaContainer(String... names) {
        for (String name : names) {
            SchemaEntityRefType entity = new SchemaEntityRefType();
            entity.setEntityName(name);
            entity.setIsContainer(true);
            schema.getSupportedSchemaEntity().add(entity);
        }
        return this;
    }
    
    public SchemaBuilder reference(String ref) {
        schema.setRef(ref);
        return this;
    }
    
    public SchemaBuilder attributeDefinition(String name) {
        return attributeDefinition(name, null, false, null);
    }
    
    public SchemaBuilder attributeDefinition(String name, String desc) {
        return attributeDefinition(name, null, false, desc);
    }
    
    public SchemaBuilder attributeDefinition(String name, String desc, boolean multi) {
        return attributeDefinition(name, null, multi, desc);
    }
    
    public SchemaBuilder attributeDefinition(String name, String type, boolean multi, String desc) {
        AttributeDefinitionType attr = new AttributeDefinitionType();
        attr.setName(name);
        attr.setType(type);
        attr.setMultivalued(multi);
        attr.setDescription(desc);
        dsmlSchema.getAttributeDefinition().add(attr);
        return this;
    }
    
    public SchemaBuilder objectclassDefinition(String name) {
        return objectclassDefinition(name, null);
    }
    
    public SchemaBuilder objectclassDefinition(String name, String desc) {
        ObjectClassDefinitionType oc = new ObjectClassDefinitionType();
        oc.setName(name);
        oc.setDescription(desc);
        dsmlSchema.getObjectClassDefinition().add(oc);
        System.err.println("Added!!!!");
        return this;
    }
    
    protected ObjectClassDefinitionType getObjectclassDefinition(String name) {
        for (ObjectClassDefinitionType oc: dsmlSchema.getObjectClassDefinition()) {
            System.err.println(oc.getName());
            if (oc.getName().equals(name)) {
                System.err.println("returning...");
                return oc;
            }
        }
        return null;
    }
    
    public SchemaBuilder objectclassDefinitionAttrDef(String ocName, String... attrName) {
        ObjectClassDefinitionType oc = getObjectclassDefinition(ocName);
        if (oc == null) {
            objectclassDefinition(ocName);
            oc = getObjectclassDefinition(ocName);
        }
        if (oc.getMemberAttributes() == null) {
            oc.setMemberAttributes(new AttributeDefinitionReferencesType());
        }
        for (String name: attrName) {
            AttributeDefinitionReferenceType ref = new AttributeDefinitionReferenceType();
            ref.setName(name);
            oc.getMemberAttributes().getAttributeDefinitionReference().add(ref);
        }
        return this;
    }
    
    public SchemaBuilder objectclassDefinitionAttrDefRequired(String ocName, String... attrName) {
        ObjectClassDefinitionType oc = getObjectclassDefinition(ocName);
        System.err.println(oc);
        if (oc == null) {
            objectclassDefinition(ocName);
            oc = getObjectclassDefinition(ocName);
        }
        if (oc.getMemberAttributes() == null) {
            oc.setMemberAttributes(new AttributeDefinitionReferencesType());
        }
        for (String name: attrName) {
            AttributeDefinitionReferenceType ref = new AttributeDefinitionReferenceType();
            ref.setName(name);
            ref.setRequired(true);
            oc.getMemberAttributes().getAttributeDefinitionReference().add(ref);
        }
        return this;
    }
    
    public SchemaBuilder xsdSchema(File file) throws SpmlException {
        try (InputStream is = new FileInputStream(file)){
            return xsdSchema(is);
        } catch (IOException e) {
            throw new SpmlException(e);
        }
    }
    
    public SchemaBuilder xsdSchema(InputStream is) throws SpmlException {
        try {
            DocumentBuilder db = RequestBuilder.documentBuilderFactory.newDocumentBuilder();
            Document doc = db.parse(is);
            schema.getAny().add(doc.getDocumentElement());
            return this;
        } catch (ParserConfigurationException|IOException|SAXException e) {
            throw new SpmlException(e);
        }
    }
    
    @Override
    public SchemaType build() {
        if (!dsmlSchema.getAttributeDefinition().isEmpty()) {
            schema.getAny().add(RequestBuilder.spmldsmlObjectFactory.createSchema(dsmlSchema));
        }
        return schema;
    }
    
}
