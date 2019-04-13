package ch.monokellabs.lp21;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import ch.monokellabs.lp21.html.UeberfachlicheKpParser;
import ch.monokellabs.lp21.load.ZipHtmlLoader;

public class BaseLpTest {

	protected final String loadStaticResource(String resource) throws IOException {
		return IOUtils.toString(BaseLpTest.class.getResourceAsStream(resource), 
				StandardCharsets.UTF_8);
	}

	protected final List<String> loadLocalLp21() throws IOException {
		File zipFile = new File("test/ch/monokellabs/lp21/lu.lehrplan.ch-april.zip");
		List<String> htmlPages  = ZipHtmlLoader.loadPagesZip(zipFile);
		return htmlPages;
	}
	
	protected static final List<Kompetenz> parse(List<String> htmlPages) {
		List<String> failed = new ArrayList<>();
		List<Kompetenz> kompetenzen = htmlPages.stream()
			.map(html -> {
				Kompetenz kp = Kompetenz.parse(html);
				if (kp == null)
				{
					failed.add(html);
				}
				return kp;
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		for(String fail : failed)
		{
			kompetenzen.addAll(UeberfachlicheKpParser.parse(fail));
		}
		return kompetenzen;
	}
	
}