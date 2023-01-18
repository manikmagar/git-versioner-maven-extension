/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import com.github.manikmagar.maven.versioner.plugin.mojo.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

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
		MojoExecutionException exception = catchThrowableOfType(() -> set.execute(), MojoExecutionException.class);
		assertThat(exception).isNotNull().hasMessage("Cannot find .git-versioner.pom.xml");
	}
}
