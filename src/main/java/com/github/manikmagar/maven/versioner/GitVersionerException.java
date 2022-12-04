package com.github.manikmagar.maven.versioner;

public class GitVersionerException extends RuntimeException {
	public GitVersionerException(String message, Exception cause) {
		super(message, cause);
	}

	public GitVersionerException(String message) {
		super(message);
	}
}
