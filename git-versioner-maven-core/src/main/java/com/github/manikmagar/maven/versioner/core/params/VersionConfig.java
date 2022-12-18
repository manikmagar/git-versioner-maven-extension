package com.github.manikmagar.maven.versioner.core.params;

import java.util.Objects;

public final class VersionConfig {

	/**
	 * Define initial version components
	 */
	private InitialVersion initial = new InitialVersion();

	/**
	 * Define the version keywords to use when parsing git commit messages
	 */
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VersionConfig))
			return false;
		VersionConfig that = (VersionConfig) o;
		return getInitial().equals(that.getInitial()) && getKeywords().equals(that.getKeywords());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getInitial(), getKeywords());
	}
}
