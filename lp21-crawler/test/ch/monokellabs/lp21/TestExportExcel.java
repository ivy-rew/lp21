package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Test;

import ch.monokellabs.lp21.export.xls.Header;
import ch.monokellabs.lp21.export.xls.IndividualSheet;
import ch.monokellabs.lp21.export.xls.XlsWriter;

public class TestExportExcel extends BaseLpTest {

	@Test
	public void xlsMerged() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		List<Kompetenz> kompetenzen = parse(htmlPages);
		String de = "Deutsch";
		List<Kompetenz> deutsch = kompetenzen.stream()
			.filter(kp -> kp.fach.equals(de))
			.collect(Collectors.toList());
		
		XlsWriter excel = new XlsWriter();
		XSSFSheet sheet = excel.writeMerged(deutsch);
		
		assertThat(sheet.getRow(0).getPhysicalNumberOfCells())
			.as("has headers")
			.isEqualTo(Header.ALL.size());
		
		XSSFRow kpRow = sheet.getRow(2);
		assert_D1A1a(kpRow);

		File xls = new File("target/deutsch.xlsx");
		try(OutputStream out = new FileOutputStream(xls))
		{
			excel.persist(out);
		}
	}

	private static void assert_D1A1a(XSSFRow kpRow) {
		assertThat(kpRow.getPhysicalNumberOfCells()).isEqualTo(Header.ALL.size());
		assertThat(kpRow.getCell(0).getStringCellValue()).isEqualTo("D.1.A.1");
		assertThat(kpRow.getCell(1).getStringCellValue()).isEqualTo("Deutsch");
		assertThat(kpRow.getCell(2).getStringCellValue()).isEqualTo("D.1");
		assertThat(kpRow.getCell(3).getStringCellValue()).isEqualTo("Hören");
		assertThat(kpRow.getCell(4).getStringCellValue()).isEqualTo("A");
		assertThat(kpRow.getCell(5).getStringCellValue()).isEqualTo("Grundfertigkeiten");
		assertThat(kpRow.getCell(6).getNumericCellValue()).isEqualTo(1);
		assertThat(kpRow.getCell(7).getStringCellValue()).contains("können Laute, Silben, Stimmen");
		assertThat(kpRow.getCell(9).getNumericCellValue()).isEqualTo(1);
		assertThat(kpRow.getCell(10).getStringCellValue()).isEqualTo("D.1.A.1.a");
		assertThat(kpRow.getCell(11).getStringCellValue()).startsWith("können die Aufmerksamkeit");
	}
	
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
		
		File xls = new File("target/deutschAll.xlsx");
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
		assertThat(kompetenzen).hasSize(407); // inkl ueberfachlich
		
		XlsWriter workbook = new XlsWriter();
		workbook.write(kompetenzen);
		File xls = new File("target/kompetenzenAll.xlsx");
		try(OutputStream out = new FileOutputStream(xls))
		{
			workbook.persist(out);
		}
	}

	@Test
	public void testNextColName()
	{
		assertThat(IndividualSheet.getNextName("A", 2)).isEqualTo("C");
		assertThat(IndividualSheet.getNextName("F", 1)).isEqualTo("G");
		assertThat(IndividualSheet.getNextName("Z", 1)).isEqualTo("AA");
		assertThat(IndividualSheet.getNextName("AA", 1)).isEqualTo("AB");
		assertThat(IndividualSheet.getNextName("Z", 2)).isEqualTo("AB");
	}
}
