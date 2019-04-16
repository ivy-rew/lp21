package ch.monokellabs.lp21.export.xls;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenzstufe;
import ch.monokellabs.lp21.Verweis;
import ch.monokellabs.lp21.export.xls.meta.PrintOptions;

/**
 * Technical output with de-normalized data
 */
public class MergedSheet
{
	private static final int KP_ROW_START = 2;
	private static final int STUFE_COL_START = 9;

	private final XSSFSheet sheet;
	private final Style style;
	
	public MergedSheet(XSSFSheet sheet) {
		this.sheet = sheet;
		this.style = new Style(sheet.getWorkbook());
		new PrintOptions(sheet).optimize();
	}
	
	public void fill(Collection<Kompetenz> kompetenzen)
	{
		writeHeaderRow(sheet.createRow(0));
		writeKompetenzen(kompetenzen);
        writeKompetenStufen(kompetenzen);
        addAutoFilter();
	}

	private void writeKompetenzen(Collection<Kompetenz> kompetenzen) {
		int rowNum = KP_ROW_START;
        for(Kompetenz komp : kompetenzen)
        {
        	Row kpRow = new Row(sheet.createRow(rowNum));
        	for(int col=0; col<9; col++)
        	{	// 9 common Kompetenz prefixes for Stufe
        		sheet.addMergedRegion(range(komp, rowNum, col));
        	}
			kpRow.add(komp.code).setCellStyle(style.top);
			kpRow.add(komp.fach).setCellStyle(style.top);
			kpRow.add(komp.bereichCode).setCellStyle(style.codeShort);
			kpRow.add(komp.bereich).setCellStyle(style.top);
			kpRow.add(komp.aspektCode).setCellStyle(style.codeShort);
			kpRow.add(komp.aspekt).setCellStyle(style.topAutoBreak);
			
			XSSFCell tNrCell = kpRow.addCell();
			tNrCell.setCellValue((komp.titelNr));
			tNrCell.setCellStyle(style.codeShort);
			
			XSSFCell titelCell = kpRow.add(komp.titel);
			titelCell.setCellStyle(style.topAutoBreak);
			kpRow.add(verweiseCell(komp.verweise)).setCellStyle(style.top);
			rowNum+=komp.stufen.size();
        }
        
        optimizeSize();
	}

	private void optimizeSize() {
        sheet.setColumnWidth(Header.indexOf(Header.bCode), 4*256);
		sheet.setColumnWidth(Header.indexOf(Header.aCode), 4*256);
		sheet.setColumnWidth(Header.indexOf(Header.titelNo), 4*256);
		sheet.setColumnWidth(Header.indexOf(Header.titel), 12*4*256);
	}

	private void addAutoFilter() {
		sheet.setAutoFilter(new CellRangeAddress(
				1, sheet.getLastRowNum(), 0, Header.ALL.size()-1));
	}
	
	static void writeHeaderRow(XSSFRow row) {
		for(int col=0; col<Header.ALL.size(); col++)
		{
			String colHeader = Header.ALL.get(col);
			XSSFCell cell = row.createCell(col);
			XSSFRichTextString formattedValue = new XSSFRichTextString(colHeader);
			formattedValue.applyFont(XlsWriter.HEADER_FONT);
			cell.setCellValue(formattedValue);
		}
	}

	private void writeKompetenStufen(Collection<Kompetenz> kompetenzen) {
		int rowNum = KP_ROW_START;
        for(Kompetenz komp : kompetenzen)
        {
        	rowNum = writeKompetenzStufen(komp, sheet, rowNum);
        }
	}
	
	private int writeKompetenzStufen(Kompetenz komp, XSSFSheet sheet, int rowNum) 
	{
		for(Kompetenzstufe stufe : komp.stufen)
		{
			Row row = new Row(getOrCreate(rowNum++), STUFE_COL_START);
			XSSFCell zyklusCell = row.addCell();
			zyklusCell.setCellValue(stufe.zyklus);
			zyklusCell.setCellStyle(style.getZyklusStyle(stufe.zyklus));
			row.add(stufe.code).setCellStyle(style.top);
			XSSFCell textCell = row.add(stufe.text);
			if (stufe.grundanspruch)
			{
				textCell.setCellStyle(style.grundanspruch);
			}
			row.add(verweiseCell(stufe.verweise)).setCellStyle(style.top);
		}
		return rowNum;
	}

	private XSSFRow getOrCreate(int row)
	{
		XSSFRow existing = sheet.getRow(row);
		if (existing != null)
		{
			return existing;
		}
		return sheet.createRow(row);
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