/* (C)2022 */
package com.github.manikmagar.maven.versioner.mojo;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

import com.github.manikmagar.maven.versioner.version.VersionStrategy;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/** Generate a version from git commit history and set it in the project pom. */
@Mojo(name = "set", defaultPhase = LifecyclePhase.INITIALIZE)
public class Set extends AbstractVersionerMojo {
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject mavenProject;

	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession mavenSession;

	@Component
	private BuildPluginManager pluginManager;

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
	}
}
