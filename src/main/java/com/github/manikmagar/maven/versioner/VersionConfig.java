/* (C)2022 */
package com.github.manikmagar.maven.versioner;

public class VersionConfig {
	public static final String KEY_MAJOR = "[major]";
	public static final String KEY_MINOR = "[minor]";
	public static final String KEY_PATCH = "[patch]";
	private String majorKey = KEY_MAJOR;
	private String minorKey = KEY_MINOR;
	private String patchKey = KEY_PATCH;

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
}
