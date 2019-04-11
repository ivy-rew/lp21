package ch.monokellabs.lp21.load;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public class ZipHtmlLoader
{
	private static final Logger LOGGER = Logger.getLogger(ZipHtmlLoader.class.getName());
	
	public static List<String> loadPagesZip(File zipFile) throws IOException {
		String fileUri = zipFile.getAbsoluteFile().toURI().toASCIIString();
		URI uri = URI.create("jar:"+fileUri);
		try(FileSystem zipFs = FileSystems.newFileSystem(uri, Collections.emptyMap()))
		{
			Path firstDir = zipFs.getPath("/lu.lehrplan.ch");
            try {
				return readHtmlDir(firstDir);
			} catch (IOException ex) {
				LOGGER.log(Level.WARNING, "Failed to read "+firstDir+" of "+zipFile, ex);
			}
		}
		return Collections.emptyList();
	}

	private static List<String> readHtmlDir(Path firstDir) throws IOException {
		return Files.walk(firstDir)
			.filter(path -> !Files.isDirectory(path))
			.map(path -> readFile(path))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	private static String readFile(Path path) {
		try(InputStream htmlStream = Files.newInputStream(path))
		{
			return IOUtils.toString(htmlStream, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, "Failed to read "+path, ex);
			return null;
		}
	}
}