package ch.monokellabs.lp21;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		List<String> codes = Arrays.asList(FachStart.DEUTSCH, FachStart.MATH, FachStart.NMG, FachStart.GESTALTEN, 
				FachStart.MUSIK, FachStart.SPORT, FachStart.MI);
		List<URI> starts = new ArrayList<>();
		for(String code : codes)
		{
			starts.add(createLpUri(code));
		}
		return starts;
	}
	
	static interface FachStart
	{
		String DEUTSCH = "a|1|11|1|1|1";
		String MATH = "a|5|0|1|1|1";
		String NMG = "a|6|1|1|0|1";
		String GESTALTEN = "a|7|1|1|1|1";
		String MUSIK = "a|8|0|1|1|1";
		String SPORT = "a|9|0|1|1|1";
		String MI = "a|10|0|1|0|1";
	}
}