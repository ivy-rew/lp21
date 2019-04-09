package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;

import ch.monokellabs.lp21.Kompetenz.KpEntry;
import ch.monokellabs.lp21.export.CsvWriter;
import ch.monokellabs.lp21.export.XlsWriter;

public class TestKompetenz {

	@Test
	public void parseKompetenzHtml() throws ClientProtocolException, IOException, URISyntaxException
	{
		String hoerenHtml = loadStaticResource("code=a-1-11-1-1-1.html");
		
		Kompetenz deHoeren = Kompetenz.parse(hoerenHtml);
		assertThat(deHoeren.fach).isEqualTo("Deutsch");
		assertThat(deHoeren.titel).isEqualTo("Die Schülerinnen und Schüler können Laute, Silben, Stimmen, Geräusche und Töne wahrnehmen, einordnen und vergleichen. Sie können ihren rezeptiven Wortschatz aktivieren, um das Gehörte angemessen schnell zu verstehen.");
		assertThat(deHoeren.code).isEqualTo("D.1.A.1");
		assertThat(deHoeren.entries).hasSize(8);
		KpEntry first = deHoeren.entries.get(0);
		assertThat(first.zyklus).isEqualTo(1);
		assertThat(first.code).isEqualTo("D.1.A.1.a");
		assertThat(first.text).isEqualTo("können die Aufmerksamkeit auf die sprechende Person und deren Beitrag richten.");
	}
	
	@Test
	public void loadPageCached() throws ClientProtocolException, IOException, URISyntaxException
	{
		KpPageLoader kompetenz = cachedLoader();
		kompetenz.setKanton("Luzern");
		kompetenz.setZyklus(1);
		URI testDeHoeren = LehrplanUri.createLpUri("a|1|11|1|1|1");
		String html = kompetenz.fetch(testDeHoeren);
		assertThat(html).isNotNull();
		
		Kompetenz deHoeren = Kompetenz.parse(html);
		assertThat(deHoeren.fach).isEqualTo("Deutsch");
		assertThat(deHoeren.code).isEqualTo("D.1.A.1");
	}
	
	@Test
	public void canNavigateToNext() throws IOException, URISyntaxException
	{
		String hoerenHtml = loadStaticResource("code=a-1-11-1-1-1.html");
		URI nextUri = LehrplanUri.parseNext(hoerenHtml);
		assertThat(nextUri.getQuery().toString()).isEqualTo("code=a|1|11|1|2|1");
		
		String lastMathHtml = loadStaticResource("code=a-5-0-3-3-3.html");
		URI noLink = LehrplanUri.parseNext(lastMathHtml);
		assertThat(noLink).isNull();
	}

	private String loadStaticResource(String resource) throws IOException {
		return IOUtils.toString(TestKompetenz.class.getResourceAsStream(resource), StandardCharsets.UTF_8);
	}
	
	@Test
	public void runThroughFach() throws Exception
	{
		URI testDeHoeren = LehrplanUri.createLpUri("a|1|11|1|1|1");
		List<String> deutsch = cachedLoader().fetchFach(testDeHoeren);
		assertThat(deutsch).hasSize(28);
	}
	
	@Test
	public void writeCsv() throws IOException
	{
		String hoerenHtml = loadStaticResource("code=a-1-11-1-1-1.html");
		Kompetenz deHoeren = Kompetenz.parse(hoerenHtml);
		String csv = CsvWriter.writeKompetenzen(Collections.singletonList(deHoeren));
		assertThat(csv).startsWith("FACH;CODE");
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
		List<String> htmlPages = loadPages();
		assertThat(htmlPages).isNotEmpty();
		System.out.println("Loaded "+htmlPages.size()+" competences from LP21");
		
		List<Kompetenz> kompetenzen = parse(htmlPages);
		assertThat(kompetenzen).hasSameSizeAs(htmlPages);
		
		File csv = new File("target/kompetenzen.csv");
		writeToCsvFile(kompetenzen, csv);
	}

	@Test
	public void xlsLehrplan() throws Exception
	{
		List<String> htmlPages = loadPages();
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
	
	private static void writeToCsvFile(List<Kompetenz> kompetenzen, File csv) throws IOException {
		try(OutputStream os = new FileOutputStream(csv))
		{
			String csvContent = CsvWriter.writeKompetenzen(kompetenzen);
			IOUtils.write(csvContent, os, StandardCharsets.UTF_8);
		}
	}

	private List<String> loadPages() throws URISyntaxException, ClientProtocolException, IOException {
		List<URI> starts = LehrplanUri.getStarts();
		List<String> htmlPages = cachedLoader().fetchLehrplan(starts);
		return htmlPages;
	}

	private static KpPageLoader cachedLoader() {
		KpPageLoader loader = new KpPageLoader();
		loader.setCacheDir(new File("target/site-cache-april"));
		return loader;
	}

	private static List<Kompetenz> parse(List<String> htmlPages) {
		List<Kompetenz> kompetenzen = htmlPages.stream()
			.map(html -> Kompetenz.parse(html))
			.collect(Collectors.toList());
		return kompetenzen;
	}
	
	@Test @Ignore("not yet implemented")
	public void canStoreInDb()
	{
	}
	
}
