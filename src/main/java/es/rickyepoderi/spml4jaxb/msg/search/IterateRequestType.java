//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.31 at 07:40:23 PM CEST 
//


package es.rickyepoderi.spml4jaxb.msg.search;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;


/**
 * <p>Java class for IterateRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IterateRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SPML:2:0}RequestType">
 *       &lt;sequence>
 *         &lt;element name="iterator" type="{urn:oasis:names:tc:SPML:2:0:search}ResultsIteratorType"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace=''/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IterateRequestType", propOrder = {
    "iterator"
})
public class IterateRequestType
    extends RequestType
{

    @XmlElement(required = true)
    protected ResultsIteratorType iterator;

    /**
     * Gets the value of the iterator property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsIteratorType }
     *     
     */
    public ResultsIteratorType getIterator() {
        return iterator;
    }

    /**
     * Sets the value of the iterator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsIteratorType }
     *     
     */
    public void setIterator(ResultsIteratorType value) {
        this.iterator = value;
    }

}
