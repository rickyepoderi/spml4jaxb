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

import es.rickyepoderi.spml4jaxb.builder.SchemaBuilder;
import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaEntityRefType;
import es.rickyepoderi.spml4jaxb.msg.core.SchemaType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.AttributeDefinitionReferenceType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.AttributeDefinitionType;
import es.rickyepoderi.spml4jaxb.msg.spmldsml.ObjectClassDefinitionType;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author ricky
 */
public class SchemaAccessor implements Accessor<SchemaType, SchemaAccessor, SchemaBuilder>{
    
    protected SchemaType schema;
    
    public SchemaAccessor(SchemaType schema) {
        this.schema = schema;
    }
    
    public Set<String> getSupportedEntityNames() {
        Set<String> res = new HashSet<>();
        for (SchemaEntityRefType ref: schema.getSupportedSchemaEntity()) {
            res.add(ref.getEntityName());
        }
        return res;
    }
    
    public boolean isEntityContainer(String name) {
        for (SchemaEntityRefType ref: schema.getSupportedSchemaEntity()) {
            if (name.equals(ref.getEntityName())) {
                return ref.isIsContainer() == null? false:ref.isIsContainer();
            }
        }
        return false;
    }
    
    public String getReference() {
        return schema.getRef();
    }
    
    public es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType getDsmlSchema() {
        List<Object> data = schema.getAny();
        for (Object o : data) {
            if (o instanceof es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType) {
                return (es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType) o;
            } else if (o instanceof JAXBElement) {
                if (((JAXBElement) o).getValue() instanceof es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType) {
                    return (es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType) ((JAXBElement) o).getValue();
                }
            }
        }
        return null;
    }
    
    public AttributeDefinitionType[] getDsmlAttributeDefinitions() {
        es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType dsmlSchema = getDsmlSchema();
        if (dsmlSchema != null) {
            return dsmlSchema.getAttributeDefinition().toArray(new AttributeDefinitionType[0]);
        }
        return new AttributeDefinitionType[0];
    }
    
    public ObjectClassDefinitionType[] getDsmlObjectClassDefinition() {
        es.rickyepoderi.spml4jaxb.msg.spmldsml.SchemaType dsmlSchema = getDsmlSchema();
        if (dsmlSchema != null) {
            return dsmlSchema.getObjectClassDefinition().toArray(new ObjectClassDefinitionType[0]);
        }
        return new ObjectClassDefinitionType[0];
    }
    
    public String getXsdSchemaString() throws SpmlException {
        Document doc = getXsdSchemaDocument();
        if (doc != null) {
            try {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StringWriter sw = new StringWriter();
                transformer.transform(source, new StreamResult(sw));
                return sw.toString();
            } catch (TransformerException e) {
                throw new SpmlException(e);
            }
        }
        return null;
    }
    
    public Document getXsdSchemaDocument() throws SpmlException {
        for (Object o : schema.getAny()) {
            if (o instanceof Element) {
                Element el = (Element) o;
                if ("http://www.w3.org/2001/XMLSchema".equals(el.getNamespaceURI())
                        && "schema".equals(el.getLocalName())) {
                    try {
                        Document doc = RequestAccessor.documentBuilderFactory.newDocumentBuilder().newDocument();
                        doc.adoptNode(el);
                        doc.appendChild(el);
                        return doc;
                    } catch (DOMException|ParserConfigurationException e) {
                        throw new SpmlException(e);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder("    Schema").append(nl)
                .append("      entities: ").append(getSupportedEntityNames()).append(nl)
                .append("      ref: ").append(getReference()).append(nl);
        if (getDsmlAttributeDefinitions() != null) {
            sb.append("      AttributeDefinitions:").append(nl);
            for (AttributeDefinitionType attr : getDsmlAttributeDefinitions()) {
                sb.append("        name: ").append(attr.getName())
                        .append(" type: ").append(attr.getType())
                        .append(" multi: ").append(attr.isMultivalued())
                        .append(" desc: ").append(attr.getDescription()).append(nl);
            }
        }
        if (getDsmlObjectClassDefinition() != null) {
            sb.append("      ObjectclassDefinitions:").append(nl);
            for (ObjectClassDefinitionType obj : getDsmlObjectClassDefinition()) {
                sb.append("        name: ").append(obj.getName())
                        .append(" desc: ").append(obj.getDescription()).append(nl);
                if (obj.getMemberAttributes() != null && obj.getMemberAttributes().getAttributeDefinitionReference() != null) {
                    for (AttributeDefinitionReferenceType def : obj.getMemberAttributes().getAttributeDefinitionReference()) {
                        sb.append("          name: ").append(def.getName())
                                .append(" schema: ").append(def.getSchema())
                                .append(" req: ").append(def.isRequired()).append(nl);
                    }
                }
            }
        }
        try {
            String xmlSchema = getXsdSchemaString();
            if (xmlSchema != null) {
                sb.append("      Schema:").append(nl).append(xmlSchema).append(nl);
            }
        } catch (SpmlException e) {
            // no-op
        }
        return sb.toString();
    }

    @Override
    public SchemaType getInternalType() {
        return this.schema;
    }

    @Override
    public SchemaBuilder toBuilder() {
        return new SchemaBuilder(this.schema, this.getDsmlSchema());
    }

    @Override
    public SchemaAccessor asAccessor(SchemaType type) {
        return new SchemaAccessor(type);
    }
}
