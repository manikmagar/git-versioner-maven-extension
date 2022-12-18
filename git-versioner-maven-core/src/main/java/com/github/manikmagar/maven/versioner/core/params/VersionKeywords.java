/* (C)2022 */
package com.github.manikmagar.maven.versioner.core.params;

import java.util.Objects;

/**
 * Define the version keywords to use when parsing git commit messages.
 */
public final class VersionKeywords {
	public static final String KEY_MAJOR = "[major]";
	public static final String KEY_MINOR = "[minor]";
	public static final String KEY_PATCH = "[patch]";
	public static final String GV_KEYWORDS_MAJOR_KEY = "gv.keywords.majorKey";
	public static final String GV_KEYWORDS_MINOR_KEY = "gv.keywords.minorKey";
	public static final String GV_KEYWORDS_PATCH_KEY = "gv.keywords.patchKey";

	/**
	 * The keyword for calculating major version of the SemVer.
	 */
	private String majorKey = KEY_MAJOR;
	/**
	 * The keyword for calculating minor version of the SemVer.
	 */
	private String minorKey = KEY_MINOR;
	/**
	 * The keyword for calculating patch version of the SemVer.
	 */
	private String patchKey = KEY_PATCH;

	public VersionKeywords(String majorKey, String minorKey, String patchKey) {
		setMajorKey(majorKey);
		setMinorKey(minorKey);
		setPatchKey(patchKey);
	}
	public VersionKeywords() {

	}

	public String getMajorKey() {
		return majorKey;
	}

	public void setMajorKey(String majorKey) {
		if (majorKey == null || majorKey.trim().isEmpty()) {
			this.majorKey = KEY_MAJOR;
		} else {
			this.majorKey = majorKey;
		}
	}

	public String getMinorKey() {
		return minorKey;
	}

	public void setMinorKey(String minorKey) {
		if (minorKey == null || minorKey.trim().isEmpty()) {
			this.minorKey = KEY_MINOR;
		} else {
			this.minorKey = minorKey;
		}
	}

	public String getPatchKey() {
		return patchKey;
	}

	public void setPatchKey(String patchKey) {
		if (patchKey == null || patchKey.trim().isEmpty()) {
			this.patchKey = KEY_PATCH;
		} else {
			this.patchKey = patchKey;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VersionKeywords))
			return false;
		VersionKeywords that = (VersionKeywords) o;
		return majorKey.equals(that.majorKey) && minorKey.equals(that.minorKey) && patchKey.equals(that.patchKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(majorKey, minorKey, patchKey);
	}
}
