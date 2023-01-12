/* (C)2022 */
package com.github.manikmagar.maven.versioner.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.testing.SilentLog;
import org.junit.Test;

import com.github.manikmagar.maven.versioner.plugin.mojo.Print;

import static org.assertj.core.api.Assertions.assertThat;

public class PrintTest extends AbstractMojoTest {

	public static class TestLog extends SilentLog {
		List<String> messages = new ArrayList<>();

		@Override
		public void warn(String message) {
			super.warn(message);
			messages.add(message);
		}

		@Override
		public boolean isInfoEnabled() {
			return true;
		}

		@Override
		public void info(CharSequence content) {
			super.info(content);
			messages.add(content.toString());
		}

		public List<String> getMessages() {
			return messages;
		}
	}

	/**
	 * @throws Exception
	 *             if any
	 */
	@Test
	public void testPrint() throws Exception {
		File pom = new File("target/test-classes/project-to-test/");
		assertThat(pom).as("POM file").isNotNull().exists();

		Print printVersionMojo = (Print) rule.lookupConfiguredMojo(pom, "print");
		TestLog testLog = new TestLog();
		printVersionMojo.setLog(testLog);
		assertThat(printVersionMojo).isNotNull();
		printVersionMojo.execute();
		assertThat(testLog.getMessages()).isNotEmpty().allMatch(s -> s.startsWith("VersionPatternStrategy [branch:"));
	}
}
