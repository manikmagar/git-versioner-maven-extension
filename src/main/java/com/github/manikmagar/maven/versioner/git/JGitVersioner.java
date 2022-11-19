package com.github.manikmagar.maven.versioner.git;

import com.github.manikmagar.maven.versioner.Version;
import com.github.manikmagar.maven.versioner.VersionConfig;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JGitVersioner {

  VersionConfig versionConfig;

  public JGitVersioner(VersionConfig versionConfig) {
    this.versionConfig = versionConfig;
  }

  public String version(){
    try(Repository repository = new FileRepositoryBuilder().readEnvironment()
            .findGitDir().build()){
      try(Git git = new Git(repository)) {
        var branch = git.getRepository().getBranch();
        var hash = git.getRepository().findRef("HEAD").getName();
        var version = new Version(branch, hash);
        var commits = git.log().call();
        List<RevCommit> revCommits = StreamSupport.stream(commits.spliterator(), false)
                .collect(Collectors.toList());
        Collections.reverse(revCommits);
        for (RevCommit commit : revCommits) {
          if(commit.getFullMessage().contains(versionConfig.getMajorKey())){
            version.incrementMajor();
          } else if (commit.getFullMessage().contains(versionConfig.getMinorKey())){
            version.incrementMinor();
          } else if (commit.getFullMessage().contains(versionConfig.getPatchKey())) {
            version.incrementPatch();
          }
          System.out.println("LogCommit: " + commit + ", message:" + commit.getFullMessage());
        }
        version.printVersion();
        return version.toString();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
