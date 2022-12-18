package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.VersionConfig;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionConfigParamTest {

	@Test
	public void toVersionConfig() {
		assertThat(new VersionConfigParam()).extracting(VersionConfigParam::toVersionConfig)
				.isEqualTo(new VersionConfig());
	}
}