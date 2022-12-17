package com.github.manikmagar.maven.versioner.core.params;

import org.apache.maven.plugins.annotations.Parameter;

public final class VersionConfig {

	/**
	 * Define initial version components
	 */
	@Parameter(name = "initial")
	private InitialVersion initial = new InitialVersion();

	/**
	 * Define the version keywords to use when parsing git commit messages
	 */
	@Parameter(name = "keywords")
	private VersionKeywords keywords = new VersionKeywords();

	public InitialVersion getInitial() {
		return initial;
	}

	public void setInitial(InitialVersion initialVersion) {
		this.initial = initialVersion;
	}

	public VersionKeywords getKeywords() {
		return keywords;
	}

	public void setKeywords(VersionKeywords keywords) {
		this.keywords = keywords;
	}
}
