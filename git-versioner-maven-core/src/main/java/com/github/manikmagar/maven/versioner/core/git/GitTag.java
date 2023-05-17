package com.github.manikmagar.maven.versioner.core.git;

import org.eclipse.jgit.lib.Ref;

import java.io.File;

public class GitTag {

	private GitTag() {

	}

	public static String create(File basePath, final String tagName, final String tagMessage) {
		return JGit.executeOperation(basePath, git -> {
			Ref tag = git.tag().setName(tagName).setMessage(tagMessage).call();
			return String.format("%s@%s", tag.getName(), tag.getObjectId().getName());
		});
	}

	public static boolean exists(File basePath, final String tagName) {
		return JGit.executeOperation(basePath, git -> null != git.getRepository().findRef("refs/tags/" + tagName));
	}
}
