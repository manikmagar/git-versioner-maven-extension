package com.github.manikmagar.maven.versioner.core.version;

public abstract class AbstractVersionStrategy implements VersionStrategy {

	private final String branchName;
	private String hash;

	protected AbstractVersionStrategy(String branchName, String hash) {
		this.branchName = branchName;
		this.hash = hash;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getHash() {
		return hash;
	}

	@Override
	public String getHashShort() {
		return hash != null ? hash.substring(0, 7) : null;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String strategyName() {
		return "";
	}

	@Override
	public String toString() {
		return String.format("%s [branch: %s, version: %s, hash: %s]", getClass().getSimpleName(), getBranchName(),
				toVersionString(), hash);
	}
}
