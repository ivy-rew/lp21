package ch.monokellabs.lp21.load;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class LehrplanUri
{
	public static URI createLpUri(String code) throws URISyntaxException
	{
		return new URIBuilder("https://lu.lehrplan.ch/index.php")
				.addParameter("code", code)
				.build();
	}
	
	public static URI parseNext(String kpHtml) throws URISyntaxException
	{
		Elements nexts = Jsoup.parse(kpHtml).select("a[original-title*=Zur nachfolgenden]");
		if (nexts.isEmpty())
		{
			return null;
		}
		String nextUri = nexts.get(0).attr("href");
		if (nextUri.startsWith("index.php?code="))
		{
			String code = StringUtils.substringAfter(nextUri, "?code=");
			return createLpUri(code);
		}
		return null;
	}
	
	public static List<URI> getStarts() throws URISyntaxException
	{
		Set<Fach> faecher = EnumSet.allOf(Fach.class);
		List<URI> starts = new ArrayList<>();
		for(Fach fach : faecher)
		{
			starts.add(createLpUri(fach.code));
		}
		return starts;
	}
	
	
	public static enum Fach
	{
		DEUTSCH("a|1|11|1|1|1"),
		ENGLISH("a|1|21|1|1|1"),
		FRANZ("a|1|32|1|1|1"),
		ITALIENISCH("a|1|41|1|1|1"),
		
		MATH("a|5|0|1|1|1"),
		NMG("a|6|1|1|0|1"),
		NATUR_TECHNIK("a|6|2|1|0|1"),
		WIRSCHAFT_ARBEIT_HAUSHALT("a|6|3|1|0|1"),
		RAEUME_ZEITEN_GESELLSCHAFT("a|6|4|1|0|1"),
		ETHIK_RELIGION_GEMEINSCHAFT("a|6|5|1|0|1"),
		BERUFLICHE_ORIENTIERUNG("a|6|7|1|0|1"),
		
		BILDNERISCHES_GESTALTEN("a|7|1|1|1|1"),
		TEXTILES_TECHNISCHES_GESTALTEN("a|7|2|1|1|1"),
		MUSIK("a|8|0|1|1|1"),
		BEWEGUNG_SPORT("a|9|0|1|1|1"),
		
		MI("a|10|0|1|0|1"), 
		PROJEKT_VPU("a|11|1|1|1|1"),
		PROJEKT_PU("a|11|2|1|1|1"), 
		
		UEBERFACHLICHE("e|200|3"),
		ENTWICKLUNGSORIENTIERTE_ZUGAENGE("e|200|5");
		
		public final String code;
		
		private Fach(String code)
		{
			this.code = code;
		}
	}
}