package com.github.manikmagar.maven.versioner.plugin.mojo;

import com.github.manikmagar.maven.versioner.core.GitVersionerException;
import com.github.manikmagar.maven.versioner.core.git.GitTag;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Create a local git tag with current version.
 */
@Mojo(name = "tag", requiresProject = true)
public class Tag extends AbstractVersionerMojo {

	/**
	 * Fail the build if tag already exist?
	 */
	@Parameter(name = "failWhenTagExist", defaultValue = "true", property = "tag.failWhenTagExist")
	private boolean failWhenTagExist = true;

	/**
	 * Pattern to create tag message. You may use following tokens -
	 * 
	 * <pre>
	 *    - %v : replace with the generated version
	 * </pre>
	 */
	@Parameter(name = "tagMessagePattern", defaultValue = "Release version %v", property = "tag.messagePattern")
	private String tagMessagePattern = "Release version %v";

	/**
	 * Pattern to create tag name. You may use following tokens -
	 * 
	 * <pre>
	 *    - %v : replace with the generated version
	 * </pre>
	 */
	@Parameter(name = "tagNamePattern", defaultValue = "v%v", property = "tag.namePattern")
	private String tagNamePattern = "v%v";

	public boolean isFailWhenTagExist() {
		return failWhenTagExist;
	}

	public String getTagMessagePattern() {
		return tagMessagePattern;
	}

	public String getTagNamePattern() {
		return tagNamePattern;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		var versionStrategy = getVersioner().version();
		var tagName = replaceTokens(getTagNamePattern(), versionStrategy);
		var tagMessage = replaceTokens(getTagMessagePattern(), versionStrategy);
		getLog().info("Current Version: " + versionStrategy.toVersionString());
		getLog().info(String.format("Tag Version '%s' with message '%s'", tagName, tagMessage));
		if (GitTag.exists(tagName)) {
			getLog().error(String.format("Tag already exist: %s", tagName));
			if (isFailWhenTagExist())
				throw new GitVersionerException("Tag already exist: " + tagName);
		} else {
			String tagId = GitTag.create(tagName, tagMessage);
			getLog().info(String.format("Created tag: '%s'", tagId));
		}
	}
}
