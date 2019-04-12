package ch.monokellabs.lp21.export.xls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenzstufe;
import ch.monokellabs.lp21.Verweis;

/**
 * Technical output with de-normalized data
 */
public class RawSheet
{
	private final XSSFSheet sheet;

	public RawSheet(XSSFSheet sheet) {
		this.sheet = sheet;
	}
	
	public void fill(Collection<Kompetenz> kompetenzen)
	{
		writeHeaderRow(sheet.createRow(0));
        
        int rowNum = 2;
        for(Kompetenz komp : kompetenzen)
        {
        	rowNum = writeKompetenz(komp, sheet, rowNum);
        }
	}
	
	private static final List<String> HEADERS = Header.ALL;
	
	static void writeHeaderRow(XSSFRow row) {
		for(int col=0; col<HEADERS.size(); col++)
		{
			String colHeader = HEADERS.get(col);
			XSSFCell cell = row.createCell(col);
			XSSFRichTextString formattedValue = new XSSFRichTextString(colHeader);
			formattedValue.applyFont(XlsWriter.HEADER_FONT);
			cell.setCellValue(formattedValue);
		}
	}
	
	private static int writeKompetenz(Kompetenz komp, XSSFSheet sheet, int rowNum) {
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
			row.add(komp.titel);
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