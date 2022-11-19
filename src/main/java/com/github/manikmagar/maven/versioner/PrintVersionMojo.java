package com.github.manikmagar.maven.versioner;

import com.github.manikmagar.maven.versioner.git.JGitVersioner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "print-version")
public class PrintVersionMojo extends AbstractMojo {
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    System.out.println(new JGitVersioner(new VersionConfig()).version());
  }
}
