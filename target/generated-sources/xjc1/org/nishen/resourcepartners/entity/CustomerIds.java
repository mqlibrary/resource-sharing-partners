//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.27 at 11:50:46 AM AEST 
//


package org.nishen.resourcepartners.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * List of British Library customer IDs. At least one customer id should be defined.
 * 
 * <p>Java class for customer_ids complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customer_ids"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="customer_id" type="{}customer_id" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customer_ids", propOrder = {
    "customerId"
})
public class CustomerIds
    implements Equals, HashCode, ToString
{

    @XmlElement(name = "customer_id")
    protected List<CustomerId> customerId;

    /**
     * Gets the value of the customerId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerId }
     * 
     * 
     */
    public List<CustomerId> getCustomerId() {
        if (customerId == null) {
            customerId = new ArrayList<CustomerId>();
        }
        return this.customerId;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            List<CustomerId> theCustomerId;
            theCustomerId = (((this.customerId!= null)&&(!this.customerId.isEmpty()))?this.getCustomerId():null);
            strategy.appendField(locator, this, "customerId", buffer, theCustomerId);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof CustomerIds)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final CustomerIds that = ((CustomerIds) object);
        {
            List<CustomerId> lhsCustomerId;
            lhsCustomerId = (((this.customerId!= null)&&(!this.customerId.isEmpty()))?this.getCustomerId():null);
            List<CustomerId> rhsCustomerId;
            rhsCustomerId = (((that.customerId!= null)&&(!that.customerId.isEmpty()))?that.getCustomerId():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "customerId", lhsCustomerId), LocatorUtils.property(thatLocator, "customerId", rhsCustomerId), lhsCustomerId, rhsCustomerId)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<CustomerId> theCustomerId;
            theCustomerId = (((this.customerId!= null)&&(!this.customerId.isEmpty()))?this.getCustomerId():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "customerId", theCustomerId), currentHashCode, theCustomerId);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}