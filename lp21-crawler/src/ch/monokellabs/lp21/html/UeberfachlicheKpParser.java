package ch.monokellabs.lp21.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenzstufe;

public class UeberfachlicheKpParser
{
	public static List<Kompetenz> parse(String html)
	{
		Document document = Jsoup.parse(html);
		if (!document.getElementsByTag("h1").text().equals("Überfachliche Kompetenzen"))
		{
			return Collections.emptyList();
		}

		List<Kompetenz> kps = new ArrayList<>();
		String bereich = null;
		for(Element absatz : document.select(".ek_absatz"))
		{
			Elements h2 = absatz.getElementsByTag("h2");
			if (!h2.isEmpty())
			{
				if (h2.text().equals("Einleitung"))
				{
					continue;
				}
				bereich = StringUtils.substringBefore(h2.text(), " (");
				continue;
			}
			if (bereich != null)
			{
				kps.add(parseKp(bereich, absatz));
			}
		}
		return kps;
	}

	private static Kompetenz parseKp(String bereich, Element absatz) {
		Kompetenz kp = new Kompetenz();
		kp.fach = "Überfachliche Kompetenzen";
		kp.bereich = bereich;
		kp.bereichCode = BEREICH_TO_CODE.get(kp.bereich);
		
		String h4 = absatz.getElementsByTag("h4").text();
		kp.titel = StringUtils.substringAfter(h4, ": ");
		kp.aspekt = StringUtils.substringBefore(h4, ":");
		
		kp.code = new StringBuilder("UE").append(".").append(kp.bereichCode).toString();
		
		Elements stufen = absatz.getElementsByTag("ul");
		kp.stufen = stufen.get(0).getElementsByTag("li").stream()
			.map(li -> parseStufe(li))
			.collect(Collectors.toList());
		return kp;
	}

	private static Kompetenzstufe parseStufe(Element li) {
		Kompetenzstufe stufe = new Kompetenzstufe();
		stufe.zyklus = 0;
		stufe.text = li.text();
		return stufe;
	}
	
	private static Map<String, String> BEREICH_TO_CODE = new HashMap<>();
	static
	{
		BEREICH_TO_CODE.put("Personale Kompetenzen", "P");
		BEREICH_TO_CODE.put("Soziale Kompetenzen", "S");
		BEREICH_TO_CODE.put("Methodische Kompetenzen", "M");
	}
}