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
	public void verbAfterGenderPrefix() throws Exception
	{
		List<String> htmlPages = loadLocalLp21();
		List<Kompetenz> kompetenzen = parse(htmlPages);
		
		Set<String> verbs = kompetenzen.stream()
			.filter(kp -> kp.titel.isGenderized())
			.map(kp -> kp.titel.withoutGenderPrefix())
			.map(shortened -> StringUtils.substringBefore(shortened, " "))
			.collect(Collectors.toSet());
		
		String[] mehrzahl = Genderized.VERBS.keySet().toArray(new String[Genderized.VERBS.size()]);
		assertThat(verbs)
			.as("prefix following verbs are very limited and well known")
			.containsExactly(mehrzahl);
	}
	
}
