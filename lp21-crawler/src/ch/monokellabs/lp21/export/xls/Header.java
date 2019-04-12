package ch.monokellabs.lp21.export.xls;

import java.util.Arrays;
import java.util.List;

public interface Header
{
	String kCode = "K Code";
	String fach = "Fach";
	String bCode = "B Code";
	String bereich = "Bereich";
	String aCode = "A Code";
	String aspekt = "Aspekt";
	
	String titelNo = "TNR";
	String titel = "Titel";
	String titelQvw = "TQV";
	
	String zyklus = "Zyklus";
	String sCode = "S Code";
	String stufe = "Stufe";
	String stufeQvw = "SQV";
	
	static int indexOf(String header)
	{
		return ALL.indexOf(header);
	}
	
	List<String> ALL = Arrays.asList(kCode, fach, bCode, bereich, aCode, aspekt, 
			titelNo, titel, titelQvw, 
			zyklus, sCode, stufe, stufeQvw);
}