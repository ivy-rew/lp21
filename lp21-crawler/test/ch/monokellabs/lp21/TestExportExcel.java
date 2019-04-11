package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import ch.monokellabs.lp21.export.XlsWriter;

public class TestExportExcel extends BaseLpTest {

	@Test
	public void xlsLehrplan() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		assertThat(htmlPages).isNotEmpty();
		System.out.println("Loaded "+htmlPages.size()+" competences from LP21");
		
		List<Kompetenz> kompetenzen = parse(htmlPages);
		assertThat(kompetenzen).hasSameSizeAs(htmlPages);
		
		XSSFWorkbook workbook = XlsWriter.write(kompetenzen);
		File xls = new File("target/kompetenzen.xls");
		try(OutputStream out = new FileOutputStream(xls))
		{
			XlsWriter.persist(workbook, out);
		}
	}
	
}
