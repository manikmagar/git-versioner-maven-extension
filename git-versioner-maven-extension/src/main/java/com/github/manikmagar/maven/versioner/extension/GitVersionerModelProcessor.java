package com.github.manikmagar.maven.versioner.extension;

import com.github.manikmagar.maven.versioner.core.GitVersionerException;
import com.github.manikmagar.maven.versioner.core.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.core.params.InitialVersion;
import com.github.manikmagar.maven.versioner.core.params.VersionConfig;
import com.github.manikmagar.maven.versioner.core.params.VersionKeywords;
import com.github.manikmagar.maven.versioner.core.version.VersionStrategy;
import org.apache.maven.building.Source;
import org.apache.maven.model.*;
import org.apache.maven.model.building.DefaultModelProcessor;
import org.apache.maven.model.building.ModelProcessor;
import org.eclipse.sisu.Typed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Maven @{@link ModelProcessor} implementation to set the project version
 * before build is initialized.
 */
@Named("core-default")
@Singleton
@Typed(ModelProcessor.class)
public class GitVersionerModelProcessor extends DefaultModelProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(GitVersionerModelProcessor.class);

	public static final String GIT_VERSIONER_EXTENSIONS_PROPERTIES = "git-versioner.extensions.properties";
	public static final String DOT_MVN = ".mvn";
	private boolean initialized = false;

	private final List<Path> relatedPoms = new ArrayList<>();
	private VersionStrategy versionStrategy;

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
			versionStrategy = new JGitVersioner(loadConfig()).version();
			findRelatedProjects(projectModel);
			initialized = true;
		}
		processRelatedProjects(projectModel);
		return projectModel;
	}

	private void processRelatedProjects(Model projectModel) {
		if (!relatedPoms.contains(projectModel.getPomFile().toPath()))
			return;

		projectModel.setVersion(versionStrategy.toVersionString());

		Parent parent = projectModel.getParent();
		if (parent != null) {
			parent.setVersion(versionStrategy.toVersionString());
		}

		Util.writePom(projectModel, projectModel.getPomFile().toPath());
		// NOTE: Build plugin must be running a mojo to set .git-versioner.pom.xml as
		// project pom
		// Otherwise, the published pom will still be original pom.xml with default
		// version
		addVersionerBuildPlugin(projectModel);
	}

	private static void addVersionerBuildPlugin(Model projectModel) {
		Artifact extensionArtifact = Util.extensionArtifact();
		if (projectModel.getBuild() == null) {
			projectModel.setBuild(new Build());
		}
		if (projectModel.getBuild().getPlugins() == null) {
			projectModel.getBuild().setPlugins(new ArrayList<>());
		}

		Plugin plugin = new Plugin();
		plugin.setGroupId(extensionArtifact.getGroupId());
		plugin.setArtifactId(extensionArtifact.getArtifactId().replace("-extension", "-plugin"));
		plugin.setVersion(extensionArtifact.getVersion());

		PluginExecution execution = new PluginExecution();
		execution.setId("extension-set");
		execution.setGoals(Collections.singletonList("set"));
		plugin.addExecution(execution);
		projectModel.getBuild().getPlugins().add(0, plugin);
	}

	private VersionConfig loadConfig() {
		VersionConfig versionConfig = new VersionConfig();
		Properties properties = loadExtensionProperties();
		InitialVersion version = new InitialVersion();
		version.setMajor(Integer.parseInt(properties.getProperty(InitialVersion.GV_INITIAL_VERSION_MAJOR, "0")));
		version.setMinor(Integer.parseInt(properties.getProperty(InitialVersion.GV_INITIAL_VERSION_MINOR, "0")));
		version.setPatch(Integer.parseInt(properties.getProperty(InitialVersion.GV_INITIAL_VERSION_PATCH, "0")));
		versionConfig.setInitial(version);

		VersionKeywords keywords = new VersionKeywords();
		keywords.setMajorKey(properties.getProperty(VersionKeywords.GV_KEYWORDS_MAJOR_KEY));
		keywords.setMinorKey(properties.getProperty(VersionKeywords.GV_KEYWORDS_MINOR_KEY));
		keywords.setPatchKey(properties.getProperty(VersionKeywords.GV_KEYWORDS_PATCH_KEY));
		versionConfig.setKeywords(keywords);

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

	private void findRelatedProjects(Model projectModel) {
		LOGGER.debug("Finding related projects for {}", projectModel.getArtifactId());

		// Add main project
		relatedPoms.add(projectModel.getPomFile().toPath());

		// Find modules
		List<Path> modulePoms = projectModel.getModules().stream().map(module -> projectModel.getProjectDirectory()
				.toPath().resolve(module).resolve("pom.xml").toAbsolutePath()).collect(Collectors.toList());
		LOGGER.debug("Modules found: {}", modulePoms);
		relatedPoms.addAll(modulePoms);
	}
}
