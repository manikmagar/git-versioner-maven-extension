/* (C)2022 */
package com.github.manikmagar.maven.versioner;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manikmagar.maven.versioner.mojo.Set;
import java.io.File;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.twdata.maven.mojoexecutor.MojoExecutor;

public class SetTest {

	@Rule
	public MojoRule rule = new MojoRule() {
		@Override
		protected void before() throws Throwable {
		}

		@Override
		protected void after() {
		}
	};

	/**
	 * @throws Exception
	 *             if any
	 */
	@Test
	public void testSetVersion() throws Exception {
		File pom = new File("target/test-classes/project-to-test/");
		assertThat(pom).as("POM file").isNotNull().exists();

		Set set = (Set) rule.lookupConfiguredMojo(pom, "set");
		assertThat(set).isNotNull();
		try (MockedStatic<MojoExecutor> executorMockedStatic = Mockito.mockStatic(MojoExecutor.class)) {
			executorMockedStatic
					.when(() -> MojoExecutor.executeMojo(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
					.then(Answers.RETURNS_DEFAULTS);
			set.execute();
		}
	}
}
