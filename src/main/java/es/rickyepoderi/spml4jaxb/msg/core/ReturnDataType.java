//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.24 at 12:33:22 PM CEST 
//


package es.rickyepoderi.spml4jaxb.msg.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReturnDataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReturnDataType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="identifier"/>
 *     &lt;enumeration value="data"/>
 *     &lt;enumeration value="everything"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReturnDataType")
@XmlEnum
public enum ReturnDataType {

    @XmlEnumValue("identifier")
    IDENTIFIER("identifier"),
    @XmlEnumValue("data")
    DATA("data"),
    @XmlEnumValue("everything")
    EVERYTHING("everything");
    private final String value;

    ReturnDataType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReturnDataType fromValue(String v) {
        for (ReturnDataType c: ReturnDataType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}