package com.github.manikmagar.maven.versioner.mojo.params;

import com.github.manikmagar.maven.versioner.Util;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Objects;

/**
 * Defines the initial values for version components in SemVer -
 * {major}.{minor}.{patch}
 */
public final class InitialVersion {

	public InitialVersion(int major, int minor, int patch) {
		this._major = major;
		this._minor = minor;
		this._patch = patch;
	}

	public InitialVersion() {
		this(0, 0, 0);
	}

	/**
	 * Initial Major version.
	 */
	@Parameter(name = "major", defaultValue = "0")
	private int _major = 0;
	/**
	 * Initial Minor version.
	 */
	@Parameter(name = "minor", defaultValue = "0")
	private int _minor = 0;
	/**
	 * Initial Patch version.
	 */
	@Parameter(name = "patch", defaultValue = "0")
	private int _patch = 0;

	public int getMajor() {
		return _major;
	}

	public void setMajor(int major) {
		Util.mustBePositive(major, "InitialVersion.major");
		this._major = major;
	}

	public int getMinor() {
		return _minor;
	}

	public void setMinor(int minor) {
		Util.mustBePositive(minor, "InitialVersion.minor");
		this._minor = minor;
	}

	public int getPatch() {
		return _patch;
	}

	public void setPatch(int patch) {
		Util.mustBePositive(patch, "InitialVersion.patch");
		this._patch = patch;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof InitialVersion))
			return false;
		InitialVersion that = (InitialVersion) o;
		return _major == that._major && _minor == that._minor && _patch == that._patch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_major, _minor, _patch);
	}
}
