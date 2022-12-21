package com.github.manikmagar.maven.versioner.extension;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.github.manikmagar.maven.versioner.core.GitVersionerException;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class Util {

	private Util() {
	}

	public static final String GIT_VERSIONER_POM_XML = ".git-versioner.pom.xml";

	public static final String GIT_VERSIONER_MAVEN_CORE_PROPERTIES = "git-versioner-maven-core.properties";
	public static final String GIT_VERSIONER_MAVEN_EXTENSION_PROPERTIES = "git-versioner-maven-extension.properties";
	public static final String GIT_VERSIONER_MAVEN_PLUGIN_PROPERTIES = "git-versioner-maven-plugin.properties";

	public static Path writePom(Model projectModel, Path originalPomPath) {
		Path newPomPath = originalPomPath.resolveSibling(GIT_VERSIONER_POM_XML);
		try (Writer fileWriter = Files.newBufferedWriter(newPomPath, Charset.defaultCharset())) {
			MavenXpp3Writer writer = new MavenXpp3Writer();
			writer.write(fileWriter, projectModel);
		} catch (IOException e) {
			throw new GitVersionerException(e.getMessage(), e);
		}
		return newPomPath;
	}
	public static Model readPom(Path pomPath) {
		try (InputStream inputStream = Files.newInputStream(pomPath)) {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			return reader.read(inputStream);
		} catch (IOException | XmlPullParserException e) {
			throw new GitVersionerException(e.getMessage(), e);
		}
	}
	public static GAV extensionArtifact() {
		Properties props = new Properties();
		try (InputStream inputStream = Util.class.getClassLoader().getResource(GIT_VERSIONER_MAVEN_EXTENSION_PROPERTIES)
				.openStream()) {
			props.load(inputStream);
			return GAV.with(props.getProperty("projectGroupId"), props.getProperty("projectArtifactId"),
					props.getProperty("projectVersion"));
		} catch (Exception e) {
			throw new GitVersionerException(e.getMessage(), e);
		}
	}
}
