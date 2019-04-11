package ch.monokellabs.lp21;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Verweis
{
	public final String code;
	public final String titel;
	
	public Verweis(String code, String titel)
	{
		this.code = code;
		this.titel = titel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
		{
			return false;
		}
		if (this == obj)
		{
			return true;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		Verweis other = (Verweis) obj;
		return new EqualsBuilder()
				.append(this.titel, other.titel)
				.append(this.code, other.code)
				.isEquals();
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, 
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}