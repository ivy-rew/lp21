package ch.monokellabs.lp21;

import org.junit.Test;

import ch.monokellabs.lp21.Kompetenz.KpEntry;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;

public class TestKompetenz {

	@Test
	public void test() throws ClientProtocolException, IOException, URISyntaxException
	{
		KpPageLoader kompetenz = new KpPageLoader();
		kompetenz.setKanton("Luzern");
		kompetenz.setZyklus(1);
		URI testDeHoeren = new URIBuilder("https://lu.lehrplan.ch/index.php").addParameter("code", "a|1|11|1|1|1").build();
		String html = kompetenz.fetch(testDeHoeren);
		assertThat(html).isNotNull();

		Kompetenz deHoeren = Kompetenz.parse(html);
		assertThat(deHoeren.fach).isEqualTo("Deutsch");
		assertThat(deHoeren.titel).isEqualTo("Die Schülerinnen und Schüler können Laute, Silben, Stimmen, Geräusche und Töne wahrnehmen, einordnen und vergleichen. Sie können ihren rezeptiven Wortschatz aktivieren, um das Gehörte angemessen schnell zu verstehen.");
		assertThat(deHoeren.code).isEqualTo("D.1.A.1");
		assertThat(deHoeren.entries).hasSize(8);
		KpEntry first = deHoeren.entries.get(0);
		assertThat(first.zyklus).isEqualTo(1);
		assertThat(first.code).isEqualTo("D.1.A.1.a");
		assertThat(first.text).isEqualTo("können die Aufmerksamkeit auf die sprechende Person und deren Beitrag richten.");
	
		
	}
	
}
