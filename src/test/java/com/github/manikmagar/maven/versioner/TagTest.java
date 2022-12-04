/* (C)2022 */
package com.github.manikmagar.maven.versioner;

import com.github.manikmagar.maven.versioner.mojo.Tag;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class TagTest {

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
	public void readDefaultParameters() throws Exception {
		File pom = new File("target/test-classes/project-to-test/");
		assertThat(pom).as("POM file").isNotNull().exists();
		Tag tag = (Tag) rule.lookupConfiguredMojo(pom, "tag");
		assertThat(tag).isNotNull();
		assertThat(tag.getTagNamePattern()).isEqualTo("v%v");
		assertThat(tag.getTagMessagePattern()).isEqualTo("Release version %v");
		assertThat(tag.isFailWhenTagExist()).isTrue();
	}

	@Test
	public void readPropertiesParameters() throws Exception {
		File pom = new File("target/test-classes/project-to-tag/");
		assertThat(pom).as("POM file").isNotNull().exists();
		Tag tag = (Tag) rule.lookupConfiguredMojo(pom, "tag");
		assertThat(tag).isNotNull();
		assertThat(tag.getTagNamePattern()).isEqualTo("version-%v");
		assertThat(tag.getTagMessagePattern()).isEqualTo("Release message %v");
		assertThat(tag.isFailWhenTagExist()).isFalse();
	}
}
