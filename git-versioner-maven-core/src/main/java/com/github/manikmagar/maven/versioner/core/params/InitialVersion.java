package com.github.manikmagar.maven.versioner.core.params;

import java.util.Objects;

import com.github.manikmagar.maven.versioner.core.Util;

/**
 * Defines the initial values for version components in SemVer -
 * {major}.{minor}.{patch}
 */
public final class InitialVersion {

	public static final String GV_INITIAL_VERSION_MAJOR = "gv.initialVersion.major";
	public static final String GV_INITIAL_VERSION_MINOR = "gv.initialVersion.minor";
	public static final String GV_INITIAL_VERSION_PATCH = "gv.initialVersion.patch";

	public InitialVersion(int major, int minor, int patch) {
		setMajor(major);
		setMinor(minor);
		setPatch(patch);
	}

	public InitialVersion() {
		this(0, 0, 0);
	}

	/**
	 * Initial Major version.
	 */
	private int major = 0;
	/**
	 * Initial Minor version.
	 */
	private int minor = 0;
	/**
	 * Initial Patch version.
	 */
	private int patch = 0;

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		Util.mustBePositive(major, "InitialVersion.major");
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		Util.mustBePositive(minor, "InitialVersion.minor");
		this.minor = minor;
	}

	public int getPatch() {
		return patch;
	}

	public void setPatch(int patch) {
		Util.mustBePositive(patch, "InitialVersion.patch");
		this.patch = patch;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof InitialVersion))
			return false;
		InitialVersion that = (InitialVersion) o;
		return major == that.major && minor == that.minor && patch == that.patch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(major, minor, patch);
	}
}
