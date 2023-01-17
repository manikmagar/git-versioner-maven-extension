package com.github.manikmagar.maven.versioner.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Add a new commit to change version
 */
public abstract class VersionCommit extends AbstractVersionerMojo {

	public static final String KEYWORD_TOKEN = "[%k]";

	public enum IncrementType {
		MAJOR("major"), MINOR("minor"), PATCH("patch");
		private final String name;
		IncrementType(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}

	@Parameter(name = "message", property = "gv.commit.message", defaultValue = "chore(release): " + KEYWORD_TOKEN)
	private String message;

	public void setMessage(String message) {
		this.message = message;
	}

	abstract IncrementType getIncrementType();

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String typeName = getIncrementType().getName();
		switch (getIncrementType()) {
			case MAJOR :
				typeName = getVersionConfig().getKeywords().getMajorKey();
				break;
			case MINOR :
				typeName = getVersionConfig().getKeywords().getMinorKey();
				break;
			case PATCH :
				typeName = getVersionConfig().getKeywords().getPatchKey();
				break;
		}
		if (!message.contains(KEYWORD_TOKEN))
			message = message.concat(" " + KEYWORD_TOKEN);
		String resolvedMessage = message.replace(KEYWORD_TOKEN, typeName);
		try {
			// Use local git configuration for any write operations to retain local user
			// settings such as sign
			String gitDir = mavenProject.getBasedir().getAbsoluteFile().toPath().resolve(".git").toString();
			boolean completed = new ProcessBuilder()
					.command("git", "--git-dir", gitDir, "commit", "--allow-empty", "-m", resolvedMessage).inheritIO()
					.start().waitFor(5, TimeUnit.SECONDS);
			if (!completed) {
				throw new MojoFailureException("Timed out for creating commit");
			}
		} catch (IOException | InterruptedException e) {
			throw new MojoFailureException(e.getMessage(), e);
		}
	}

	/**
	 * Add a new git commit to increment patch version.
	 */
	@Mojo(name = "commit-patch")
	public static class VersionCommitPatch extends VersionCommit {

		public IncrementType getIncrementType() {
			return IncrementType.PATCH;
		}
	}
	/**
	 * Add a new git commit to increment minor version.
	 */
	@Mojo(name = "commit-minor")
	public static class VersionCommitMinor extends VersionCommit {

		public IncrementType getIncrementType() {
			return IncrementType.MINOR;
		}
	}

	/**
	 * Add a new git commit to increment major version.
	 */
	@Mojo(name = "commit-major")
	public static class VersionCommitMajor extends VersionCommit {

		public IncrementType getIncrementType() {
			return IncrementType.MAJOR;
		}
	}
}
