/* (C)2022 */
package com.github.manikmagar.maven.versioner.mojo;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.manikmagar.maven.versioner.version.VersionStrategy;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/** Generate a version from git commit history and set it in the project pom. */
@Mojo(name = "set", defaultPhase = LifecyclePhase.INITIALIZE)
public class Set extends AbstractVersionerMojo {

	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession mavenSession;

	@Component
	private BuildPluginManager pluginManager;

	public void setMavenSession(MavenSession mavenSession) {
		this.mavenSession = mavenSession;
	}

	public void setPluginManager(BuildPluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public void execute() throws MojoExecutionException {
		VersionStrategy version = getVersioner().version();
		if (version.getHash().isEmpty()) {
			getLog().warn(String.format("No HEAD ref found on branch %s.", version.getBranchName()));
		}
		executeMojo(plugin(groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.13.0")),
				goal("set"), configuration(element(name("newVersion"), version.toVersionString())),
				executionEnvironment(mavenProject, mavenSession, pluginManager));
		executeMojo(plugin(groupId("org.codehaus.mojo"), artifactId("versions-maven-plugin"), version("2.13.0")),
				goal("commit"), configuration(), executionEnvironment(mavenProject, mavenSession, pluginManager));
		mavenProject.setPomFile(new File(mavenProject.getBasedir(), "pom.xml"));
	}
}
