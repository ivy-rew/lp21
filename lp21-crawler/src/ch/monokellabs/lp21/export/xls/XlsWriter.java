package ch.monokellabs.lp21.export.xls;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.export.xls.meta.MetaData;

public class XlsWriter {

	public static final XSSFFont HEADER_FONT = new XSSFFont();
	static{
		HEADER_FONT.setBold(true);
	}

	private final XSSFWorkbook workbook;
	
	public XlsWriter()
	{
		workbook = new XSSFWorkbook();
		new MetaData(workbook).write();
	}

	public XSSFWorkbook write(Collection<Kompetenz> kompetenzen)
	{
		writeMerged(kompetenzen);
        writeRaw(kompetenzen);
        return workbook;
    }

	public XSSFSheet writeMerged(Collection<Kompetenz> kompetenzen) {
		XSSFSheet lp21 = workbook.createSheet("LP21");
		new MergedSheet(lp21).fill(kompetenzen);
		return lp21;
	}
	
	public void writeRaw(Collection<Kompetenz> kompetenzen) {
		new RawSheet(workbook.createSheet("RAW")).fill(kompetenzen);
	}
	
	public void persist(OutputStream out) throws IOException {
	    workbook.write(out);
	    workbook.close();
	}
	
}
