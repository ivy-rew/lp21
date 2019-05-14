package ch.monokellabs.lp21.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.monokellabs.lp21.Kompetenz;

public class LpParser {

	public boolean parseUeberfachlich = true;
	
	public List<Kompetenz> parse(List<String> htmlPages) {
		List<String> failed = new ArrayList<>();
		List<Kompetenz> kompetenzen = htmlPages.stream()
			.map(html -> {
				Kompetenz kp = Kompetenz.parse(html);
				if (kp == null)
				{
					failed.add(html);
				}
				return kp;
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		if (parseUeberfachlich)
		{
			for(String fail : failed)
			{
				kompetenzen.addAll(UeberfachlicheKpParser.parse(fail));
			}
		}
		return kompetenzen;
	}
	
}
