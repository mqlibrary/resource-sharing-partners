package org.nishen.resourcepartners.entity;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "address")
@JsonPropertyOrder({ "addressStatus", "addressType", "line1", "line2", "line3", "line4", "line5", "city",
                     "stateProvince", "postalCode", "country", "addressNote", "startDate", "endDate", "preferred" })
public class ResourcePartnerAddress implements Serializable
{
	@JsonIgnore
	private static final long serialVersionUID = 5441686509217046883L;

	@JsonProperty("address_status")
	private String addressStatus;

	@JsonProperty("address_type")
	private String addressType;

	@JsonProperty("line1")
	private String line1;

	@JsonProperty("line2")
	private String line2;

	@JsonProperty("line3")
	private String line3;

	@JsonProperty("line4")
	private String line4;

	@JsonProperty("line5")
	private String line5;

	@JsonProperty("city")
	private String city;

	@JsonProperty("state_province")
	private String stateProvince;

	@JsonProperty("postal_code")
	private String postalCode;

	@JsonProperty("country")
	private String country;

	@JsonProperty("address_note")
	private String addressNote;

	@JsonProperty("start_date")
	private String startDate;

	@JsonProperty("end_date")
	private String endDate;

	@JsonProperty("preferred")
	private Boolean preferred;

	public String getAddressStatus()
	{
		return addressStatus;
	}

	public void setAddressStatus(String addressStatus)
	{
		this.addressStatus = addressStatus;
	}

	public String getAddressType()
	{
		return addressType;
	}

	public void setAddressType(String addressType)
	{
		this.addressType = addressType;
	}

	public String getLine1()
	{
		return line1;
	}

	public void setLine1(String line1)
	{
		this.line1 = line1;
	}

	public String getLine2()
	{
		return line2;
	}

	public void setLine2(String line2)
	{
		this.line2 = line2;
	}

	public String getLine3()
	{
		return line3;
	}

	public void setLine3(String line3)
	{
		this.line3 = line3;
	}

	public String getLine4()
	{
		return line4;
	}

	public void setLine4(String line4)
	{
		this.line4 = line4;
	}

	public String getLine5()
	{
		return line5;
	}

	public void setLine5(String line5)
	{
		this.line5 = line5;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getStateProvince()
	{
		return stateProvince;
	}

	public void setStateProvince(String stateProvince)
	{
		this.stateProvince = stateProvince;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getAddressNote()
	{
		return addressNote;
	}

	public void setAddressNote(String addressNote)
	{
		this.addressNote = addressNote;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public Boolean getPreferred()
	{
		return preferred;
	}

	public void setPreferred(Boolean preferred)
	{
		this.preferred = preferred;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(addressNote, addressStatus, addressType, city, country, endDate, line1, line2, line3, line4,
		                    line5, postalCode, preferred, startDate, stateProvince);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourcePartnerAddress other = (ResourcePartnerAddress) obj;
		return Objects.equals(addressNote, other.addressNote) && Objects.equals(addressStatus, other.addressStatus) &&
		       Objects.equals(addressType, other.addressType) && Objects.equals(city, other.city) &&
		       Objects.equals(country, other.country) && Objects.equals(endDate, other.endDate) &&
		       Objects.equals(line1, other.line1) && Objects.equals(line2, other.line2) &&
		       Objects.equals(line3, other.line3) && Objects.equals(line4, other.line4) &&
		       Objects.equals(line5, other.line5) && Objects.equals(postalCode, other.postalCode) &&
		       Objects.equals(preferred, other.preferred) && Objects.equals(startDate, other.startDate) &&
		       Objects.equals(stateProvince, other.stateProvince);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ResourcePartnerAddress [addressStatus=");
		builder.append(addressStatus);
		builder.append(", addressType=");
		builder.append(addressType);
		builder.append(", line1=");
		builder.append(line1);
		builder.append(", line2=");
		builder.append(line2);
		builder.append(", line3=");
		builder.append(line3);
		builder.append(", line4=");
		builder.append(line4);
		builder.append(", line5=");
		builder.append(line5);
		builder.append(", city=");
		builder.append(city);
		builder.append(", stateProvince=");
		builder.append(stateProvince);
		builder.append(", postalCode=");
		builder.append(postalCode);
		builder.append(", country=");
		builder.append(country);
		builder.append(", addressNote=");
		builder.append(addressNote);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", preferred=");
		builder.append(preferred);
		builder.append("]");
		return builder.toString();
	}
}
