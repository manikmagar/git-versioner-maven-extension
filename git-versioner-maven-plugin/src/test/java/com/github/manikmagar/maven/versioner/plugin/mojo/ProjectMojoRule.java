package com.github.manikmagar.maven.versioner.plugin.mojo;

import java.io.File;
import java.io.FileReader;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;

public class ProjectMojoRule extends MojoRule {

	@Override
	public MavenProject readMavenProject(File basedir) throws Exception {
		// Manual project instantiation is to avoid
		// Invalid repository system session: Local Repository Manager is not set.
		// when using default implementation.
		File pom = new File(basedir, "pom.xml");
		MavenXpp3Reader mavenReader = new MavenXpp3Reader();
		try (FileReader reader = new FileReader(pom)) {
			Model model = mavenReader.read(reader);
			model.setPomFile(pom);
			return new MavenProject(model);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't read pom: " + pom);
		}
	}

}
