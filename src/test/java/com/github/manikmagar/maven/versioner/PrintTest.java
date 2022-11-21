/* (C)2022 */
package com.github.manikmagar.maven.versioner;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.manikmagar.maven.versioner.mojo.Print;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.SilentLog;
import org.junit.Rule;
import org.junit.Test;

public class PrintTest {

	public static class TestLog extends SilentLog {
		List<String> messages = new ArrayList<>();

		@Override
		public void warn(String message) {
			super.warn(message);
			messages.add(message);
		}

		public List<String> getMessages() {
			return messages;
		}
	}

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
	public void testPrint() throws Exception {
		File pom = new File("target/test-classes/project-to-test/");
		assertThat(pom).as("POM file").isNotNull().exists();

		Print printVersionMojo = (Print) rule.lookupConfiguredMojo(pom, "print");
		TestLog testLog = new TestLog();
		printVersionMojo.setLog(testLog);
		assertThat(printVersionMojo).isNotNull();
		printVersionMojo.execute();
		assertThat(testLog.getMessages())
				.allMatch(s -> s.startsWith("com.github.manikmagar.maven.versioner.Version [branch:"));
	}
}
