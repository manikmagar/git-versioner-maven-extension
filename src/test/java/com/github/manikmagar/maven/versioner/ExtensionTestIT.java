package com.github.manikmagar.maven.versioner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtensionTestIT extends AbstractMojoTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void extensionBuildInitialVersion() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.0";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}

	@Test
	public void extensionBuildPatchVersion() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[patch]");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.1";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}
	@Test
	public void extensionBuildMinorVersion() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[minor]");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.1.0";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}
	@Test
	public void extensionBuildMajorVersion() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[major]");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "1.0.0";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}
	@Test
	public void extensionBuildHashVersion() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[patch]");
			String hash = addCommit(git, "new commit");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.1+" + hash.substring(0, 7);
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}

	private static void addEmptyCommit(Git git) throws GitAPIException {
		git.commit().setSign(false).setMessage("Empty commit").setAllowEmpty(true).call();
	}
	private static String addCommit(Git git, String message) throws GitAPIException {
		RevCommit commit = git.commit().setSign(false).setMessage(message).setAllowEmpty(true).call();
		return commit.toObjectId().getName();
	}

	private static Git getMain(File tempProject) throws GitAPIException {
		return Git.init().setInitialBranch("main").setDirectory(tempProject).call();
	}

	private File setupTestProject() throws IOException {
		File tempProject = temporaryFolder.newFolder();
		Path testProject = Paths.get("src/test/resources/project-with-extension/");
		Files.copy(testProject.resolve("pom.xml"), tempProject.toPath().resolve("pom.xml"),
				StandardCopyOption.REPLACE_EXISTING);
		Path mvnDir = tempProject.toPath().resolve(".mvn");
		Files.createDirectory(mvnDir);
		Files.copy(testProject.resolve(".mvn").resolve("extensions.xml"), mvnDir.resolve("extensions.xml"),
				StandardCopyOption.REPLACE_EXISTING);
		assertThat(tempProject.list()).contains("pom.xml");
		assertThat(mvnDir.toFile().list()).contains("extensions.xml");
		return tempProject;
	}
}
