package com.github.manikmagar.maven.versioner.extension;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

public class PomUtil {

	public static final String GIT_VERSIONER_POM_XML = ".git-versioner.pom.xml";

	public static Path writePom(Model projectModel, Path originalPomPath) {
		Path newPomPath = originalPomPath.resolveSibling(GIT_VERSIONER_POM_XML);
		try (Writer fileWriter = Files.newBufferedWriter(newPomPath, Charset.defaultCharset())) {

			MavenXpp3Writer writer = new MavenXpp3Writer();
			writer.write(fileWriter, projectModel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return newPomPath;
	}
}
