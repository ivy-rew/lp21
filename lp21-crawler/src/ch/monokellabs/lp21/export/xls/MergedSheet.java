package ch.monokellabs.lp21.export.xls;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenzstufe;
import ch.monokellabs.lp21.Verweis;

/**
 * Technical output with de-normalized data
 */
public class MergedSheet
{
	private final XSSFSheet sheet;
	private XSSFCellStyle top;

	public MergedSheet(XSSFSheet sheet) {
		this.sheet = sheet;
		
		top = this.sheet.getWorkbook().createCellStyle();
		top.setVerticalAlignment(VerticalAlignment.TOP);
	}
	
	public void fill(Collection<Kompetenz> kompetenzen)
	{
		writeHeaderRow(sheet.createRow(0));
        
        int rowNum = 2;
        for(Kompetenz komp : kompetenzen)
        {
        	rowNum = writeKompetenz(komp, sheet, rowNum);
        }
        addAutoFilter();
	}

	private void addAutoFilter() {
		sheet.setAutoFilter(new CellRangeAddress(1, sheet.getLastRowNum(), 0, HEADERS.size()));
	}
	
	private static final List<String> HEADERS = Arrays.asList(
			"K Code", 
			"Fach", 
			"B Code", "Bereich",
			"A Code", "Aspekt",
			"TNR", "Titel", "TQV",
			"Zyklus",
			"S Code", "Stufe", "SQV"
	);
	
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
	
	private int writeKompetenz(Kompetenz komp, XSSFSheet sheet, int rowNum) 
	{
		for(int i=0; i<9; i++)
		{	// 9 common Kompetenz prefixes for Stufe
			sheet.addMergedRegion(range(komp, rowNum, i));
		}
		for(Kompetenzstufe stufe : komp.stufen)
		{
			Row row = new Row(sheet.createRow(rowNum++));
			row.add(komp.code).setCellStyle(top);
			row.add(komp.fach).setCellStyle(top);
			row.add(komp.bereichCode).setCellStyle(top);
			row.add(komp.bereich).setCellStyle(top);
			row.add(komp.aspektCode).setCellStyle(top);
			row.add(komp.aspekt).setCellStyle(top);
			
			XSSFCell tNrCell = row.addCell();
			tNrCell.setCellValue((komp.titelNr));
			tNrCell.setCellStyle(top);
			
			row.add(komp.titel).setCellStyle(top);
			row.add(verweiseCell(komp.verweise)).setCellStyle(top);
			
			row.addCell().setCellValue(stufe.zyklus);
			row.add(stufe.code);
			row.add(stufe.text);
			row.add(verweiseCell(stufe.verweise));
		}
		return rowNum;
	}

	private CellRangeAddress range(Kompetenz komp, int row, int col) {
		return new CellRangeAddress(row, row+komp.stufen.size()-1, col, col);
	}
	
	private static String verweiseCell(List<Verweis> verweise)
	{
		return verweise.stream()
		.map(verweis -> verweis.code)
		.collect(Collectors.joining(", "));
	}
	
}