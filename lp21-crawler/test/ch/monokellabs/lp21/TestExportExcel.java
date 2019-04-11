package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ch.monokellabs.lp21.export.xls.XlsWriter;

public class TestExportExcel extends BaseLpTest {

	
	@Test
	public void xlsDeutsch() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		List<Kompetenz> kompetenzen = parse(htmlPages);
		String de = "Deutsch";
		List<Kompetenz> deutsch = kompetenzen.stream()
			.filter(kp -> kp.fach.equals(de))
			.collect(Collectors.toList());
		
		XlsWriter workbook = new XlsWriter();
		workbook.write(deutsch);
		File xls = new File("target/deutsch.xls");
		try(OutputStream out = new FileOutputStream(xls))
		{
			workbook.persist(out);
		}
	}
	
	@Test
	public void xlsLehrplan21() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		assertThat(htmlPages).isNotEmpty();
		System.out.println("Loaded "+htmlPages.size()+" competences from LP21");
		
		List<Kompetenz> kompetenzen = parse(htmlPages);
		assertThat(kompetenzen).hasSameSizeAs(htmlPages);
		
		XlsWriter workbook = new XlsWriter();
		workbook.write(kompetenzen);
		File xls = new File("target/kompetenzen.xls");
		try(OutputStream out = new FileOutputStream(xls))
		{
			workbook.persist(out);
		}
	}
	
}
