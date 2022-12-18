package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.InitialVersion;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class InitialVersionTest {

	@Test
	public void testDefaultVersion() {
		assertThat(new InitialVersion()).isEqualTo(new InitialVersion(0, 0, 0));
	}

	@Test
	public void testMajorVersion() {
		var initialVersion = new InitialVersion();
		initialVersion.setMajor(1);
		assertThat(initialVersion).isEqualTo(new InitialVersion(1, 0, 0));
	}

	@Test
	public void testNegativeMajorVersion() {
		var initialVersion = new InitialVersion();
		var ex = catchThrowableOfType(() -> initialVersion.setMajor(-1), IllegalArgumentException.class);
		assertThat(ex).isNotNull().hasMessage("InitialVersion.major must be a positive number");
	}
	@Test
	public void testMinorVersion() {
		var initialVersion = new InitialVersion();
		initialVersion.setMinor(1);
		assertThat(initialVersion).isEqualTo(new InitialVersion(0, 1, 0));
	}

	@Test
	public void testNegativeMinorVersion() {
		var initialVersion = new InitialVersion();
		var ex = catchThrowableOfType(() -> initialVersion.setMinor(-1), IllegalArgumentException.class);
		assertThat(ex).isNotNull().hasMessage("InitialVersion.minor must be a positive number");
	}
	@Test
	public void testPatchVersion() {
		var initialVersion = new InitialVersion();
		initialVersion.setPatch(1);
		assertThat(initialVersion).isEqualTo(new InitialVersion(0, 0, 1));
	}

	@Test
	public void testNegativePatchVersion() {
		var initialVersion = new InitialVersion();
		IllegalArgumentException ex = catchThrowableOfType(() -> initialVersion.setPatch(-1),
				IllegalArgumentException.class);
		assertThat(ex).isNotNull().hasMessage("InitialVersion.patch must be a positive number");
	}

}