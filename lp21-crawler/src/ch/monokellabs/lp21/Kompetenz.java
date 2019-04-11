package ch.monokellabs.lp21;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ch.monokellabs.lp21.html.KpHtmLParser;

public class Kompetenz {

	/** Fach bzw fachübergreifendes Thema */
	public String fach;

	public String bereichCode;
	public String bereich;

	/** Handlungs oder Themenaspekt*/
	public String aspektCode;
	public String aspekt;
	
	public String titelNr;
	public String titel;
	public List<Verweis> verweise;
	
	public String code;
	
	public List<Kompetenzstufe> stufen = new ArrayList<>();

	public static Kompetenz parse(String html)
	{
		return KpHtmLParser.parse(html);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, 
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
