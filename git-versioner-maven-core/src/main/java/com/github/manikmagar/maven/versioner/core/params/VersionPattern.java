package com.github.manikmagar.maven.versioner.core.params;

import java.util.Objects;

public class VersionPattern {

	public static final String GV_PATTERN_PATTERN = "gv.pattern.pattern";
	private String pattern = "";
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VersionPattern))
			return false;
		VersionPattern that = (VersionPattern) o;
		return Objects.equals(getPattern(), that.getPattern());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPattern());
	}
}
