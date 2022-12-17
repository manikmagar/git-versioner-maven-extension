/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin;

import java.io.File;

import org.junit.Test;

import com.github.manikmagar.maven.versioner.plugin.mojo.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesTest extends AbstractMojoTest {

	/**
	 * @throws Exception
	 *             if any
	 */
	@Test
	public void testProperties() throws Exception {
		File pom = new File("target/test-classes/project-to-test/");
		assertThat(pom).as("POM file").isNotNull().exists();

		Properties properties = (Properties) rule.lookupConfiguredMojo(pom, "properties");
		assertThat(properties).isNotNull();
		properties.execute();

		java.util.Properties props = mavenSession.getCurrentProject().getProperties();
		assertThat(props).as("Maven Project properties").containsKeys("git-versioner.semver", "git-versioner.major",
				"git-versioner.minor", "git-versioner.patch", "git-versioner.commitNumber");
	}
}
