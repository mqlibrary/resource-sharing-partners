package org.nishen.resourcepartners.entity;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "suspension")
@JsonPropertyOrder({ "suspensionAdded", "suspensionStatus", "suspensionStart", "suspensionEnd", "suspensionCode",
                     "suspensionReason" })
public class ResourcePartnerSuspension implements Serializable, Comparable<ResourcePartnerSuspension>
{
	@JsonIgnore
	private static final long serialVersionUID = 7409549749185576638L;

	@JsonIgnore
	public static final String SUSPENDED = "suspended";

	@JsonIgnore
	public static final String NOT_SUSPENDED = "not suspended";

	@JsonIgnore
	public static final String UNKNOWN = "unknown";

	@JsonProperty("suspension_added")
	private String suspensionAdded;

	@JsonProperty("suspension_status")
	private String suspensionStatus;

	@JsonProperty("suspension_start")
	private String suspensionStart;

	@JsonProperty("suspension_end")
	private String suspensionEnd;

	@JsonProperty("suspension_code")
	private String suspensionCode;

	@JsonProperty("suspension_reason")
	private String suspensionReason;

	public String getSuspensionAdded()
	{
		return suspensionAdded;
	}

	public void setSuspensionAdded(String suspensionAdded)
	{
		this.suspensionAdded = suspensionAdded;
	}

	public String getSuspensionStatus()
	{
		return suspensionStatus;
	}

	public void setSuspensionStatus(String suspensionStatus)
	{
		this.suspensionStatus = suspensionStatus;
	}

	public String getSuspensionStart()
	{
		return suspensionStart;
	}

	public void setSuspensionStart(String suspensionStart)
	{
		this.suspensionStart = suspensionStart;
	}

	public String getSuspensionEnd()
	{
		return suspensionEnd;
	}

	public void setSuspensionEnd(String suspensionEnd)
	{
		this.suspensionEnd = suspensionEnd;
	}

	public String getSuspensionCode()
	{
		return suspensionCode;
	}

	public void setSuspensionCode(String suspensionCode)
	{
		this.suspensionCode = suspensionCode;
	}

	public String getSuspensionReason()
	{
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason)
	{
		this.suspensionReason = suspensionReason;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(suspensionCode, suspensionEnd, suspensionReason, suspensionStart, suspensionStatus);
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
		ResourcePartnerSuspension other = (ResourcePartnerSuspension) obj;
		return Objects.equals(suspensionCode, other.suspensionCode) &&
		       Objects.equals(suspensionEnd, other.suspensionEnd) &&
		       Objects.equals(suspensionReason, other.suspensionReason) &&
		       Objects.equals(suspensionStart, other.suspensionStart) &&
		       Objects.equals(suspensionStatus, other.suspensionStatus);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ResourcePartnerSuspension [suspensionAdded=");
		builder.append(suspensionAdded);
		builder.append(", suspensionStatus=");
		builder.append(suspensionStatus);
		builder.append(", suspensionStart=");
		builder.append(suspensionStart);
		builder.append(", suspensionEnd=");
		builder.append(suspensionEnd);
		builder.append(", suspensionCode=");
		builder.append(suspensionCode);
		builder.append(", suspensionReason=");
		builder.append(suspensionReason);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(ResourcePartnerSuspension o)
	{
		if (this.suspensionEnd == null && o.suspensionEnd != null)
			return -1;

		if (this.suspensionEnd != null && o.suspensionEnd == null)
			return 1;

		if (this.suspensionEnd.compareTo(o.suspensionEnd) != 0)
			return this.suspensionEnd.compareTo(o.suspensionEnd);

		if (this.suspensionStart == null && o.suspensionStart != null)
			return -1;

		if (this.suspensionStart != null && o.suspensionStart == null)
			return 1;

		if (this.suspensionStart.compareTo(o.suspensionStart) != 0)
			return this.suspensionStart.compareTo(o.suspensionStart);

		if (SUSPENDED.equals(this.suspensionStatus) && !SUSPENDED.equals(o.suspensionStatus))
			return 1;

		if (!SUSPENDED.equals(this.suspensionStatus) && SUSPENDED.equals(o.suspensionStatus))
			return -1;

		return 0;
	}
}
