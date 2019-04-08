package ch.monokellabs.lp21;

import java.util.Collection;

import ch.monokellabs.lp21.Kompetenz.KpEntry;

public class CsvWriter {

	public static final String COL_SEPARATOR = ";";
	public static final String ROW_SEPARATOR = System.lineSeparator();
	
	public static String writeKompetenzen(Collection<Kompetenz> kompetenzen)
	{
		StringBuilder csv = new StringBuilder();
		addColumnHeaders(csv);
		kompetenzen.stream().forEach(kompetenz -> csv.append(writeLines(kompetenz)));
		return csv.toString();
	}

	private static void addColumnHeaders(StringBuilder csv) {
		csv
			.append("FACH").append(COL_SEPARATOR)
			.append("CODE").append(COL_SEPARATOR)
			.append("TITEL").append(COL_SEPARATOR)
			.append("FULL_CODE").append(COL_SEPARATOR)
			.append("ZYKLUS").append(COL_SEPARATOR)
			.append("TEXT").append(ROW_SEPARATOR);
	}
	
	public static String writeLines(Kompetenz komp)
	{
		StringBuilder line = new StringBuilder();
		
		for(KpEntry entry : komp.entries)
		{
			line.append(komp.fach).append(COL_SEPARATOR);
			line.append(komp.code).append(COL_SEPARATOR);
			line.append(komp.titel).append(COL_SEPARATOR);

			line.append(entry.code).append(COL_SEPARATOR);
			line.append(entry.zyklus).append(COL_SEPARATOR);
			line.append(entry.text).append(ROW_SEPARATOR);
		}
		
		return line.toString();
	}
	
}
