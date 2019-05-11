package ch.monokellabs.lp21.load;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

public class LpLoadCli {

	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException
	{
		File propFile = new File(args[0]);
		Properties faecher = load(propFile);
		String kanton = args[1];
		System.out.println("Loading Kanton="+kanton+", Faecher="+faecher.keySet());
		
		LehrplanUri uris = new LehrplanUri(kanton);
		List<URI> fachStarts = uris.getStarts(faecher);
		KpPageLoader loader = new KpPageLoader(uris);
		List<String> htmlKps = loader.fetchLehrplan(fachStarts);
		System.out.println("DONE! Loaded "+htmlKps.size()+" Kompetenzen");
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
	
}
