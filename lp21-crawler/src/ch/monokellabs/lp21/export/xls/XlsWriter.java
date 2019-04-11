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
		writeMerged(kompetenzen);
        writeRaw(kompetenzen);
        return workbook;
    }

	public void writeMerged(Collection<Kompetenz> kompetenzen) {
		new MergedSheet(workbook.createSheet("LP21")).fill(kompetenzen);
	}
	
	public void writeRaw(Collection<Kompetenz> kompetenzen) {
		new RawSheet(workbook.createSheet("RAW")).fill(kompetenzen);
	}
	
	public void persist(OutputStream out) throws IOException {
	    workbook.write(out);
	    workbook.close();
	}
	
}
