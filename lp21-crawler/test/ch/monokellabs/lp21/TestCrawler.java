package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import ch.monokellabs.lp21.load.KpPageLoader;
import ch.monokellabs.lp21.load.LehrplanUri;
import ch.monokellabs.lp21.load.LpLuzern;

public class TestCrawler extends BaseLpTest {

	@Test
	public void loadPageCached() throws ClientProtocolException, IOException, URISyntaxException
	{
		KpPageLoader kompetenz = cachedLoader();
		URI testDeHoeren = new LehrplanUri().createLpUri("a|1|11|1|1|1");
		String html = kompetenz.fetch(testDeHoeren);
		assertThat(html).isNotNull();
		
		Kompetenz deHoeren = Kompetenz.parse(html);
		assertThat(deHoeren.fach).isEqualTo("Deutsch");
		assertThat(deHoeren.code).isEqualTo("D.1.A.1");
	}
	
	@Test
	public void runThroughFach() throws Exception
	{
		URI testDeHoeren = new LehrplanUri().createLpUri("a|1|11|1|1|1");
		List<String> deutsch = cachedLoader().fetchFach(testDeHoeren);
		assertThat(deutsch).hasSize(28);
	}
	
	@Test
	public void loadUncategorized() throws Exception
	{
		URI ueberfachlich = new LehrplanUri().createLpUri(LpLuzern.UEBERFACHLICHE.code);
		String uef = cachedLoader().fetch(ueberfachlich);
		assertThat(uef).contains("Personale Kompetenzen");
		
		URI ezUri = new LehrplanUri().createLpUri(LpLuzern.ENTWICKLUNGSORIENTIERTE_ZUGAENGE.code);
		String ezHtml = cachedLoader().fetch(ezUri);
		assertThat(ezHtml).contains("Entwicklungsorientierte Zugänge");
	}
	
	@Test
	public void loadFulLP21() throws Exception
	{
		assertThat(loadPages().size()).isEqualTo(400);
	}
	
	@Test
	public void storeLucernLp() throws FileNotFoundException, IOException
	{
		Properties props = LpLuzern.toProps();
		try(OutputStream os = new FileOutputStream(new File("target/lu.lp21.starts.properties")))
		{
			props.store(os, "Lehrplan21 Luzern");
		}
	}
	
	@Test
	public void loadThirdparty_AG() throws FileNotFoundException, IOException, URISyntaxException
	{
		Properties ag = new Properties();
		try(InputStream is = TestCrawler.class.getResourceAsStream("ag.lp21.starts.properties"))
		{
			ag.load(is);
		}
		
		LehrplanUri agUri = new LehrplanUri("ag");
		List<URI> fachStarts = agUri.getStarts(ag);
		assertThat(fachStarts).hasSize(20);
		
		KpPageLoader loader = new KpPageLoader("ag");
		List<String> kpHtmls = loader.fetchLehrplan(fachStarts);
		assertThat(kpHtmls).hasSize(400);
	}
	
	private static List<String> loadPages() throws URISyntaxException, ClientProtocolException, IOException {
		List<URI> starts = LpLuzern.getStarts();
		List<String> htmlPages = cachedLoader().fetchLehrplan(starts);
		return htmlPages;
	}

	private static KpPageLoader cachedLoader() {
		KpPageLoader loader = new KpPageLoader("lu");
		loader.setCacheDir(new File("target/site-cache-april"));
		return loader;
	}
	
}
