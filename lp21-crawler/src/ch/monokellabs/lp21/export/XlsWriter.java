package ch.monokellabs.lp21.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.Kompetenz.KpEntry;

public class XlsWriter {

	public static XSSFWorkbook write(Collection<Kompetenz> kompetenzen)
	{
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Kompetenzen");
	        
	        writeHeaderRow(sheet.createRow(0));
	        
	        int rowNum = 2;
	        for(Kompetenz komp : kompetenzen)
	        {
	        	rowNum = writeKompetenz(komp, sheet, rowNum);
	        }
	        return workbook;
	    }

	private static void writeHeaderRow(XSSFRow row) {
		List<String> headers = Arrays.asList("Fach","Code","Titel","Code komplett","Zyklus","Text");
		for(int col=0; col<headers.size(); col++)
		{
			String colHeader = headers.get(col);
			XSSFCell headercell = row.createCell(col);
			headercell.setCellValue(colHeader);
		}
	}

	private static int writeKompetenz(Kompetenz komp, XSSFSheet sheet, int rowNum) {
		for(KpEntry entry : komp.entries)
		{
			XSSFRow row = sheet.createRow(rowNum++);
			int col = 0;
			row.createCell(col++).setCellValue(komp.code);
			row.createCell(col++).setCellValue(komp.fach);
			row.createCell(col++).setCellValue(komp.titel);
			
			row.createCell(col++).setCellValue(entry.code);
			row.createCell(col++).setCellValue(entry.zyklus);
			row.createCell(col++).setCellValue(entry.text);
		}
		return rowNum;
	}

	public static void persist(XSSFWorkbook workbook, OutputStream out) throws IOException {
		    workbook.write(out);
		    workbook.close();
	}
	
}
