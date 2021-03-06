package ch.monokellabs.lp21;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Kompetenzstufe
{
	public int zyklus;
	public String code;
	public String text;
	public boolean grundanspruch;
	public List<Verweis> verweise = Collections.emptyList();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, 
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}