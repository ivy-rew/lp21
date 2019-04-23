package ch.monokellabs.lp21;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * subtracts genderized prefixes for enhanced readability and 
 * to give dignity to the indiviudal.
 */
public class Genderized
{
	public static String GENDER_PREFIX = "Die Schülerinnen und Schüler";
	public static String SHORT_GENDER_PREFIX = "Schülerinnen und Schüler";
	
	/** Mehrzahl zu Einzahl */
	public static Map<String, String> VERBS = new HashMap<>();
	static{
		VERBS.put("können", "kann");
		VERBS.put("erfahren, erkennen und reflektieren", "erfährt, erkennt und reflektiert");
		VERBS.put("verstehen", "versteht");
		VERBS.put("kennen", "kennt");
		VERBS.put("begegnen", "begegnet");
		VERBS.put("experimentieren", "experimentiert");
		VERBS.put("verfügen", "verfügt");
	}
			
	private final String kpTitel;

	public Genderized(String kpTitel)
	{
		this.kpTitel = kpTitel;
	}
	
	public String get(boolean genderized)
	{
		if (genderized)
		{
			return getValue();
		}
		return indiviudalized();
	}
	
	public String getValue()
	{
		return this.kpTitel;
	}
	
	public boolean isGenderized()
	{
		if (StringUtils.startsWith(kpTitel, GENDER_PREFIX))
		{
			return true;
		}
		if (StringUtils.startsWith(kpTitel, SHORT_GENDER_PREFIX))
		{ // PU
			return true;
		}
		return false;
	}
	
	public String indiviudalized()
	{
		if (isGenderized())
		{
			String multi = withoutGenderPrefix();
			String verbMulti = StringUtils.substringBefore(multi, " ");
			String verbSingle = VERBS.get(verbMulti);
			if (verbSingle == null)
			{ 
				for(Entry<String, String> nTo1 : VERBS.entrySet())
				{
					if (StringUtils.startsWith(multi, nTo1.getKey()))
					{
						verbMulti = nTo1.getKey();
						verbSingle = nTo1.getValue();
						break;
					}
				}
			}
			if (verbSingle == null)
			{
				return multi;
			}
			String verbless = StringUtils.substringAfter(kpTitel, verbMulti);
			return verbSingle + verbless;
		}
		return kpTitel;
	}
	
	public String withoutGenderPrefix()
	{
		if (StringUtils.startsWith(kpTitel, GENDER_PREFIX))
		{
			return cutPrefix(kpTitel, GENDER_PREFIX);
		}
		if (StringUtils.startsWith(kpTitel, SHORT_GENDER_PREFIX))
		{ // PU
			return cutPrefix(kpTitel, SHORT_GENDER_PREFIX);
		}
		return kpTitel;
	}
	
	static String cutPrefix(String titel, String prefix)
	{
		int begin = prefix.length();
		if (titel.charAt(begin) == ' ')
		{
			begin+= 1;
		}
		String shortened = titel.substring(begin, titel.length());
		return shortened;
	}
	
}