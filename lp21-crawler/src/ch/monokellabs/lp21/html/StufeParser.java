package ch.monokellabs.lp21.html;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.monokellabs.lp21.Kompetenzstufe;

public class StufeParser
{
	private static int lastZyklus = -1;
	
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
		
		entry.verweise = QVParser.parseStufe(row);
		
		return entry; 
	}
}