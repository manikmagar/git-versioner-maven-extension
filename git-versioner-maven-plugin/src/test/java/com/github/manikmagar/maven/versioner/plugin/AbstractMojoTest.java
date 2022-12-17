package com.github.manikmagar.maven.versioner.plugin;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.junit.Rule;

import com.github.manikmagar.maven.versioner.plugin.mojo.ProjectMojoRule;

public abstract class AbstractMojoTest {

	protected MavenSession mavenSession;

	@Rule
	public MojoRule rule = new ProjectMojoRule() {
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

}
