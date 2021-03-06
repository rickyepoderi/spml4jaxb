//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.24 at 02:52:06 PM CEST 
//


package es.rickyepoderi.spml4jaxb.msg.spmldsml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import es.rickyepoderi.spml4jaxb.msg.core.ExtensibleType;


/**
 * <p>Java class for ObjectClassDefinitionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectClassDefinitionType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SPML:2:0}ExtensibleType">
 *       &lt;sequence>
 *         &lt;element name="memberAttributes" type="{urn:oasis:names:tc:SPML:2:0:DSML}AttributeDefinitionReferencesType" minOccurs="0"/>
 *         &lt;element name="superiorClasses" type="{urn:oasis:names:tc:SPML:2:0:DSML}ObjectClassDefinitionReferencesType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;anyAttribute processContents='lax' namespace=''/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectClassDefinitionType", propOrder = {
    "memberAttributes",
    "superiorClasses"
})
public class ObjectClassDefinitionType
    extends ExtensibleType
{

    protected AttributeDefinitionReferencesType memberAttributes;
    protected ObjectClassDefinitionReferencesType superiorClasses;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the memberAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeDefinitionReferencesType }
     *     
     */
    public AttributeDefinitionReferencesType getMemberAttributes() {
        return memberAttributes;
    }

    /**
     * Sets the value of the memberAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeDefinitionReferencesType }
     *     
     */
    public void setMemberAttributes(AttributeDefinitionReferencesType value) {
        this.memberAttributes = value;
    }

    /**
     * Gets the value of the superiorClasses property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectClassDefinitionReferencesType }
     *     
     */
    public ObjectClassDefinitionReferencesType getSuperiorClasses() {
        return superiorClasses;
    }

    /**
     * Sets the value of the superiorClasses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectClassDefinitionReferencesType }
     *     
     */
    public void setSuperiorClasses(ObjectClassDefinitionReferencesType value) {
        this.superiorClasses = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
