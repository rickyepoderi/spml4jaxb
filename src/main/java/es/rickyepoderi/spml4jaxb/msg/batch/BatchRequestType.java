//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.03 at 11:44:53 AM CEST 
//


package es.rickyepoderi.spml4jaxb.msg.batch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import es.rickyepoderi.spml4jaxb.msg.core.RequestType;


/**
 * <p>Java class for BatchRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BatchRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:SPML:2:0}RequestType">
 *       &lt;attribute name="processing" type="{urn:oasis:names:tc:SPML:2:0:batch}ProcessingType" default="sequential" />
 *       &lt;attribute name="onError" type="{urn:oasis:names:tc:SPML:2:0:batch}OnErrorType" default="exit" />
 *       &lt;anyAttribute processContents='lax' namespace=''/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchRequestType")
public class BatchRequestType
    extends RequestType
{

    @XmlAttribute(name = "processing")
    protected ProcessingType processing;
    @XmlAttribute(name = "onError")
    protected OnErrorType onError;

    /**
     * Gets the value of the processing property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessingType }
     *     
     */
    public ProcessingType getProcessing() {
        if (processing == null) {
            return ProcessingType.SEQUENTIAL;
        } else {
            return processing;
        }
    }

    /**
     * Sets the value of the processing property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessingType }
     *     
     */
    public void setProcessing(ProcessingType value) {
        this.processing = value;
    }

    /**
     * Gets the value of the onError property.
     * 
     * @return
     *     possible object is
     *     {@link OnErrorType }
     *     
     */
    public OnErrorType getOnError() {
        if (onError == null) {
            return OnErrorType.EXIT;
        } else {
            return onError;
        }
    }

    /**
     * Sets the value of the onError property.
     * 
     * @param value
     *     allowed object is
     *     {@link OnErrorType }
     *     
     */
    public void setOnError(OnErrorType value) {
        this.onError = value;
    }

}
