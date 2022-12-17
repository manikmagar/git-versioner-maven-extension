package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.InitialVersion;
import com.github.manikmagar.maven.versioner.core.params.VersionConfig;
import com.github.manikmagar.maven.versioner.core.params.VersionKeywords;
import org.junit.Test;

import static com.github.manikmagar.maven.versioner.core.params.VersionKeywords.*;
import static org.assertj.core.api.Assertions.assertThat;

public class VersionConfigTest {

	@Test
	public void getDefaultInitial() {
		assertThat(new VersionConfig().getInitial()).isEqualTo(new InitialVersion(0, 0, 0));
	}

	@Test
	public void setInitial() {
		VersionConfig versionConfig = new VersionConfig();
		InitialVersion initialVersion = new InitialVersion();
		initialVersion.setMajor(1);
		versionConfig.setInitial(initialVersion);
		assertThat(versionConfig.getInitial()).isEqualTo(new InitialVersion(1, 0, 0));
	}

	@Test
	public void getDefaultKeywords() {
		assertThat(new VersionConfig().getKeywords()).extracting("majorKey", "minorKey", "patchKey")
				.containsExactly(KEY_MAJOR, KEY_MINOR, KEY_PATCH);
	}

	@Test
	public void setKeywords() {
		VersionConfig versionConfig = new VersionConfig();
		VersionKeywords versionKeywords = new VersionKeywords();
		versionKeywords.setMajorKey("[TEST]");
		versionConfig.setKeywords(versionKeywords);
		assertThat(versionConfig.getKeywords()).extracting("majorKey", "minorKey", "patchKey").containsExactly("[TEST]",
				KEY_MINOR, KEY_PATCH);
	}
}