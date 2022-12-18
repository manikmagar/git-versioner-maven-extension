/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin.mojo;

import com.github.manikmagar.maven.versioner.core.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.core.version.VersionStrategy;
import com.github.manikmagar.maven.versioner.core.version.Versioner;
import com.github.manikmagar.maven.versioner.plugin.mojo.params.VersionConfigParam;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractVersionerMojo extends AbstractMojo {

	/**
	 * Set Version Configuration
	 */
	@Parameter(name = "versionConfig")
	private VersionConfigParam _versionConfig;

	public VersionConfigParam getVersionConfig() {
		defaultConfig();
		return _versionConfig;
	}
	public void setVersionConfig(VersionConfigParam versionConfig) {
		this._versionConfig = versionConfig;
		defaultConfig();
	}

	private void defaultConfig() {
		if (this._versionConfig == null) {
			this._versionConfig = new VersionConfigParam();
		}
	}

	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject mavenProject;

	public void setMavenProject(MavenProject mavenProject) {
		this.mavenProject = mavenProject;
	}

	protected Versioner getVersioner() {
		return new JGitVersioner(getVersionConfig().toVersionConfig());
	}

	protected String replaceTokens(String pattern, VersionStrategy versionStrategy) {
		return pattern.replace("%v", versionStrategy.toVersionString());
	}
}
