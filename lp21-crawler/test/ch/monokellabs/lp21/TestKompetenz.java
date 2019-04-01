package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;

import ch.monokellabs.lp21.Kompetenz.KpEntry;

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
		KpPageLoader kompetenz = new KpPageLoader();
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
		List<String> deutsch = new KpPageLoader().fetchFach(testDeHoeren);
		assertThat(deutsch).hasSize(28);
	}
	
	@Test
	public void downloadAll() throws Exception
	{
		List<URI> starts = LehrplanUri.getStarts();
		List<String> all = new KpPageLoader().fetchLehrplan(starts);
		assertThat(all).isNotEmpty();
	}
	
	@Test @Ignore("not yet implemented")
	public void canStoreInDb()
	{
	}
	
}
