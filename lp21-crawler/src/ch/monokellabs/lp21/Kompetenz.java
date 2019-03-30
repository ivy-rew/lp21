package ch.monokellabs.lp21;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Kompetenz {

	public String fach;
	public String titel;
	public String code;
	public List<KpEntry> entries = new ArrayList<>();
	
	public static class KpEntry
	{
		private static int lastZyklus = -1;
		
		public int zyklus;
		public String code;
		public String text;

		public static KpEntry parse(Element row) {
			KpEntry entry = new KpEntry();
			
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
			entry.text = texts.get(0).text();
			
			return entry;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, 
					ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	public static Kompetenz parse(String html)
	{
		Kompetenz kp = new Kompetenz();
		Document document = Jsoup.parse(html);
		
		Elements crumbs = document.select(".active_breadcrumb");
		kp.fach = crumbs.get(0).text();
		
		Elements titels = document.select(".kompetenztitel");
		kp.titel = titels.select("p").get(1).text();
		
		Elements codes = document.select(".ganzercode");
		kp.code = codes.select(".tooltip").get(0).text();
		
		Elements rows = document.select(".komp_row");
		kp.entries = rows.stream()
			.map(row -> KpEntry.parse(row))
			.collect(Collectors.toList());
		
		return kp;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, 
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
