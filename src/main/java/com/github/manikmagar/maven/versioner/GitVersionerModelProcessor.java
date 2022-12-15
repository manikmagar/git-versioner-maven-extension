package com.github.manikmagar.maven.versioner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.building.Source;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelProcessor;
import org.apache.maven.model.building.ModelProcessor;
import org.eclipse.sisu.Typed;

import com.github.manikmagar.maven.versioner.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.mojo.params.VersionConfig;

/**
 * Maven @{@link ModelProcessor} implementation to set the project version
 * before build is initialized.
 */
@Named("core-default")
@Singleton
@Typed(ModelProcessor.class)
public class GitVersionerModelProcessor extends DefaultModelProcessor {

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
			var versioner = new JGitVersioner(new VersionConfig());
			var newVersion = versioner.version().toVersionString();
			projectModel.setVersion(newVersion);
			initialized = true;
		}
		return projectModel;
	}
}
