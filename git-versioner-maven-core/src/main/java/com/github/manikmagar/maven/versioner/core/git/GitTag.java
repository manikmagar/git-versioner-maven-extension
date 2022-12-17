package com.github.manikmagar.maven.versioner.core.git;

import org.eclipse.jgit.lib.Ref;

public class GitTag {

	private GitTag() {

	}

	public static String create(final String tagName, final String tagMessage) {
		return JGit.executeOperation(git -> {
			Ref tag = git.tag().setName(tagName).setMessage(tagMessage).call();
			return String.format("%s@%s", tag.getName(), tag.getObjectId().getName());
		});
	}

	public static boolean exists(final String tagName) {
		return JGit.executeOperation(git -> null != git.getRepository().findRef("refs/tags/" + tagName));
	}
}
