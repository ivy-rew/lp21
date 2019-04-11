package ch.monokellabs.lp21.export;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenzstufe;
import ch.monokellabs.lp21.Verweis;

public class CsvWriter {

	public static final String COL_SEPARATOR = ";";
	public static final String ROW_SEPARATOR = System.lineSeparator();
	
	private static final List<String> HEADERS = Arrays.asList(
			"K Code", 
			"Fach", 
			"B Code", "Bereich",
			"A Code", "Aspekt",
			"TNR", "Titel", "TQV",
			"Zyklus",
			"S Code", "Stufe", "SQV"
	);
	
	public static String writeKompetenzen(Collection<Kompetenz> kompetenzen)
	{
		StringBuilder csv = new StringBuilder();
		csv.append(getHeaderRow());
		kompetenzen.stream()
			.forEach(kompetenz -> csv.append(writeLines(kompetenz)));
		return csv.toString();
	}

	private static String getHeaderRow() {
		return HEADERS.stream().collect(
				Collectors.joining(COL_SEPARATOR, "", ROW_SEPARATOR));
	}
	
	public static String writeLines(Kompetenz komp)
	{
		StringBuilder line = new StringBuilder();
		for(Kompetenzstufe stufe : komp.stufen)
		{
			line.append(escape(komp.code)).append(COL_SEPARATOR);
			line.append(escape(komp.fach)).append(COL_SEPARATOR);
			line.append(escape(komp.bereichCode)).append(COL_SEPARATOR);
			line.append(escape(komp.bereich)).append(COL_SEPARATOR);
			line.append(escape(komp.aspektCode)).append(COL_SEPARATOR);
			line.append(escape(komp.aspekt)).append(COL_SEPARATOR);
			line.append(escape(komp.titelNr)).append(COL_SEPARATOR);
			line.append(escape(komp.titel)).append(COL_SEPARATOR);
			line.append(verweiseCell(komp.verweise)).append(COL_SEPARATOR);
			
			line.append(stufe.zyklus).append(COL_SEPARATOR);
			line.append(escape(stufe.code)).append(COL_SEPARATOR);
			line.append(singleLine(escape(stufe.text))).append(COL_SEPARATOR);
			line.append(verweiseCell(stufe.verweise)).append(ROW_SEPARATOR);
		}
		return line.toString();
	}
	
	private static String verweiseCell(List<Verweis> verweise)
	{
		return verweise.stream()
		.map(verweis -> verweis.code)
		.collect(Collectors.joining(", "));
	}
	
	private static String singleLine(String titel) 
	{ // normalize multi lined content to single
		return StringUtils.replace(titel, "\n", " || ");
	}

	private static String escape(String colContent)
	{ // ensure that no accidental column switches will be added to the CSV.
		return StringUtils.replace(colContent, COL_SEPARATOR, ":");
	}
	
}
