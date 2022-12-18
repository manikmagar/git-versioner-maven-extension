/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.VersionKeywords;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Objects;

import static com.github.manikmagar.maven.versioner.core.params.VersionKeywords.*;

/**
 * Define the version keywords to use when parsing git commit messages.
 */
public final class VersionKeywordsParam {

	/**
	 * The keyword for calculating major version of the SemVer.
	 */
	@Parameter(name = "majorKey", defaultValue = KEY_MAJOR, property = GV_KEYWORDS_MAJOR_KEY)
	private String majorKey = KEY_MAJOR;
	/**
	 * The keyword for calculating minor version of the SemVer.
	 */
	@Parameter(name = "minorKey", defaultValue = KEY_MINOR, property = GV_KEYWORDS_MINOR_KEY)
	private String minorKey = KEY_MINOR;
	/**
	 * The keyword for calculating patch version of the SemVer.
	 */
	@Parameter(name = "patchKey", defaultValue = KEY_PATCH, property = GV_KEYWORDS_PATCH_KEY)
	private String patchKey = KEY_PATCH;

	public VersionKeywordsParam(String majorKey, String minorKey, String patchKey) {
		this.majorKey = majorKey;
		this.minorKey = minorKey;
		this.patchKey = patchKey;
	}

	public VersionKeywordsParam() {

	}

	public String getMajorKey() {
		return majorKey;
	}

	public String getMinorKey() {
		return minorKey;
	}

	public String getPatchKey() {
		return patchKey;
	}

	public VersionKeywords toVersionKeywords() {
		return new VersionKeywords(getMajorKey(), getMinorKey(), getPatchKey());
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VersionKeywordsParam))
			return false;
		VersionKeywordsParam that = (VersionKeywordsParam) o;
		return getMajorKey().equals(that.getMajorKey()) && getMinorKey().equals(that.getMinorKey())
				&& getPatchKey().equals(that.getPatchKey());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getMajorKey(), getMinorKey(), getPatchKey());
	}
}
