package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import ch.monokellabs.lp21.load.KpPageLoader;
import ch.monokellabs.lp21.load.LehrplanUri;

public class TestCrawler extends BaseLpTest {

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
	public void runThroughFach() throws Exception
	{
		URI testDeHoeren = LehrplanUri.createLpUri("a|1|11|1|1|1");
		List<String> deutsch = cachedLoader().fetchFach(testDeHoeren);
		assertThat(deutsch).hasSize(28);
	}
	
	@Test
	public void loadFulLP21() throws Exception
	{
		assertThat(loadPages().size()).isEqualTo(398);
	}
	
	private static List<String> loadPages() throws URISyntaxException, ClientProtocolException, IOException {
		List<URI> starts = LehrplanUri.getStarts();
		List<String> htmlPages = cachedLoader().fetchLehrplan(starts);
		return htmlPages;
	}

	private static KpPageLoader cachedLoader() {
		KpPageLoader loader = new KpPageLoader();
		loader.setCacheDir(new File("target/site-cache-april"));
		return loader;
	}
	
}
