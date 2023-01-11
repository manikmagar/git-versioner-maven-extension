/* (C)2022 */
package com.github.manikmagar.maven.versioner.core.version;

import java.util.Objects;

public class Version {
	private final int major;
	private final int minor;
	private final int patch;
	private int commit;
	private final String branch;
	private final String hash;

	public Version(String branch, String hash) {
		this(branch, hash, 0, 0, 0, 0);
	}

	public Version(String branch, String hash, int major, int minor, int patch, int commit) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.branch = branch;
		this.hash = hash;
		this.commit = commit;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getPatch() {
		return patch;
	}

	public int getCommit() {
		return commit;
	}

	public String getBranch() {
		return branch;
	}

	public String getHash() {
		return hash;
	}
	public String getHashShort() {
		return hash != null ? hash.substring(0, 7) : null;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Version))
			return false;
		Version version = (Version) o;
		return getMajor() == version.getMajor() && getMinor() == version.getMinor() && getPatch() == version.getPatch();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getMajor(), getMinor(), getPatch());
	}
}
