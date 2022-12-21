package com.github.manikmagar.maven.versioner.extension;

import org.apache.maven.model.Model;

import java.util.Objects;

public class GAV {
	private String groupId;
	private String artifactId;
	private String version;

	private GAV() {

	}
	public static GAV with(String groupId, String artifactId, String version) {
		Objects.requireNonNull(groupId, "groupId must not be null");
		Objects.requireNonNull(artifactId, "artifactId must not be null");
		Objects.requireNonNull(version, "version must not be null");
		GAV gav = new GAV();
		gav.groupId = groupId;
		gav.artifactId = artifactId;
		gav.version = version;
		return gav;
	}

	public static GAV of(Model model) {
		return GAV.with(model.getGroupId(), model.getArtifactId(), model.getVersion());
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GAV))
			return false;
		GAV gav = (GAV) o;
		return getGroupId().equals(gav.getGroupId()) && getArtifactId().equals(gav.getArtifactId())
				&& getVersion().equals(gav.getVersion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getGroupId(), getArtifactId(), getVersion());
	}

	@Override
	public String toString() {
		return String.format("%s:%s:%s", getGroupId(), getArtifactId(), getVersion());
	}
}
