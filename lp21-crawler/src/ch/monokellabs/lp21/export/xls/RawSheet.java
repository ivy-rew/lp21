package ch.monokellabs.lp21.export.xls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenzstufe;
import ch.monokellabs.lp21.Verweis;
import ch.monokellabs.lp21.export.xls.meta.PrintOptions;

/**
 * Technical output with de-normalized data
 */
public class RawSheet
{
	private final XSSFSheet sheet;

	public boolean genderized = false;
	
	public RawSheet(XSSFSheet sheet) {
		this.sheet = sheet;
		new PrintOptions(sheet).optimize();
	}
	
	public void fill(Collection<Kompetenz> kompetenzen)
	{
		KpHeader header = new KpHeader(sheet);
		header.writeColTitle(0);
		
        int rowNum = 2;
        for(Kompetenz komp : kompetenzen)
        {
        	rowNum = writeKompetenz(komp, sheet, rowNum);
        }
        
        header.addAutoFilter(1);
        header.freezeHeaderAndFilter();
	}

	private int writeKompetenz(Kompetenz komp, XSSFSheet sheet, int rowNum) {
		for(Kompetenzstufe stufe : komp.stufen)
		{
			Row row = new Row(sheet.createRow(rowNum++));
			row.add(komp.code);
			row.add(komp.fach);
			row.add(komp.bereichCode);
			row.add(komp.bereich);
			row.add(komp.aspektCode);
			row.add(komp.aspekt);
			
			row.addCell().setCellValue((komp.titelNr));
			row.add(komp.titel.get(genderized));
			row.add(verweiseCell(komp.verweise));
			
			row.addCell().setCellValue(stufe.zyklus);
			row.add(stufe.code);
			row.add(stufe.text);
			row.add(verweiseCell(stufe.verweise));
		}
		return rowNum;
	}
	
	private static String verweiseCell(List<Verweis> verweise)
	{
		return verweise.stream()
		.map(verweis -> verweis.code)
		.collect(Collectors.joining(", "));
	}
	
}