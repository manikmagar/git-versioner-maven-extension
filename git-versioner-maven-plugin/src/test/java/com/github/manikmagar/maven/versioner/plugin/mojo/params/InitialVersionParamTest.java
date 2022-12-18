package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.InitialVersion;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class InitialVersionParamTest {

	@Test
	public void toInitialVersion() {
		Assertions.assertThat(new InitialVersionParam(1, 2, 3)).extracting(InitialVersionParam::toInitialVersion)
				.isEqualTo(new InitialVersion(1, 2, 3));
	}

	@Test
	public void createNewVersionParam() {
		Assertions.assertThat(new InitialVersionParam(1, 2, 3)).extracting("major", "minor", "patch").containsExactly(1,
				2, 3);
	}
}