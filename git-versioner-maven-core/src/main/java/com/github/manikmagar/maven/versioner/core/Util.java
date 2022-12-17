package com.github.manikmagar.maven.versioner.core;

public final class Util {

	private Util() {
	}

	public static void mustBePositive(int number, String label) {
		if (number < 0)
			throw new IllegalArgumentException(label + " must be a positive number");
	}
}
