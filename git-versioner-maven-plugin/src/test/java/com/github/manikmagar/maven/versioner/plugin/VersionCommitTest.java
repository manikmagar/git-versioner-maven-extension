package com.github.manikmagar.maven.versioner.plugin;

import com.github.manikmagar.maven.versioner.plugin.mojo.VersionCommit;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class VersionCommitTest extends AbstractMojoTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	@Parameters(value = {"commit-patch, patch", "commit-minor, minor", "commit-major, major"})
	public void executeVersionCommit(String goal, String keyword) throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			var commit = (VersionCommit) rule.lookupConfiguredMojo(tempProject, goal);
			assertThat(commit).isNotNull();
			commit.execute();
			var commits = git.log().call();
			var revCommits = StreamSupport.stream(commits.spliterator(), false).collect(Collectors.toList());
			assertThat(revCommits.get(0).getShortMessage()).isEqualTo(String.format("chore(release): [%s]", keyword));
		}
	}

	@Test
	@Parameters(value = {"commit-patch, patch", "commit-minor, minor", "commit-major, major"})
	public void executeVersionCommitCustomMessage(String goal, String keyword) throws Exception {
		File tempProject = setupTestProject();
		try (Git git = getMain(tempProject)) {
			addEmptyCommit(git);
			var commit = (VersionCommit) rule.lookupConfiguredMojo(tempProject, goal);
			commit.setMessage("chore: releasing [%k]");
			assertThat(commit).isNotNull();
			commit.execute();
			var commits = git.log().call();
			var revCommits = StreamSupport.stream(commits.spliterator(), false).collect(Collectors.toList());
			assertThat(revCommits.get(0).getShortMessage()).isEqualTo(String.format("chore: releasing [%s]", keyword));
		}
	}

	private File setupTestProject() throws IOException {
		File tempProject = temporaryFolder.newFolder();
		Path testProject = Paths.get("src/test/resources/project-to-test/");
		Files.copy(testProject.resolve("pom.xml"), tempProject.toPath().resolve("pom.xml"),
				StandardCopyOption.REPLACE_EXISTING);
		assertThat(tempProject.list()).contains("pom.xml");
		return tempProject;
	}
	private static Git getMain(File tempProject) throws GitAPIException {
		return Git.init().setInitialBranch("main").setDirectory(tempProject).call();
	}
	private static void addEmptyCommit(Git git) throws GitAPIException {
		git.commit().setSign(false).setMessage("Empty commit").setAllowEmpty(true).call();
	}
	private static String addCommit(Git git, String message) throws GitAPIException {
		RevCommit commit = git.commit().setSign(false).setMessage(message).setAllowEmpty(true).call();
		return commit.toObjectId().getName();
	}

}
