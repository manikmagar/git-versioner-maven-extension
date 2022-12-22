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
import org.apache.maven.shared.utils.logging.MessageBuilder;
import org.apache.maven.shared.utils.logging.MessageUtils;
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
			GAV extensionGAV = Util.extensionArtifact();
			LOGGER.info(MessageUtils.buffer().a("--- ").mojo(extensionGAV).a(" ").strong("[core-extension]").a(" ---")
					.toString());
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
		LOGGER.info("Project {}:{}, Computed version: {}", getGroupId(projectModel), projectModel.getArtifactId(),
				MessageUtils.buffer().strong(versionStrategy.toVersionString()));
		projectModel.setVersion(versionStrategy.toVersionString());

		Parent parent = projectModel.getParent();
		if (parent != null) {
			var path = Paths.get(parent.getRelativePath());
			// Parent is part of this build
			try {
				Path parentPomPath = projectModel.getPomFile().getParentFile().toPath().resolve(path).toRealPath();
				LOGGER.debug("Looking for parent pom {}", parentPomPath);
				if (Files.exists(parentPomPath) && this.relatedPoms.contains(parentPomPath)) {
					LOGGER.info("Setting parent {} version to {}", parent, versionStrategy.toVersionString());
					parent.setVersion(versionStrategy.toVersionString());
				} else {
					LOGGER.debug("Parent {} is not part of this build. Skipping version change for parent.", parent);
				}
			} catch (IOException e) {
				throw new GitVersionerException(e.getMessage(), e);
			}
		}
		addVersionerProperties(projectModel);
		Path versionerPom = Util.writePom(projectModel, projectModel.getPomFile().toPath());
		LOGGER.debug("Generated versioner pom at {}", versionerPom);
		// NOTE: Build plugin must be running a mojo to set .git-versioner.pom.xml as
		// project pom
		// Otherwise, the published pom will still be original pom.xml with default
		// version
		addVersionerBuildPlugin(projectModel);
	}

	private static String getGroupId(Model projectModel) {
		return (projectModel.getGroupId() == null && projectModel.getParent() != null)
				? projectModel.getParent().getGroupId()
				: projectModel.getGroupId();
	}

	private static void addVersionerBuildPlugin(Model projectModel) {
		GAV extensionGAV = Util.extensionArtifact();
		LOGGER.debug("Adding build plugin version {}", extensionGAV);
		if (projectModel.getBuild() == null) {
			projectModel.setBuild(new Build());
		}
		if (projectModel.getBuild().getPlugins() == null) {
			projectModel.getBuild().setPlugins(new ArrayList<>());
		}
		Plugin plugin = new Plugin();
		plugin.setGroupId(extensionGAV.getGroupId());
		plugin.setArtifactId(extensionGAV.getArtifactId().replace("-extension", "-plugin"));
		plugin.setVersion(extensionGAV.getVersion());
		Plugin existing = projectModel.getBuild().getPluginsAsMap().get(plugin.getKey());
		boolean addExecution = true;
		if (existing != null) {
			plugin = existing;
			LOGGER.warn("Found existing plugin configuration for {}", plugin.getKey());
			if (!existing.getVersion().equals(extensionGAV.getVersion())) {
				LOGGER.warn(MessageUtils.buffer().mojo(plugin).warning(" version is different than ").mojo(extensionGAV)
						.newline().a("This can introduce unexpected behaviors.").toString());
			}
			Optional<PluginExecution> setGoal = existing.getExecutions().stream()
					.filter(e -> e.getGoals().contains("set")).findFirst();
			if (setGoal.isPresent()) {
				LOGGER.info("Using existing plugin execution with id {}", setGoal.get().getId());
				addExecution = false;
			}
		}
		if (addExecution) {
			LOGGER.debug("Adding build plugin execution for {}", plugin.getKey());
			PluginExecution execution = new PluginExecution();
			execution.setId("extension-set");
			execution.setGoals(Collections.singletonList("set"));
			plugin.addExecution(execution);
		}
		if (existing == null)
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
			LOGGER.debug("Reading versioner properties from {}", propertiesPath);
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

	private void addVersionerProperties(Model projectModel) {
		Map<String, String> properties = new TreeMap<>();
		properties.put("git-versioner.version", versionStrategy.toVersionString());
		properties.put("git-versioner.major", String.valueOf(versionStrategy.getVersion().getMajor()));
		properties.put("git-versioner.minor", String.valueOf(versionStrategy.getVersion().getMinor()));
		properties.put("git-versioner.patch", String.valueOf(versionStrategy.getVersion().getPatch()));
		properties.put("git-versioner.commitNumber", String.valueOf(versionStrategy.getVersion().getCommit()));
		properties.put("git.branch", versionStrategy.getVersion().getBranch());
		properties.put("git.hash", versionStrategy.getVersion().getHash());
		properties.put("git.hash.short", versionStrategy.getVersion().getHashShort());
		MessageBuilder builder = MessageUtils.buffer().a("properties:");
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			builder = builder.newline();
			String key = entry.getKey();
			String value = entry.getValue();
			builder = builder.format("	%s=%s", key, value);
		}
		LOGGER.info("Adding generated properties to project model: {}", builder);
		projectModel.getProperties().putAll(properties);
	}
}
