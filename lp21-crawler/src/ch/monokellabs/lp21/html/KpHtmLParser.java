package ch.monokellabs.lp21.html;

import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ch.monokellabs.lp21.Kompetenz;

public class KpHtmLParser
{
	public static Kompetenz parse(String html)
	{
		Kompetenz kp = new Kompetenz();
		Document document = Jsoup.parse(html);
		
		Elements crumbs = document.select(".active_breadcrumb");
		kp.fach = crumbs.get(0).text();
		
		Elements code = document.select(".kcode");
		if (code.isEmpty())
		{ // not a kp page!
			return null;
		}
		
		kp.bereichCode = code.get(0).text();
		kp.aspektCode = document.select(".htacode").get(0).text();
		Elements areas = document.select(".kompetenzbereich");
		kp.bereich = areas.get(0).text();
		kp.aspekt = areas.get(1).text();
		
		kp.titelNr = Integer.parseInt(document.select(".komptitelnr").get(0).text());
		Elements titels = document.select(".kompetenztitel");
		kp.titel = titels.select("p").get(1).text();
		
		kp.verweise = QVParser.parseHead(document);
		
		Elements codes = document.select(".ganzercode");
		kp.code = codes.select(".tooltip").get(0).text();
		
		Elements rows = document.select(".komp_row");
		kp.stufen = rows.stream()
			.map(row -> StufeParser.parse(row))
			.collect(Collectors.toList());
		
		return kp;
	}
}