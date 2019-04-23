package ch.monokellabs.lp21.export.xls;

import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Generic Header rows for list of competences
 */
public class KpHeader {
	
	private final XSSFSheet sheet;

	private static final List<String> HEADERS = Header.ALL;
	
	public KpHeader(XSSFSheet sheet)
	{
		this.sheet= sheet;
	}
	
	public void writeColTitle(int rowNum) {
		XSSFRow row = sheet.createRow(rowNum);
		for(int col=0; col<HEADERS.size(); col++)
		{
			String colHeader = HEADERS.get(col);
			XSSFCell cell = row.createCell(col);
			XSSFRichTextString formattedValue = new XSSFRichTextString(colHeader);
			formattedValue.applyFont(XlsWriter.HEADER_FONT);
			cell.setCellValue(formattedValue);
		}
	}
	
	public void addAutoFilter(int row) {
		sheet.setAutoFilter(new CellRangeAddress(
				row, sheet.getLastRowNum(), 0, Header.ALL.size()-1));
	}
	
	public void freezeHeaderAndFilter() {
		sheet.createFreezePane(0, 2);
	}
	
}
