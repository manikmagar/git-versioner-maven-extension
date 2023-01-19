package com.github.manikmagar.maven.versioner.core.git;

import com.github.manikmagar.maven.versioner.core.GitVersionerException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;

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

	/**
	 * Find local git dir by scanning upwards to find .git dir
	 * 
	 * @return
	 */
	public static String findGitDir(String basePath) {
		try (Repository repository = new FileRepositoryBuilder().readEnvironment().findGitDir(new File(basePath))
				.build()) {
			return repository.getDirectory().getAbsolutePath();
		} catch (Exception e) {
			throw new GitVersionerException(e.getMessage(), e);
		}
	}

	@FunctionalInterface
	public interface Operation<T, R> {
		R apply(T t) throws Exception;
	}

}
