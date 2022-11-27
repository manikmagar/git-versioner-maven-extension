/* (C)2022 */
package com.github.manikmagar.maven.versioner.git;

import com.github.manikmagar.maven.versioner.Version;
import com.github.manikmagar.maven.versioner.mojo.params.VersionConfig;
import com.github.manikmagar.maven.versioner.Versioner;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class JGitVersioner implements Versioner {

	VersionConfig versionConfig;

	public JGitVersioner(VersionConfig versionConfig) {
		this.versionConfig = versionConfig;
	}

	public Version version() {
		try (Repository repository = new FileRepositoryBuilder().readEnvironment().findGitDir().build()) {
			try (Git git = new Git(repository)) {
				var branch = git.getRepository().getBranch();
				Ref head = git.getRepository().findRef("HEAD");
				var hash = "";
				if (head != null && head.getObjectId() != null) {
					hash = head.getObjectId().getName();
				}
				var version = new Version(branch, hash, versionConfig.getInitial().getMajor(),
						versionConfig.getInitial().getMinor(), versionConfig.getInitial().getPatch());
				var commits = git.log().call();
				List<RevCommit> revCommits = StreamSupport.stream(commits.spliterator(), false)
						.collect(Collectors.toList());
				Collections.reverse(revCommits);
				for (RevCommit commit : revCommits) {
					if (commit.getFullMessage().contains(versionConfig.getKeywords().getMajorKey())) {
						version.incrementMajor();
					} else if (commit.getFullMessage().contains(versionConfig.getKeywords().getMinorKey())) {
						version.incrementMinor();
					} else if (commit.getFullMessage().contains(versionConfig.getKeywords().getPatchKey())) {
						version.incrementPatch();
					} else {
						version.incrementCommit();
					}
				}
				return version;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
