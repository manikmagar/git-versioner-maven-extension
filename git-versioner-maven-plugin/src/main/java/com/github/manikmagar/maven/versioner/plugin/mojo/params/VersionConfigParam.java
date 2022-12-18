package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.VersionConfig;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Objects;

public final class VersionConfigParam {

	/**
	 * Define initial version components
	 */
	@Parameter(name = "initial")
	private InitialVersionParam initial = new InitialVersionParam();

	/**
	 * Define the version keywords to use when parsing git commit messages
	 */
	@Parameter(name = "keywords")
	private VersionKeywordsParam keywords = new VersionKeywordsParam();

	public InitialVersionParam getInitial() {
		return initial;
	}

	public void setInitial(InitialVersionParam initialVersion) {
		this.initial = initialVersion;
	}

	public VersionKeywordsParam getKeywords() {
		return keywords;
	}
	public void setKeywords(VersionKeywordsParam keywords) {
		this.keywords = keywords;
	}

	public VersionConfig toVersionConfig() {
		VersionConfig versionConfig = new VersionConfig();
		versionConfig.setInitial(getInitial().toInitialVersion());
		versionConfig.setKeywords(getKeywords().toVersionKeywords());
		return versionConfig;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VersionConfigParam))
			return false;
		VersionConfigParam that = (VersionConfigParam) o;
		return getInitial().equals(that.getInitial()) && getKeywords().equals(that.getKeywords());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getInitial(), getKeywords());
	}
}
