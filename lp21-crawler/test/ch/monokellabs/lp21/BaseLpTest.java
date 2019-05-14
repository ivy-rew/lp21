package ch.monokellabs.lp21;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;

import ch.monokellabs.lp21.html.LpParser;
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
	
	protected final List<Kompetenz> parse(List<String> htmlpages)
	{
		return new LpParser().parse(htmlpages);
	}
}