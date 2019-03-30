package ch.monokellabs.lp21;

import org.junit.Ignore;
import org.junit.Test;

import ch.monokellabs.lp21.Kompetenz.KpEntry;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class TestKompetenz {

	@Test
	public void parseKompetenzHtml() throws ClientProtocolException, IOException, URISyntaxException
	{
		String hoerenHtml = IOUtils.toString(TestKompetenz.class.getResourceAsStream("code=a-1-11-1-1-1.html"), StandardCharsets.UTF_8);
		
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
		URI testDeHoeren = new URIBuilder("https://lu.lehrplan.ch/index.php").addParameter("code", "a|1|11|1|1|1").build();
		String html = kompetenz.fetch(testDeHoeren);
		assertThat(html).isNotNull();
		
		Kompetenz deHoeren = Kompetenz.parse(html);
		assertThat(deHoeren.fach).isEqualTo("Deutsch");
		assertThat(deHoeren.code).isEqualTo("D.1.A.1");
	}
	
	@Test
	public void navigateThroughFach() throws IOException
	{
		String hoerenHtml = IOUtils.toString(TestKompetenz.class.getResourceAsStream("code=a-1-11-1-1-1.html"), StandardCharsets.UTF_8);
		Elements nexts = Jsoup.parse(hoerenHtml).select("a[original-title*=Zur nachfolgenden]");
		assertThat(nexts).hasSize(1);
		String nextUri = nexts.get(0).attr("href");
		assertThat(nextUri).isEqualTo("index.php?code=a|1|11|1|2|1");
	}
	
	@Test @Ignore("not yet implemented")
	public void canStoreInDb()
	{
	}
	
}
