package com.github.manikmagar.maven.versioner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.building.Source;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelProcessor;
import org.apache.maven.model.building.ModelProcessor;
import org.eclipse.sisu.Typed;

import com.github.manikmagar.maven.versioner.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.mojo.params.InitialVersion;
import com.github.manikmagar.maven.versioner.mojo.params.VersionConfig;
import com.github.manikmagar.maven.versioner.mojo.params.VersionKeywords;

/**
 * Maven @{@link ModelProcessor} implementation to set the project version
 * before build is initialized.
 */
@Named("core-default")
@Singleton
@Typed(ModelProcessor.class)
public class GitVersionerModelProcessor extends DefaultModelProcessor {

	public static final String GIT_VERSIONER_EXTENSIONS_PROPERTIES = "git-versioner.extensions.properties";
	public static final String DOT_MVN = ".mvn";
	private boolean initialized = false;

	@Override
	public Model read(File input, Map<String, ?> options) throws IOException {
		return processModel(super.read(input, options), options);
	}

	@Override
	public Model read(Reader input, Map<String, ?> options) throws IOException {
		return processModel(super.read(input, options), options);
	}

	@Override
	public Model read(InputStream input, Map<String, ?> options) throws IOException {
		return processModel(super.read(input, options), options);
	}

	private Model processModel(Model projectModel, Map<String, ?> options) {
		final Source pomSource = (Source) options.get(ModelProcessor.SOURCE);

		if (pomSource != null) {
			projectModel.setPomFile(new File(pomSource.getLocation()));
		} else {
			return projectModel;
		}

		// This model processor is invoked for every POM on the classpath, including the
		// plugins.
		// The first execution is with the project's pom though.
		// Use first initialized flag to avoid processing other classpath poms.
		if (!initialized) {
			// TODO: Define extension configuration model.
			var versioner = new JGitVersioner(loadConfig());
			var newVersion = versioner.version().toVersionString();
			projectModel.setVersion(newVersion);
			initialized = true;
		}
		return projectModel;
	}

	private VersionConfig loadConfig() {
		VersionConfig versionConfig = new VersionConfig();
		Properties properties = loadExtensionProperties();
		InitialVersion iv = new InitialVersion();
		iv.setMajor(Integer.parseInt(properties.getProperty(InitialVersion.GV_INITIAL_VERSION_MAJOR, "0")));
		iv.setMinor(Integer.parseInt(properties.getProperty(InitialVersion.GV_INITIAL_VERSION_MINOR, "0")));
		iv.setPatch(Integer.parseInt(properties.getProperty(InitialVersion.GV_INITIAL_VERSION_PATCH, "0")));
		versionConfig.setInitial(iv);

		VersionKeywords vk = new VersionKeywords();
		vk.setMajorKey(properties.getProperty(VersionKeywords.GV_KEYWORDS_MAJOR_KEY));
		vk.setMinorKey(properties.getProperty(VersionKeywords.GV_KEYWORDS_MINOR_KEY));
		vk.setPatchKey(properties.getProperty(VersionKeywords.GV_KEYWORDS_PATCH_KEY));
		versionConfig.setKeywords(vk);

		return versionConfig;
	}
	private Properties loadExtensionProperties() {
		Properties props = new Properties();
		Path propertiesPath = Paths.get(DOT_MVN, GIT_VERSIONER_EXTENSIONS_PROPERTIES);
		if (propertiesPath.toFile().exists()) {
			try (Reader reader = Files.newBufferedReader(propertiesPath)) {
				props.load(reader);
			} catch (IOException e) {
				throw new GitVersionerException("Failed to load extensions properties file", e);
			}
		}
		return props;
	}
}
