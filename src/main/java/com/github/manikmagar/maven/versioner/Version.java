package com.github.manikmagar.maven.versioner;

import java.util.Objects;

public class Version {
  private int major;
  private int minor;
  private int patch;
  private int commit;
  private final String branch;
  private final String hash;
  
  public Version(String branch, String hash) {
    this.branch = branch;
    this.hash = hash;
    major = 0;
    minor = 0;
    patch = 0;
    commit = 0;
  }

  public int getMajor() {
    return major;
  }
  public int getMinor() {
    return minor;
  }
  public int getPatch() {
    return patch;
  }

  public void incrementMajor(){
    major++;
    minor = 0;
    patch= 0;
    commit = 0;
  }
  public void incrementMinor(){
    minor++;
    patch=0;
    commit = 0;
  }
  public void incrementPatch(){
    patch++;
    commit = 0;
  }
  public void incrementCommit(){
    commit++;
  }

  @Override
  public String toString() {
    String version = String.format("%d.%d.%d", getMajor(), getMinor(), getPatch());
    if(commit > 0 ){
      version = String.format("%s.%d", version, commit);
    }
    return version;
  }
  public void printVersion(){
    System.out.printf("[branch: %s, version: %s, hash: %s]", branch, toString(), hash);
    System.out.println();
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Version)) return false;
    Version version = (Version) o;
    return getMajor() == version.getMajor() && getMinor() == version.getMinor()
            && getPatch() == version.getPatch();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMajor(), getMinor(), getPatch());
  }
}
