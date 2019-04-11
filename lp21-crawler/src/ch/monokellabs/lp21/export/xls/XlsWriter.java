package ch.monokellabs.lp21.export.xls;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ch.monokellabs.lp21.Kompetenz;

public class XlsWriter {

	public static final XSSFFont HEADER_FONT = new XSSFFont();
	static{
		HEADER_FONT.setBold(true);
	}

	private XSSFWorkbook workbook;
	
	public XlsWriter()
	{
		workbook = new XSSFWorkbook();
	}
	
	public XSSFWorkbook write(Collection<Kompetenz> kompetenzen)
	{
        RawSheet rawSheet = new RawSheet(workbook.createSheet("Kompetenzen"));
        rawSheet.fill(kompetenzen);
        return workbook;
    }
	
	public void persist(OutputStream out) throws IOException {
	    workbook.write(out);
	    workbook.close();
	}
	
}
