//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.24 at 12:33:22 PM CEST 
//


package es.rickyepoderi.spml4jaxb.msg.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ModifyResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SPML:2:0}ResponseType">
 *       &lt;sequence>
 *         &lt;element name="pso" type="{urn:oasis:names:tc:SPML:2:0}PSOType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyResponseType", propOrder = {
    "pso"
})
public class ModifyResponseType
    extends ResponseType
{

    protected PSOType pso;

    /**
     * Gets the value of the pso property.
     * 
     * @return
     *     possible object is
     *     {@link PSOType }
     *     
     */
    public PSOType getPso() {
        return pso;
    }

    /**
     * Sets the value of the pso property.
     * 
     * @param value
     *     allowed object is
     *     {@link PSOType }
     *     
     */
    public void setPso(PSOType value) {
        this.pso = value;
    }

}
