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
package es.rickyepoderi.spml4jaxb.builder;

import es.rickyepoderi.spml4jaxb.client.SpmlException;
import es.rickyepoderi.spml4jaxb.msg.core.ModificationModeType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;
import es.rickyepoderi.spml4jaxb.msg.dsmlv2.DsmlModification;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Intermediate class for managing dsml and XSD modifications. It is used
 * later in core ModificationRequest and BulkModificationRequest.
 * 
 * @author ricky
 * @param <R>
 * @param <B>
 */
public abstract class ModificationRequestBuilder<R extends RequestType, B extends RequestBuilder> extends RequestBuilder<R, B>  {

    public ModificationRequestBuilder(R request) {
        super(request);
    }
    
    public B dsmlReplace(String name, String... values) {
        DsmlModification dsmlMod = new DsmlModification();
        dsmlMod.setOperation(ModificationModeType.REPLACE.value());
        dsmlMod.setName(name);
        dsmlMod.getValue().addAll(Arrays.asList(values));
        return dsmlModification(dsmlMod);
    }
    
    public B dsmlAdd(String name, String... values) {
        DsmlModification dsmlMod = new DsmlModification();
        dsmlMod.setOperation(ModificationModeType.ADD.value());
        dsmlMod.setName(name);
        dsmlMod.getValue().addAll(Arrays.asList(values));
        return dsmlModification(dsmlMod);
    }
    
    public B dsmlDelete(String name, String... values) {
        DsmlModification dsmlMod = new DsmlModification();
        dsmlMod.setOperation(ModificationModeType.DELETE.value());
        dsmlMod.setName(name);
        dsmlMod.getValue().addAll(Arrays.asList(values));
        return dsmlModification(dsmlMod);
    }
    
    public abstract B dsmlModification(DsmlModification dsmlMod);
    
    public B xsdAdd(String xpath, Object o) {
        return xsdModification(ModificationModeType.ADD, xpath, o);
    }
    
    public B xsdReplace(String xpath, Object o) {
        return xsdModification(ModificationModeType.REPLACE, xpath, o);
    }
    
    public B xsdDelete(String xpath) {
        return xsdModification(ModificationModeType.DELETE, xpath, null);
    }
    
    public B xsdAdd(String xpath, Document doc) {
        return xsdAdd(xpath, (Object) doc.getDocumentElement());
    }
    
    public B xsdReplace(String xpath, Document doc) {
        return xsdReplace(xpath, (Object) doc.getDocumentElement());
    }
    
    public B xsdAdd(String xpath, String xml) throws SpmlException {
        try {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return xsdAdd(xpath, doc);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new SpmlException(e);
        }
    }
    
    public B xsdReplace(String xpath, String xml) throws SpmlException {
        try {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return xsdReplace(xpath, doc);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new SpmlException(e);
        }
    }
    
    public abstract B xsdModification(ModificationModeType type, String xpath, Object o);
    
}
