package ch.monokellabs.lp21;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Kompetenzstufe
{
	public int zyklus;
	public String code;
	public String text;
	public List<Verweis> verweise;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, 
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}