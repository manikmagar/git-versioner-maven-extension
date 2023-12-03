package com.github.manikmagar.maven.versioner.core.version;

import java.util.regex.Pattern;

import static java.lang.String.valueOf;

public class VersionPatternStrategy extends SemVerStrategy {

	public static final String DEFAULT_VERSION_PATTERN = "%M.%m.%p(-%c)";
	private final String versionPattern;

	public VersionPatternStrategy(int major, int minor, int patch, String branchName, String hashRef,
			String versionPattern) {
		super(major, minor, patch, branchName, hashRef);
		if (versionPattern == null || versionPattern.trim().isEmpty()) {
			this.versionPattern = DEFAULT_VERSION_PATTERN;
		} else {
			this.versionPattern = versionPattern;
		}
	}

	public VersionPatternStrategy(String branchName, String hashRef, String versionPattern) {
		super(branchName, hashRef);
		this.versionPattern = versionPattern;
	}

	public VersionPatternStrategy(int major, int minor, int patch, String branchName, String hashRef) {
		this(major, minor, patch, branchName, hashRef, DEFAULT_VERSION_PATTERN);
	}

	public VersionPatternStrategy(String branchName, String hashRef) {
		this(branchName, hashRef, DEFAULT_VERSION_PATTERN);
	}

	public String getVersionPattern() {
		return versionPattern;
	}

	@Override
	public String toVersionString() {
		return new TokenReplacer(getVersionPattern()).replace(PatternToken.MAJOR, getVersion().getMajor())
				.replace(PatternToken.MINOR, getVersion().getMinor())
				.replace(PatternToken.PATCH, getVersion().getPatch())
				.replace(PatternToken.COMMIT, getVersion().getCommit())
				.replace(PatternToken.BRANCH, getVersion().getBranch())
				.replace(PatternToken.HASH_SHORT, getVersion().getHashShort())
				.replace(PatternToken.HASH, getVersion().getHash()).toString();
	}

	public static class TokenReplacer {
		private String text;

		public TokenReplacer(String text) {
			this.text = text;
		}

		public TokenReplacer replace(PatternToken token, int value) {
			return replace(token, valueOf(value));
		}

		public TokenReplacer replace(PatternToken token, String value) {
			if (!text.contains(token.getToken()))
				return this;
			if (!PatternToken.COMMIT.equals(token)) {
				if (text.contains(token.getToken())) {
					text = text.replace(token.getToken(), value);
				}
			} else {
				// Full regex to match the version string containing group regex
				var fullRegex = ".*" + token.getTokenGroupRegex() + ".*";
				if (Pattern.matches(fullRegex, text)) {
					if (value != null && !value.trim().isEmpty() && !value.equals("0")) {
						text = text.replace(token.getToken(), value);
					} else {
						text = text.replaceAll(token.getTokenGroupRegex(), "");
					}
				} else if (text.contains(token.getToken())) {
					text = text.replace(token.getToken(), value);
				}
			}
			return this;
		}

		private String deTokenized() {
			return text.replace("(", "").replace(")", "");
		}

		@Override
		public String toString() {
			return deTokenized();
		}
	}

	public enum PatternToken {
		MAJOR("%M"), MINOR("%m"), PATCH("%p"), COMMIT("%c"), BRANCH("%b"), HASH_SHORT("%h"), HASH("%H");

		private final String token;
		private final String tokenGroupRegex;

		PatternToken(String token) {
			this.token = token;
			this.tokenGroupRegex = String.format("(\\([^(]*%s[^)]*\\))", token);
		}

		public String getToken() {
			return token;
		}

		public String getTokenGroupRegex() {
			return tokenGroupRegex;
		}

		@Override
		public String toString() {
			return getToken();
		}
	}

}
