package ch.monokellabs.lp21;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class TestIndividualLp extends BaseLpTest {

	@Test
	public void detectGenderizedLanguage_inTitel() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		List<Kompetenz> kompetenzen = parse(htmlPages);
		
		List<Kompetenz> individualized = kompetenzen.stream()
			.filter(kp -> !kp.titel.isGenderized())
			.collect(Collectors.toList());
		List<Kompetenz> ueberfachlich = kompetenzen.stream()
				.filter(kp -> kp.fach.startsWith("Überfachliche Kompetenzen"))
				.collect(Collectors.toList());
		
		assertThat(individualized).hasSameSizeAs(ueberfachlich);
	}
	
	@Test
	public void individualizeTitle()
	{
		String mi12Titel = "Die Schülerinnen und Schüler können "
				+ "Medien und Medienbeiträge entschlüsseln, reflektieren und nutzen.";
		String shortened = new Genderized(mi12Titel).indiviudalized();
		assertThat(shortened).isEqualTo("kann Medien und Medienbeiträge entschlüsseln, reflektieren und nutzen.");
	}
	
	@Test
	public void individualizeTitle_literature()
	{
		String d6c = "Die Schülerinnen und Schüler erfahren, erkennen und reflektieren, "
				+ "dass literarische Texte in Bezug auf Inhalt, Form und Sprache bewusst gestaltet sind, um eine ästhetische Wirkung zu erzielen. Sie kennen wesentliche Merkmale von Genres und literarischen Gattungen.";
		String shortened = new Genderized(d6c).indiviudalized();
		assertThat(shortened).isEqualTo("erfährt, erkennt und reflektiert, dass literarische Texte in Bezug auf Inhalt, "
				+ "Form und Sprache bewusst gestaltet sind, um eine ästhetische Wirkung zu erzielen. "
				+ "Sie kennen wesentliche Merkmale von Genres und literarischen Gattungen.");
	}
	
	@Test
	public void verbAfterGenderPrefix() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		List<Kompetenz> kompetenzen = parse(htmlPages);
		
		Set<String> verbs = kompetenzen.stream()
			.filter(kp -> kp.titel.isGenderized())
			.map(kp -> kp.titel.withoutGenderPrefix())
			.map(shortened -> StringUtils.substringBefore(shortened, " "))
			.collect(Collectors.toSet());
		
		assertThat(verbs)
			.as("prefix following verbs are very limited and well known")
			.containsExactly("können",
				    "erfahren,",
				    "verstehen",
				    "kennen",
				    "begegnen",
				    "experimentieren",
				    "verfügen");
	}
	
}
