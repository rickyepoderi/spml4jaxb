//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.24 at 03:27:24 PM CEST 
//


package es.rickyepoderi.spml4jaxb.msg.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompareRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompareRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage">
 *       &lt;sequence>
 *         &lt;element name="assertion" type="{urn:oasis:names:tc:DSML:2:0:core}AttributeValueAssertion"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dn" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlDN" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompareRequest", propOrder = {
    "assertion"
})
public class CompareRequest
    extends DsmlMessage
{

    @XmlElement(required = true)
    protected AttributeValueAssertion assertion;
    @XmlAttribute(name = "dn", required = true)
    protected String dn;

    /**
     * Gets the value of the assertion property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public AttributeValueAssertion getAssertion() {
        return assertion;
    }

    /**
     * Sets the value of the assertion property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueAssertion }
     *     
     */
    public void setAssertion(AttributeValueAssertion value) {
        this.assertion = value;
    }

    /**
     * Gets the value of the dn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDn() {
        return dn;
    }

    /**
     * Sets the value of the dn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDn(String value) {
        this.dn = value;
    }

}
