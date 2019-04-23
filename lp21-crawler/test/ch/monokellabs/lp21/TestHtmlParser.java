package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import ch.monokellabs.lp21.html.UeberfachlicheKpParser;
import ch.monokellabs.lp21.load.LehrplanUri;

public class TestHtmlParser extends BaseLpTest {

	@Test
	public void parseKompetenzHtml() throws ClientProtocolException, IOException, URISyntaxException
	{
		String hoerenHtml = loadStaticResource("code=a-1-11-1-1-1.html");
		
		Kompetenz deHoeren = Kompetenz.parse(hoerenHtml);
		assertThat(deHoeren.fach).isEqualTo("Deutsch");
		assertThat(deHoeren.bereichCode).isEqualTo("D.1");
		assertThat(deHoeren.bereich).isEqualTo("Hören");
		assertThat(deHoeren.aspektCode).isEqualTo("A");
		assertThat(deHoeren.aspekt).isEqualTo("Grundfertigkeiten");
		assertThat(deHoeren.titelNr).isEqualTo(1);
		assertThat(deHoeren.titel.getValue()).isEqualTo("Die Schülerinnen und Schüler können Laute, Silben, Stimmen, Geräusche und Töne wahrnehmen, einordnen und vergleichen. Sie können ihren rezeptiven Wortschatz aktivieren, um das Gehörte angemessen schnell zu verstehen.");
		assertThat(deHoeren.verweise).hasSize(2);
		assertThat(deHoeren.verweise).containsExactly(
				new Verweis("EZ", "Räumliche Orientierung (4)"),
				new Verweis("EZ", "Wahrnehmung (2)"));
		assertThat(deHoeren.code).isEqualTo("D.1.A.1");
		
		assertThat(deHoeren.stufen).hasSize(8);
		Kompetenzstufe first = deHoeren.stufen.get(0);
		assertThat(first.zyklus).isEqualTo(1);
		assertThat(first.code).isEqualTo("D.1.A.1.a");
		assertThat(first.text).isEqualTo("können die Aufmerksamkeit auf die sprechende Person und deren Beitrag richten.");
		assertThat(first.grundanspruch).isEqualTo(false);
		
		Kompetenzstufe fourth = deHoeren.stufen.get(3);
		assertThat(fourth.code).isEqualTo("D.1.A.1.d");
		assertThat(fourth.text).isEqualTo("können unterschiedliche Laute und Lautverbindungen heraushören, im Wort verorten (Anlaut, Mittellaute, Endlaut) und mit Erfahrungen aus der Erstsprache vergleichen.\n"
				+ "können einzelne Wörter und Wendungen in vertrauten Situationen verstehen oder deren Bedeutung erfragen und so ihren rezeptiven Wortschatz erweitern.");
		assertThat(fourth.grundanspruch).isTrue();
		assertThat(fourth.verweise).hasSize(2);
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
	
	@Test
	public void parseUeberfachlicheKp() throws Exception
	{
		String ueberHtml = loadStaticResource("code=e-200-3.html");
		
		List<Kompetenz> ueberfachlich = UeberfachlicheKpParser.parse(ueberHtml);
		Kompetenz selfreflect = ueberfachlich.get(0);
		
		assertThat(selfreflect.code).isEqualTo("UE.P.A.1");
		assertThat(selfreflect.fach).isEqualTo("Überfachliche Kompetenzen");
		assertThat(selfreflect.bereichCode).isEqualTo("P");
		assertThat(selfreflect.bereich).isEqualTo("Personale Kompetenzen");
		assertThat(selfreflect.aspektCode).isEqualTo("A");
		assertThat(selfreflect.aspekt).isEqualTo("Selbstreflexion");
		assertThat(selfreflect.titelNr).isEqualTo(1);
		assertThat(selfreflect.titel.getValue()).isEqualTo("Eigene Ressourcen kennen und nutzen");
		
		assertThat(selfreflect.stufen).hasSize(8);
		Kompetenzstufe stufe1 = selfreflect.stufen.get(0);
		assertThat(stufe1.text)
			.isEqualTo("können eigene Gefühle wahrnehmen und situationsangemessen ausdrücken.");
		assertThat(stufe1.code).isEqualTo("UE.P.A.1.a");
		
		Kompetenz selbststaendig = ueberfachlich.get(1);
		assertThat(selbststaendig.aspekt).isEqualTo("Selbstständigkeit");
		assertThat(selbststaendig.aspektCode).isEqualTo("B");
		assertThat(selbststaendig.titelNr).isEqualTo(2);
	}
	
	@Test
	public void parseInformatikMedienKp() throws IOException
	{
		String mi12html = loadStaticResource("code=a-10-0-1-0-2.html");
		Kompetenz mi12 = Kompetenz.parse(mi12html);
		
		assertThat(mi12.code).isEqualTo("MI.1.2");
		assertThat(mi12.titel.getValue()).isEqualTo("Die Schülerinnen und Schüler können "
				+ "Medien und Medienbeiträge entschlüsseln, reflektieren und nutzen.");
		assertThat(mi12.aspekt)
			.as("can parse informal sub title as aspect!")
			.isEqualTo("Medien und Medienbeiträge verstehen");
	}
}
