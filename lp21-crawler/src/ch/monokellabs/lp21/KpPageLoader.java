package ch.monokellabs.lp21;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class KpPageLoader {

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

	private InputStream fetchRemote(URI testDeHoeren) throws IOException, ClientProtocolException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = client.execute(new HttpGet(testDeHoeren));
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
		{
			throw new RuntimeException("faild to featch page "+testDeHoeren
					+" returned http "+response.getStatusLine());
		}
		return response.getEntity().getContent();
	}

	private String toFileName(URI lpUri) {
		return StringUtils.replace(lpUri.getQuery(),"|","-")+".html";
	}

}
