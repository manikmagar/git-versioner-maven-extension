/* (C)2022 */
package com.github.manikmagar.maven.versioner;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manikmagar.maven.versioner.mojo.Properties;
import java.io.File;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.junit.Rule;
import org.junit.Test;

public class PropertiesTest {

	private MavenSession mavenSession;

	@Rule
	public MojoRule rule = new MojoRule() {
		@Override
		protected void before() throws Throwable {
		}

		@Override
		protected void after() {
		}

		@Override
		public Mojo lookupConfiguredMojo(MavenSession session, MojoExecution execution)
				throws Exception, ComponentConfigurationException {
			mavenSession = session;
			return super.lookupConfiguredMojo(session, execution);
		}
	};

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
