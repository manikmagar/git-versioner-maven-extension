package com.github.manikmagar.maven.versioner.extension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.github.manikmagar.maven.versioner.extension.GitVersionerModelProcessor.DOT_MVN;
import static com.github.manikmagar.maven.versioner.extension.GitVersionerModelProcessor.GIT_VERSIONER_EXTENSIONS_PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ExtensionTestIT {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void housekeeping() {
		// IT tests do not print to standard maven logs
		// It may look like process is stuck until all tests are executed in background.
		System.out.print(".");
	}
	@AfterClass
	public static void cleanHousekeeping() {
		System.out.println(".");
		System.out.println("IT Log files are available in target/it-logs/");
	}

	@Test
	public void extensionBuildInitialVersion() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionBuildInitialVersion.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.0";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
			assertThat(tempProject.toPath().resolve(Util.GIT_VERSIONER_POM_XML).toFile()).as("Git versioner pom file")
					.exists();
		}
	}

	@Test
	public void extensionBuildWithExistingPlugin() throws Exception {
		File tempProject = temporaryFolder.newFolder("test").toPath().toFile();
		FileUtils.copyDirectory(Paths.get("src/test/resources/project-with-extension-plugin").toFile(), tempProject);
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionBuildWithExistingPlugin.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.0";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
			verifier.verifyTextInLog("Using existing plugin execution with id set-version");
			assertThat(tempProject.toPath().resolve(Util.GIT_VERSIONER_POM_XML).toFile()).as("Git versioner pom file")
					.exists();
		}
	}

	@Test
	public void extensionValidateVersionProperties() throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[patch]");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionValidateVersionProperties.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.1";
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
			verifier.verifyTextInLog("git-versioner.commitNumber=0");
			verifier.verifyTextInLog("git-versioner.major=0");
			verifier.verifyTextInLog("git-versioner.minor=0");
			verifier.verifyTextInLog("git-versioner.patch=1");
			verifier.verifyTextInLog("git-versioner.version=0.0.1");
			verifier.verifyTextInLog("git.branch=");
			verifier.verifyTextInLog("git.hash=");
			verifier.verifyTextInLog("git.hash.short=");
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
			copyExecutionLog(tempProject, verifier, "extensionBuildPatchVersion.log.txt");
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
			copyExecutionLog(tempProject, verifier, "extensionBuildMinorVersion.log.txt");
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
			copyExecutionLog(tempProject, verifier, "extensionBuildMajorVersion.log.txt");
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
			copyExecutionLog(tempProject, verifier, "extensionBuildHashVersion.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.1+" + hash.substring(0, 7);
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}

	@Test
	public void extensionWithInitialVersionProperties() throws Exception {
		File tempProject = setupTestProject(true, "1.");
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[patch]");
			String hash = addCommit(git, "new commit");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionWithProperties.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "1.3.5+" + hash.substring(0, 7);
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}
	@Test
	public void extensionWithModule() throws Exception {
		File tempProject = temporaryFolder.newFolder("test").toPath().toFile();
		FileUtils.copyDirectory(Paths.get("src/test/resources/multi-module-project").toFile(), tempProject);
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[patch]");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionWithModule.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.1";
			verifier.verifyTextInLog("Building multi-module-parent " + expectedVersion);
			verifier.verifyTextInLog("Building cli " + expectedVersion);
			verifier.verifyTextInLog("Building lib " + expectedVersion);
		}
	}
	@Test
	public void extensionWithParentChild() throws Exception {
		File tempProject = temporaryFolder.newFolder("test").toPath().toFile();
		FileUtils.copyDirectory(Paths.get("src/test/resources/parent-child-project").toFile(), tempProject);
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, "[patch]");
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionWithParentChild.log.txt");
			verifier.verifyErrorFreeLog();
			String expectedVersion = "0.0.1";
			verifier.verifyTextInLog("Building parent-test-pom " + expectedVersion);
			verifier.verifyTextInLog("Building cli " + expectedVersion);
			verifier.verifyTextInLog(
					"Setting parent com.github.manikmagar:parent-test-pom:pom:0 version to " + expectedVersion);
			verifier.verifyTextInLog("Building lib " + expectedVersion);
		}
	}

	@Test
	@Parameters(value = {"[BIG], 1.0.0", "[MEDIUM], 0.1.0", "[SMALL], 0.0.1"})
	public void extensionWithVersionKeywordProperties(String key, String expectedVersion) throws Exception {
		File tempProject = setupTestProject(true, "2.");
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			addCommit(git, key);
			Verifier verifier = new Verifier(tempProject.getAbsolutePath(), true);
			verifier.displayStreamBuffers();
			verifier.executeGoal("verify");
			copyExecutionLog(tempProject, verifier, "extensionWithVersionKeywordProperties" + key + ".log.txt");
			verifier.verifyErrorFreeLog();
			verifier.verifyTextInLog("Building versioner-maven-extension-test " + expectedVersion);
		}
	}

	private static void copyExecutionLog(File tempProject, Verifier verifier, String logName) throws IOException {
		Path target = Files.createDirectories(Paths.get("./target/it-logs/")).resolve(logName);
		if (target.toFile().exists())
			target.toFile().delete();
		Files.copy(tempProject.toPath().resolve(verifier.getLogFileName()), target);
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
		return setupTestProject(false, "");
	}
	private File setupTestProject(boolean copyProperties, String propPrefix) throws IOException {
		File tempProject = temporaryFolder.newFolder();
		Path testProject = Paths.get("src/test/resources/project-with-extension/");
		Files.copy(testProject.resolve("pom.xml"), tempProject.toPath().resolve("pom.xml"),
				StandardCopyOption.REPLACE_EXISTING);
		Path mvnDir = tempProject.toPath().resolve(DOT_MVN);
		Files.createDirectory(mvnDir);
		Files.copy(testProject.resolve(DOT_MVN).resolve("extensions.xml"), mvnDir.resolve("extensions.xml"),
				StandardCopyOption.REPLACE_EXISTING);
		if (copyProperties) {
			Files.copy(testProject.resolve(DOT_MVN).resolve(propPrefix.concat(GIT_VERSIONER_EXTENSIONS_PROPERTIES)),
					mvnDir.resolve(GIT_VERSIONER_EXTENSIONS_PROPERTIES), StandardCopyOption.REPLACE_EXISTING);
			assertThat(mvnDir.toFile().list()).contains(GIT_VERSIONER_EXTENSIONS_PROPERTIES);
		}
		assertThat(tempProject.list()).contains("pom.xml");
		assertThat(mvnDir.toFile().list()).contains("extensions.xml");
		return tempProject;
	}
}
