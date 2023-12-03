package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.VersionKeywords;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.github.manikmagar.maven.versioner.core.params.VersionKeywords.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class VersionKeywordsTest {

	@Test
	public void defaultKeywords() {
		assertThat(new VersionKeywords()).extracting("majorKey", "minorKey", "patchKey", "useRegex")
				.containsExactly(KEY_MAJOR, KEY_MINOR, KEY_PATCH, false);
	}

	@Test
	@Parameters(value = {"[A-MAJOR], [A-MAJOR], Custom", ",[major], Default"})
	public void setMajorKey(String key, String expected, String keyType) {
		var versionKeywords = new VersionKeywords();
		versionKeywords.setMajorKey(key);
		assertThat(versionKeywords.getMajorKey()).as(keyType).isEqualTo(expected);
	}

	@Test
	@Parameters(value = {"[A-MINOR], [A-MINOR], Custom", ",[minor], Default"})
	public void setMinorKey(String key, String expected, String keyType) {
		var versionKeywords = new VersionKeywords();
		versionKeywords.setMinorKey(key);
		assertThat(versionKeywords.getMinorKey()).as(keyType).isEqualTo(expected);
	}

	@Test
	@Parameters(value = {"[A-PATCH], [A-PATCH], Custom", ",[patch], Default"})
	public void setPatchKey(String key, String expected, String keyType) {
		var versionKeywords = new VersionKeywords();
		versionKeywords.setPatchKey(key);
		assertThat(versionKeywords.getPatchKey()).as(keyType).isEqualTo(expected);
	}

	@Test
	@Parameters(value = {"true", "false"})
	public void setUseRegex(boolean useRegex) {
		var versionKeywords = new VersionKeywords();
		versionKeywords.setUseRegex(useRegex);
		assertThat(versionKeywords.isUseRegex()).isEqualTo(useRegex);
	}
}
