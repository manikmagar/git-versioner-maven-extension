/* (C)2022 */
package com.github.manikmagar.maven.versioner.mojo;

import com.github.manikmagar.maven.versioner.Version;
import com.github.manikmagar.maven.versioner.VersionConfig;
import com.github.manikmagar.maven.versioner.git.JGitVersioner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * <pre>
 * Generate a version from git commit history and exposes it to maven project properties.
 * Execution of this mojo will make following properties accessible during the build execution:
 *   - git-versioner.semver
 *   - git-versioner.major
 *   - git-versioner.minor
 *   - git-versioner.patch
 *   - git-versioner.commit
 * </pre>
 */
@Mojo(name = "properties", defaultPhase = LifecyclePhase.VALIDATE)
public class Properties extends AbstractVersionerMojo {

	/** The project currently being build. */
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject mavenProject;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Version version = new JGitVersioner(new VersionConfig()).version();
		mavenProject.getProperties().put("git-versioner.semver", version.toSemver());
		mavenProject.getProperties().put("git-versioner.major", version.getMajor());
		mavenProject.getProperties().put("git-versioner.minor", version.getMinor());
		mavenProject.getProperties().put("git-versioner.patch", version.getPatch());
		mavenProject.getProperties().put("git-versioner.commitNumber", version.getCommit());
	}
}
