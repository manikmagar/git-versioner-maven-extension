/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin;

import java.io.File;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.twdata.maven.mojoexecutor.MojoExecutor;

import com.github.manikmagar.maven.versioner.plugin.mojo.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SetTest extends AbstractMojoTest {

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
