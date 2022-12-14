/* (C)2022 */
package com.github.manikmagar.maven.versioner.mojo;

import com.github.manikmagar.maven.versioner.version.VersionStrategy;
import com.github.manikmagar.maven.versioner.version.Versioner;
import com.github.manikmagar.maven.versioner.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.mojo.params.VersionConfig;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class AbstractVersionerMojo extends AbstractMojo {

	/**
	 * Set Version Configuration
	 */
	@Parameter(name = "versionConfig")
	private VersionConfig _versionConfig;

	public VersionConfig getVersionConfig() {
		defaultConfig();
		return _versionConfig;
	}
	public void setVersionConfig(VersionConfig versionConfig) {
		this._versionConfig = versionConfig;
		defaultConfig();
	}

	private void defaultConfig() {
		if (this._versionConfig == null) {
			this._versionConfig = new VersionConfig();
		}
	}

	protected Versioner getVersioner() {
		return new JGitVersioner(getVersionConfig());
	}

	protected String replaceTokens(String pattern, VersionStrategy versionStrategy) {
		return pattern.replace("%v", versionStrategy.toVersionString());
	}
}
