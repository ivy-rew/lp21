package ch.monokellabs.lp21.load;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class LehrplanUri
{
	public final String baseUri;

	public LehrplanUri()
	{
		this("lu");
	}
	
	public LehrplanUri(String kanton)
	{
		this.baseUri = "https://"+kanton+".lehrplan.ch/index.php";
	}
	
	public URI createLpUri(String code) throws URISyntaxException
	{
		return new URIBuilder(baseUri)
				.addParameter("code", code)
				.build();
	}
	
	public URI parseNext(String kpHtml) throws URISyntaxException
	{
		String nextCode = parseNextCode(kpHtml);
		if (nextCode != null)
		{
			return createLpUri(nextCode);
		}
		return null;
	}
	
	private static String parseNextCode(String kpHtml)
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
			return code;
		}
		return null;
	}
	
	public List<URI> getStarts(Properties kantonalerLp)
	{
		return kantonalerLp.values().stream()
			.map(obj -> (String)obj)
			.map(code -> {
				try {
					return createLpUri(code);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
}