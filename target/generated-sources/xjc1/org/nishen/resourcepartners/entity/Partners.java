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
import javax.xml.bind.annotation.XmlAttribute;
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
 * A list of Resource Sharing Partners.
 * 
 * <p>Java class for partners complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="partners"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="partner" type="{}partner" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="total_record_count" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "partners", propOrder = {
    "partner"
})
public class Partners
    implements Equals, HashCode, ToString
{

    protected List<Partner> partner;
    @XmlAttribute(name = "total_record_count")
    protected Long totalRecordCount;

    /**
     * Gets the value of the partner property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the partner property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPartner().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Partner }
     * 
     * 
     */
    public List<Partner> getPartner() {
        if (partner == null) {
            partner = new ArrayList<Partner>();
        }
        return this.partner;
    }

    /**
     * Gets the value of the totalRecordCount property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTotalRecordCount() {
        return totalRecordCount;
    }

    /**
     * Sets the value of the totalRecordCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTotalRecordCount(Long value) {
        this.totalRecordCount = value;
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
            List<Partner> thePartner;
            thePartner = (((this.partner!= null)&&(!this.partner.isEmpty()))?this.getPartner():null);
            strategy.appendField(locator, this, "partner", buffer, thePartner);
        }
        {
            Long theTotalRecordCount;
            theTotalRecordCount = this.getTotalRecordCount();
            strategy.appendField(locator, this, "totalRecordCount", buffer, theTotalRecordCount);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Partners)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Partners that = ((Partners) object);
        {
            List<Partner> lhsPartner;
            lhsPartner = (((this.partner!= null)&&(!this.partner.isEmpty()))?this.getPartner():null);
            List<Partner> rhsPartner;
            rhsPartner = (((that.partner!= null)&&(!that.partner.isEmpty()))?that.getPartner():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "partner", lhsPartner), LocatorUtils.property(thatLocator, "partner", rhsPartner), lhsPartner, rhsPartner)) {
                return false;
            }
        }
        {
            Long lhsTotalRecordCount;
            lhsTotalRecordCount = this.getTotalRecordCount();
            Long rhsTotalRecordCount;
            rhsTotalRecordCount = that.getTotalRecordCount();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "totalRecordCount", lhsTotalRecordCount), LocatorUtils.property(thatLocator, "totalRecordCount", rhsTotalRecordCount), lhsTotalRecordCount, rhsTotalRecordCount)) {
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
            List<Partner> thePartner;
            thePartner = (((this.partner!= null)&&(!this.partner.isEmpty()))?this.getPartner():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "partner", thePartner), currentHashCode, thePartner);
        }
        {
            Long theTotalRecordCount;
            theTotalRecordCount = this.getTotalRecordCount();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "totalRecordCount", theTotalRecordCount), currentHashCode, theTotalRecordCount);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}