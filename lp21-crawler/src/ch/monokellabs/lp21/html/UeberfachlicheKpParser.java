package ch.monokellabs.lp21.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
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
	private static Map<String, Character> ASPECT_TO_CODE;
	private static char firstAspectCode = 'A';
	
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
				ASPECT_TO_CODE = new LinkedHashMap<>(); // reset aspect; start over with A
				continue;
			}
			if (bereich != null)
			{
				kps.add(parseKp(bereich, absatz, kps.size()+1));
			}
		}
		return kps;
	}

	private static Kompetenz parseKp(String bereich, Element absatz, int tNr) {
		Kompetenz kp = new Kompetenz();
		kp.fach = "Überfachliche Kompetenzen";
		kp.bereich = bereich;
		kp.bereichCode = BEREICH_TO_CODE.get(kp.bereich);
		
		String h4 = absatz.getElementsByTag("h4").text();
		kp.titelNr = tNr;
		kp.titel = StringUtils.substringAfter(h4, ": ");
		kp.aspekt = StringUtils.substringBefore(h4, ":");
		Character acode = ASPECT_TO_CODE.computeIfAbsent(kp.aspekt, aspekt -> {
			if (ASPECT_TO_CODE.isEmpty())
			{
				return Character.valueOf(firstAspectCode);
			}
			LinkedList<String> keys = new LinkedList<>(ASPECT_TO_CODE.keySet());
			Character lastChar = ASPECT_TO_CODE.get(keys.getLast());
			char next = nextChar(lastChar);
			return Character.valueOf(next);
		});
		kp.aspektCode = acode.toString();
		
		kp.code = new StringBuilder("UE")
					.append(".").append(kp.bereichCode)
					.append(".").append(kp.aspektCode)
					.append(".").append(kp.titelNr)
					.toString();
		
		Elements stufen = absatz.getElementsByTag("ul");
		AtomicReference<Character> stufeCodeSuffix = new AtomicReference<>('a');
		kp.stufen = stufen.get(0).getElementsByTag("li").stream()
			.map(li -> parseStufe(li))
			.map(st -> {
				st.code = kp.code + "." + stufeCodeSuffix.get();
				stufeCodeSuffix.set(nextChar(stufeCodeSuffix.get()));
				return st;
			})
			.collect(Collectors.toList());
		return kp;
	}

	private static char nextChar(Character lastChar) {
		return (char)(((int)lastChar.charValue()) + 1);
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