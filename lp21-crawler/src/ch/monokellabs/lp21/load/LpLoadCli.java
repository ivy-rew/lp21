package ch.monokellabs.lp21.load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.export.CsvWriter;
import ch.monokellabs.lp21.export.xls.XlsWriter;
import ch.monokellabs.lp21.html.LpParser;

public class LpLoadCli {

	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException
	{
		File propFile = new File(args[0]);
		Properties faecher = load(propFile);
		String kanton = args[1];
		System.out.println("Loading Kanton="+kanton+", Faecher="+faecher.keySet());
		
		List<String> htmlKps = crawHtmlPages(faecher, kanton);
		System.out.println("Crawled "+htmlKps.size()+" HTML pages");
		
		List<Kompetenz> kompetenzen = new LpParser().parse(htmlKps);
		System.out.println("Detected "+kompetenzen.size()+" 'Kompetenzen' in HTML pages.");
		
		if (args.length > 2)
		{
			String export = args[2];
			if (export.contains("csv"))
			{
				File csvFile = new File(kanton+".lp21.csv");
				saveToCsv(kompetenzen, csvFile);
				System.out.println("exported to "+csvFile.getName());
			}
			if (export.contains("xls"))
			{
				File xlsFile = new File(kanton+".lp21.xlsx");
				saveToXlsx(kompetenzen, xlsFile);
				System.out.println("exported to "+xlsFile.getName());
			}
		}
	}

	private static Properties load(File propFile) {
		if (!propFile.exists())
		{
			throw new IllegalArgumentException("Property file '"+propFile+"' does not exist.");
		}
		try(InputStream is = Files.newInputStream(propFile.toPath()))
		{
			Properties faecher = new Properties();
			faecher.load(is);
			return faecher;
		} catch (IOException ex) 
		{
			throw new IllegalArgumentException("Failed to load propery file: "+propFile, ex);
		}
	}

	private static List<String> crawHtmlPages(Properties faecher, String kanton)
			throws ClientProtocolException, IOException, URISyntaxException {
		LehrplanUri uris = new LehrplanUri(kanton);
		List<URI> fachStarts = uris.getStarts(faecher);
		KpPageLoader loader = new KpPageLoader(uris);
		List<String> htmlKps = loader.fetchLehrplan(fachStarts);
		return htmlKps;
	}

	private static void saveToCsv(List<Kompetenz> kompetenzen, File csvFile) throws IOException, FileNotFoundException {
		String csv = CsvWriter.writeKompetenzen(kompetenzen);
		try(OutputStream os = new FileOutputStream(csvFile))
		{
			IOUtils.write(csv, os, StandardCharsets.UTF_8);
		}
	}

	private static void saveToXlsx(List<Kompetenz> kompetenzen, File xlsFile) throws IOException {
		XlsWriter excelWriter = new XlsWriter();
		excelWriter.write(kompetenzen);
		try(OutputStream os = Files.newOutputStream(xlsFile.toPath()))
		{
			excelWriter.persist(os);
		}
	}
	
}
