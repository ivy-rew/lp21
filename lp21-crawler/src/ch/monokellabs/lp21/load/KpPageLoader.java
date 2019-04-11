package ch.monokellabs.lp21.load;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class KpPageLoader {
	private static final Logger LOGGER = Logger.getLogger(KpPageLoader.class.getName());
	
	private String kanton;
	private int zyklus;
	
	private File siteCacheDir = new File(System.getProperty("java.io.tmpdir"), "lp21");
	private boolean useCache = true;
	
	public void setKanton(String kanton) {
		this.kanton = kanton;
	}

	public void setZyklus(int zyklus) {
		this.zyklus = zyklus;
	}
	
	public void setCacheDir(File cache)
	{
		this.siteCacheDir = cache;
	}
	
	public List<String> fetchLehrplan(List<URI> fachStarts) throws ClientProtocolException, IOException, URISyntaxException
	{
		List<String> html = new LinkedList<>();
		for(URI fach : fachStarts)
		{
			html.addAll(fetchFach(fach));
		}
		return html;
	}

	public List<String> fetchFach(URI firstKpUri) throws ClientProtocolException, IOException, URISyntaxException
	{
		List<String> htmlSites = new LinkedList<>();
		URI nextUri = firstKpUri;
		do{
			String kpHtml = fetch(nextUri);
			htmlSites.add(kpHtml);
			nextUri = LehrplanUri.parseNext(kpHtml);
		}while(nextUri != null);
		LOGGER.info("Done: Found "+htmlSites.size()+" Kompetenzen");
		return htmlSites;
	}
	
	public String fetch(URI testDeHoeren) throws ClientProtocolException, IOException {
		File lp21 = new File(siteCacheDir, testDeHoeren.getHost());
		lp21.mkdirs();
		File html = new File(lp21, toFileName(testDeHoeren));
		if (!useCache || !html.exists())
		{
			Files.copy(fetchRemote(testDeHoeren), html.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		return FileUtils.readFileToString(html, StandardCharsets.UTF_8);
	}
	
	private InputStream fetchRemote(URI remoteKpUri) throws IOException, ClientProtocolException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		LOGGER.info("fetching remote content from "+remoteKpUri);
		CloseableHttpResponse response = client.execute(new HttpGet(remoteKpUri));
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
		{
			throw new RuntimeException("faild to featch page "+remoteKpUri
					+" returned http "+response.getStatusLine());
		}
		return response.getEntity().getContent();
	}

	private String toFileName(URI lpUri) {
		return StringUtils.replace(lpUri.getQuery(),"|","-")+".html";
	}

}
