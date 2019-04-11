package ch.monokellabs.lp21;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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


	
	public static class Kompetenzstufe
	{
		private static int lastZyklus = -1;
		
		public int zyklus;
		public String code;
		public String text;
		public List<Verweis> verweise;

		public static Kompetenzstufe parse(Element row) {
			Kompetenzstufe entry = new Kompetenzstufe();
			
			String zyCodeRaw = row.select(".zycode").get(0).text();
			if (!StringUtils.isEmpty(zyCodeRaw))
			{
				entry.zyklus = Integer.parseInt(zyCodeRaw);
				lastZyklus = entry.zyklus;
			}
			else
			{
				entry.zyklus = lastZyklus;
			}
			
			Elements codes = row.select(".kompetenz_lit");
			entry.code = codes.get(0).attributes().get("title");
			
			Elements texts = row.select(".kompetenz_text");
			Elements list = texts.get(0).select("li");
			entry.text = list.stream()
					.map(li -> li.text())
					.collect(Collectors.joining("\n"));
			
			entry.verweise = Verweis.parse(row);
			
			return entry;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, 
					ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	public static class Verweis
	{
		public final String code;
		public final String titel;
		
		public Verweis(String code, String titel)
		{
			this.code = code;
			this.titel = titel;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
			{
				return false;
			}
			if (this == obj)
			{
				return true;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Verweis other = (Verweis) obj;
			return new EqualsBuilder()
					.append(this.titel, other.titel)
					.append(this.code, other.code)
					.isEquals();
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, 
					ToStringStyle.SHORT_PREFIX_STYLE);
		}
		
		public static List<Verweis> parseHead(Element doc)
		{
			Elements qvs = doc.select(".querverweis_einleitung");
			if (qvs.isEmpty())
			{
				return Collections.emptyList();
			}
			return parseLinks(qvs);
		}
		
		public static List<Verweis> parse(Element part)
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
	
	public static Kompetenz parse(String html)
	{
		Kompetenz kp = new Kompetenz();
		Document document = Jsoup.parse(html);
		
		Elements crumbs = document.select(".active_breadcrumb");
		kp.fach = crumbs.get(0).text();
		
		kp.bereichCode = document.select(".kcode").get(0).text();
		kp.aspektCode = document.select(".htacode").get(0).text();
		Elements areas = document.select(".kompetenzbereich");
		kp.bereich = areas.get(0).text();
		kp.aspekt = areas.get(1).text();
		
		kp.titelNr = document.select(".komptitelnr").get(0).text();
		Elements titels = document.select(".kompetenztitel");
		kp.titel = titels.select("p").get(1).text();
		
		kp.verweise = Verweis.parseHead(document);
		
		Elements codes = document.select(".ganzercode");
		kp.code = codes.select(".tooltip").get(0).text();
		
		Elements rows = document.select(".komp_row");
		kp.stufen = rows.stream()
			.map(row -> Kompetenzstufe.parse(row))
			.collect(Collectors.toList());
		
		return kp;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, 
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
