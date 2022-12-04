package com.github.manikmagar.maven.versioner.mojo;

import com.github.manikmagar.maven.versioner.Version;
import com.github.manikmagar.maven.versioner.Versioner;
import com.github.manikmagar.maven.versioner.git.JGitVersioner;
import com.github.manikmagar.maven.versioner.mojo.params.InitialVersion;
import com.github.manikmagar.maven.versioner.mojo.params.VersionConfig;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import static com.github.manikmagar.maven.versioner.mojo.params.VersionKeywords.*;
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
		assertThat(testMojo.getVersionConfig().getInitial()).isNotNull().isEqualTo(new InitialVersion(0, 0, 0));
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
		InitialVersion one = new InitialVersion(1, 0, 0);
		VersionConfig versionConfig = new VersionConfig();
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
		assertThat(testMojo.replaceTokens("v%v", new Version("test", "testHash", 1, 2, 3))).isEqualTo("v1.2.3");
	}
}