package com.github.manikmagar.maven.versioner.plugin.mojo;

import com.github.manikmagar.maven.versioner.core.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.core.version.SemVerStrategy;
import com.github.manikmagar.maven.versioner.core.version.Versioner;
import com.github.manikmagar.maven.versioner.plugin.mojo.params.InitialVersionParam;
import com.github.manikmagar.maven.versioner.plugin.mojo.params.VersionConfigParam;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import com.github.manikmagar.maven.versioner.core.params.InitialVersion;
import com.github.manikmagar.maven.versioner.core.params.VersionConfig;

import static com.github.manikmagar.maven.versioner.core.params.VersionKeywords.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AbstractVersionerMojoTest {

	private AbstractVersionerMojo testMojo = new AbstractVersionerMojo() {
		@Override
		public void execute() throws MojoExecutionException, MojoFailureException {

		}
	};

	@Test
	public void getVersionConfig() {
		assertThat(testMojo.getVersionConfig()).isNotNull();
		assertThat(testMojo.getVersionConfig().getInitial()).isNotNull().isEqualTo(new InitialVersionParam(0, 0, 0));
		assertThat(testMojo.getVersionConfig().getKeywords()).isNotNull().extracting("majorKey", "minorKey", "patchKey")
				.containsExactly(KEY_MAJOR, KEY_MINOR, KEY_PATCH);
	}

	@Test
	public void setVersionConfig() {
		AbstractVersionerMojo mojo = new AbstractVersionerMojo() {
			@Override
			public void execute() throws MojoExecutionException, MojoFailureException {

			}
		};
		InitialVersionParam one = new InitialVersionParam(1, 0, 0);
		VersionConfigParam versionConfig = new VersionConfigParam();
		versionConfig.setInitial(one);
		mojo.setVersionConfig(versionConfig);
		assertThat(mojo.getVersionConfig().getInitial()).isNotNull().isEqualTo(one);
		assertThat(testMojo.getVersionConfig().getKeywords()).isNotNull().extracting("majorKey", "minorKey", "patchKey")
				.containsExactly(KEY_MAJOR, KEY_MINOR, KEY_PATCH);
	}

	@Test
	public void getVersioner() {
		Versioner versioner = testMojo.getVersioner();
		assertThat(versioner).isNotNull().isInstanceOf(JGitVersioner.class);
	}

	@Test
	public void replaceVersionToken() {
		assertThat(testMojo.replaceTokens("v%v", new SemVerStrategy(1, 2, 3, "test", "testHash"))).isEqualTo("v1.2.3");
	}
}
