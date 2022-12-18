package com.github.manikmagar.maven.versioner.extension;

public class Artifact {
	private String groupId;
	private String artifactId;
	private String version;

	public static Artifact with(String groupId, String artifactId, String version) {
		Artifact artifact = new Artifact();
		artifact.groupId = groupId;
		artifact.artifactId = artifactId;
		artifact.version = version;
		return artifact;
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
}
