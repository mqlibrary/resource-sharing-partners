//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.27 at 11:50:46 AM AEST 
//


package org.nishen.resourcepartners.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 * The partner's profile details. 
 * 
 * <p>Java class for profile_details complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="profile_details"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="profile_type" type="{}profile_type"/&gt;
 *         &lt;element name="art_email_details" type="{}art_email_details" minOccurs="0"/&gt;
 *         &lt;element name="ncip_details" type="{}ncip_details" minOccurs="0"/&gt;
 *         &lt;element name="iso_details" type="{}iso_details" minOccurs="0"/&gt;
 *         &lt;element name="email_details" type="{}email_details" minOccurs="0"/&gt;
 *         &lt;element name="ncip_p2p_details" type="{}ncip_p2p_details" minOccurs="0"/&gt;
 *         &lt;element name="bldss_details" type="{}bldss_details" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "profile_details", propOrder = {

})
public class ProfileDetails
    implements Equals, HashCode, ToString
{

    @XmlElement(name = "profile_type", required = true)
    @XmlSchemaType(name = "string")
    protected ProfileType profileType;
    @XmlElement(name = "art_email_details")
    protected ArtEmailDetails artEmailDetails;
    @XmlElement(name = "ncip_details")
    protected NcipDetails ncipDetails;
    @XmlElement(name = "iso_details")
    protected IsoDetails isoDetails;
    @XmlElement(name = "email_details")
    protected EmailDetails emailDetails;
    @XmlElement(name = "ncip_p2p_details")
    protected NcipP2PDetails ncipP2PDetails;
    @XmlElement(name = "bldss_details")
    protected BldssDetails bldssDetails;

    /**
     * Gets the value of the profileType property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileType }
     *     
     */
    public ProfileType getProfileType() {
        return profileType;
    }

    /**
     * Sets the value of the profileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileType }
     *     
     */
    public void setProfileType(ProfileType value) {
        this.profileType = value;
    }

    /**
     * Gets the value of the artEmailDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ArtEmailDetails }
     *     
     */
    public ArtEmailDetails getArtEmailDetails() {
        return artEmailDetails;
    }

    /**
     * Sets the value of the artEmailDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArtEmailDetails }
     *     
     */
    public void setArtEmailDetails(ArtEmailDetails value) {
        this.artEmailDetails = value;
    }

    /**
     * Gets the value of the ncipDetails property.
     * 
     * @return
     *     possible object is
     *     {@link NcipDetails }
     *     
     */
    public NcipDetails getNcipDetails() {
        return ncipDetails;
    }

    /**
     * Sets the value of the ncipDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link NcipDetails }
     *     
     */
    public void setNcipDetails(NcipDetails value) {
        this.ncipDetails = value;
    }

    /**
     * Gets the value of the isoDetails property.
     * 
     * @return
     *     possible object is
     *     {@link IsoDetails }
     *     
     */
    public IsoDetails getIsoDetails() {
        return isoDetails;
    }

    /**
     * Sets the value of the isoDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsoDetails }
     *     
     */
    public void setIsoDetails(IsoDetails value) {
        this.isoDetails = value;
    }

    /**
     * Gets the value of the emailDetails property.
     * 
     * @return
     *     possible object is
     *     {@link EmailDetails }
     *     
     */
    public EmailDetails getEmailDetails() {
        return emailDetails;
    }

    /**
     * Sets the value of the emailDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailDetails }
     *     
     */
    public void setEmailDetails(EmailDetails value) {
        this.emailDetails = value;
    }

    /**
     * Gets the value of the ncipP2PDetails property.
     * 
     * @return
     *     possible object is
     *     {@link NcipP2PDetails }
     *     
     */
    public NcipP2PDetails getNcipP2PDetails() {
        return ncipP2PDetails;
    }

    /**
     * Sets the value of the ncipP2PDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link NcipP2PDetails }
     *     
     */
    public void setNcipP2PDetails(NcipP2PDetails value) {
        this.ncipP2PDetails = value;
    }

    /**
     * Gets the value of the bldssDetails property.
     * 
     * @return
     *     possible object is
     *     {@link BldssDetails }
     *     
     */
    public BldssDetails getBldssDetails() {
        return bldssDetails;
    }

    /**
     * Sets the value of the bldssDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link BldssDetails }
     *     
     */
    public void setBldssDetails(BldssDetails value) {
        this.bldssDetails = value;
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
            ProfileType theProfileType;
            theProfileType = this.getProfileType();
            strategy.appendField(locator, this, "profileType", buffer, theProfileType);
        }
        {
            ArtEmailDetails theArtEmailDetails;
            theArtEmailDetails = this.getArtEmailDetails();
            strategy.appendField(locator, this, "artEmailDetails", buffer, theArtEmailDetails);
        }
        {
            NcipDetails theNcipDetails;
            theNcipDetails = this.getNcipDetails();
            strategy.appendField(locator, this, "ncipDetails", buffer, theNcipDetails);
        }
        {
            IsoDetails theIsoDetails;
            theIsoDetails = this.getIsoDetails();
            strategy.appendField(locator, this, "isoDetails", buffer, theIsoDetails);
        }
        {
            EmailDetails theEmailDetails;
            theEmailDetails = this.getEmailDetails();
            strategy.appendField(locator, this, "emailDetails", buffer, theEmailDetails);
        }
        {
            NcipP2PDetails theNcipP2PDetails;
            theNcipP2PDetails = this.getNcipP2PDetails();
            strategy.appendField(locator, this, "ncipP2PDetails", buffer, theNcipP2PDetails);
        }
        {
            BldssDetails theBldssDetails;
            theBldssDetails = this.getBldssDetails();
            strategy.appendField(locator, this, "bldssDetails", buffer, theBldssDetails);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof ProfileDetails)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final ProfileDetails that = ((ProfileDetails) object);
        {
            ProfileType lhsProfileType;
            lhsProfileType = this.getProfileType();
            ProfileType rhsProfileType;
            rhsProfileType = that.getProfileType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "profileType", lhsProfileType), LocatorUtils.property(thatLocator, "profileType", rhsProfileType), lhsProfileType, rhsProfileType)) {
                return false;
            }
        }
        {
            ArtEmailDetails lhsArtEmailDetails;
            lhsArtEmailDetails = this.getArtEmailDetails();
            ArtEmailDetails rhsArtEmailDetails;
            rhsArtEmailDetails = that.getArtEmailDetails();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "artEmailDetails", lhsArtEmailDetails), LocatorUtils.property(thatLocator, "artEmailDetails", rhsArtEmailDetails), lhsArtEmailDetails, rhsArtEmailDetails)) {
                return false;
            }
        }
        {
            NcipDetails lhsNcipDetails;
            lhsNcipDetails = this.getNcipDetails();
            NcipDetails rhsNcipDetails;
            rhsNcipDetails = that.getNcipDetails();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "ncipDetails", lhsNcipDetails), LocatorUtils.property(thatLocator, "ncipDetails", rhsNcipDetails), lhsNcipDetails, rhsNcipDetails)) {
                return false;
            }
        }
        {
            IsoDetails lhsIsoDetails;
            lhsIsoDetails = this.getIsoDetails();
            IsoDetails rhsIsoDetails;
            rhsIsoDetails = that.getIsoDetails();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "isoDetails", lhsIsoDetails), LocatorUtils.property(thatLocator, "isoDetails", rhsIsoDetails), lhsIsoDetails, rhsIsoDetails)) {
                return false;
            }
        }
        {
            EmailDetails lhsEmailDetails;
            lhsEmailDetails = this.getEmailDetails();
            EmailDetails rhsEmailDetails;
            rhsEmailDetails = that.getEmailDetails();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "emailDetails", lhsEmailDetails), LocatorUtils.property(thatLocator, "emailDetails", rhsEmailDetails), lhsEmailDetails, rhsEmailDetails)) {
                return false;
            }
        }
        {
            NcipP2PDetails lhsNcipP2PDetails;
            lhsNcipP2PDetails = this.getNcipP2PDetails();
            NcipP2PDetails rhsNcipP2PDetails;
            rhsNcipP2PDetails = that.getNcipP2PDetails();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "ncipP2PDetails", lhsNcipP2PDetails), LocatorUtils.property(thatLocator, "ncipP2PDetails", rhsNcipP2PDetails), lhsNcipP2PDetails, rhsNcipP2PDetails)) {
                return false;
            }
        }
        {
            BldssDetails lhsBldssDetails;
            lhsBldssDetails = this.getBldssDetails();
            BldssDetails rhsBldssDetails;
            rhsBldssDetails = that.getBldssDetails();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "bldssDetails", lhsBldssDetails), LocatorUtils.property(thatLocator, "bldssDetails", rhsBldssDetails), lhsBldssDetails, rhsBldssDetails)) {
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
            ProfileType theProfileType;
            theProfileType = this.getProfileType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "profileType", theProfileType), currentHashCode, theProfileType);
        }
        {
            ArtEmailDetails theArtEmailDetails;
            theArtEmailDetails = this.getArtEmailDetails();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "artEmailDetails", theArtEmailDetails), currentHashCode, theArtEmailDetails);
        }
        {
            NcipDetails theNcipDetails;
            theNcipDetails = this.getNcipDetails();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "ncipDetails", theNcipDetails), currentHashCode, theNcipDetails);
        }
        {
            IsoDetails theIsoDetails;
            theIsoDetails = this.getIsoDetails();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "isoDetails", theIsoDetails), currentHashCode, theIsoDetails);
        }
        {
            EmailDetails theEmailDetails;
            theEmailDetails = this.getEmailDetails();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "emailDetails", theEmailDetails), currentHashCode, theEmailDetails);
        }
        {
            NcipP2PDetails theNcipP2PDetails;
            theNcipP2PDetails = this.getNcipP2PDetails();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "ncipP2PDetails", theNcipP2PDetails), currentHashCode, theNcipP2PDetails);
        }
        {
            BldssDetails theBldssDetails;
            theBldssDetails = this.getBldssDetails();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "bldssDetails", theBldssDetails), currentHashCode, theBldssDetails);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}
