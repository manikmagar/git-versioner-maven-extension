package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.InitialVersion;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Objects;

import static com.github.manikmagar.maven.versioner.core.params.InitialVersion.*;

public class InitialVersionParam {

	/**
	 * Initial Major version.
	 */
	@Parameter(name = "major", defaultValue = "0", property = GV_INITIAL_VERSION_MAJOR)
	private int major = 0;
	/**
	 * Initial Minor version.
	 */
	@Parameter(name = "minor", defaultValue = "0", property = GV_INITIAL_VERSION_MINOR)
	private int minor = 0;
	/**
	 * Initial Patch version.
	 */
	@Parameter(name = "patch", defaultValue = "0", property = GV_INITIAL_VERSION_PATCH)
	private int patch = 0;

	public InitialVersionParam(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public InitialVersionParam() {
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

	public InitialVersion toInitialVersion() {
		return new InitialVersion(getMajor(), getMinor(), getPatch());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof InitialVersionParam))
			return false;
		InitialVersionParam that = (InitialVersionParam) o;
		return getMajor() == that.getMajor() && getMinor() == that.getMinor() && getPatch() == that.getPatch();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getMajor(), getMinor(), getPatch());
	}
}
