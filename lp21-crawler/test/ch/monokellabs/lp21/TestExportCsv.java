package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import ch.monokellabs.lp21.export.CsvWriter;

public class TestExportCsv extends BaseLpTest {
	
	@Test
	public void writeCsv() throws IOException
	{
		String hoerenHtml = loadStaticResource("code=a-1-11-1-1-1.html");
		Kompetenz deHoeren = Kompetenz.parse(hoerenHtml);
		String csv = CsvWriter.writeKompetenzen(Collections.singletonList(deHoeren));
		assertThat(csv).startsWith("K Code;Fach");
		int expetedRowCount = 8+1;
		assertThat(StringUtils.countMatches(csv, CsvWriter.ROW_SEPARATOR))
			.as("contains a header and a single kompetenz entry (with 8 sub entries)")
			.isEqualTo(expetedRowCount);
		
		File csvFile = new File("target/deutsch.csv");
		writeToCsvFile(Collections.singletonList(deHoeren), csvFile);
	}
	
	@Test
	public void downloadParseCsv_All() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		assertThat(htmlPages).isNotEmpty();
		System.out.println("Loaded "+htmlPages.size()+" competences from LP21");
		
		List<Kompetenz> kompetenzen = parse(htmlPages);
		assertThat(kompetenzen).hasSameSizeAs(htmlPages);
		
		File csv = new File("target/kompetenzen.csv");
		writeToCsvFile(kompetenzen, csv);
	}

	private static void writeToCsvFile(List<Kompetenz> kompetenzen, File csv) throws IOException {
		try(OutputStream os = new FileOutputStream(csv))
		{
			String csvContent = CsvWriter.writeKompetenzen(kompetenzen);
			IOUtils.write(csvContent, os, StandardCharsets.UTF_8);
		}
	}
	
}
