package com.github.manikmagar.maven.versioner.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.github.manikmagar.maven.versioner.GitVersionerException;

public class JGit {

	private JGit() {
	}

	public static <R> R executeOperation(Operation<Git, R> work) throws GitVersionerException {
		try (Repository repository = new FileRepositoryBuilder().readEnvironment().findGitDir().build()) {
			try (Git git = new Git(repository)) {
				return work.apply(git);
			}
		} catch (Exception e) {
			throw new GitVersionerException(e.getMessage(), e);
		}
	}

	@FunctionalInterface
	public interface Operation<T, R> {
		R apply(T t) throws Exception;
	}

}
