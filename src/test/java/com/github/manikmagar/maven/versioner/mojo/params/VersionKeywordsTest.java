package com.github.manikmagar.maven.versioner.mojo.params;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.github.manikmagar.maven.versioner.mojo.params.VersionKeywords.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class VersionKeywordsTest {

	@Test
	public void defaultKeywords() {
		assertThat(new VersionKeywords()).extracting("majorKey", "minorKey", "patchKey").containsExactly(KEY_MAJOR,
				KEY_MINOR, KEY_PATCH);
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

}
