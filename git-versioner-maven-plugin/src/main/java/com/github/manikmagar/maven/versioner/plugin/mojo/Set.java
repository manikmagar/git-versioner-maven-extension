/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;

/** Generate a version from git commit history and set it in the project pom. */
@Mojo(name = "set", defaultPhase = LifecyclePhase.INITIALIZE)
public class Set extends AbstractVersionerMojo {

	@Override
	public void execute() throws MojoExecutionException {
		File gitVersionerPom = mavenProject.getBasedir().toPath().resolve(".git-versioner.pom.xml").toFile();
		if (gitVersionerPom.exists()) {
			// Extension is in-force, user the generated pom
			mavenProject.setPomFile(gitVersionerPom);
		} else {
			throw new MojoExecutionException("Cannot find .git-versioner.pom.xml");
		}
	}
}
