package ch.monokellabs.lp21.html;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.monokellabs.lp21.Verweis;

public class QVParser
{
	public static List<Verweis> parseHead(Element doc)
	{
		Elements qvs = doc.select(".querverweis_einleitung");
		if (qvs.isEmpty())
		{
			return Collections.emptyList();
		}
		return parseLinks(qvs);
	}
	
	public static List<Verweis> parseStufe(Element part)
	{
		Elements querv = part.select(".querv");
		if (querv.isEmpty())
		{
			return Collections.emptyList();
		}
		return parseLinks(querv); 
	}

	private static List<Verweis> parseLinks(Elements querv) {
		return querv.select("a").stream()
			.map(a -> {
				return new Verweis(a.text(), a.attr("title"));
			})
			.collect(Collectors.toList());
	}
}