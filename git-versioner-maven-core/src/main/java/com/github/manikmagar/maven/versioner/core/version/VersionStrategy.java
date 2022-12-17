package com.github.manikmagar.maven.versioner.core.version;

/**
 * Common interface for versioning strategies.
 */
public interface VersionStrategy {
	String strategyName();
	String toVersionString();

	/**
	 * Increments the version component
	 * 
	 * @param type
	 *            {@link VersionComponentType} to increment
	 * @param hashRef
	 *            {@link String} hash representing the change log
	 */
	void increment(VersionComponentType type, String hashRef);
	String getHash();
	String getHashShort();
	String getBranchName();
	Version getVersion();

}
