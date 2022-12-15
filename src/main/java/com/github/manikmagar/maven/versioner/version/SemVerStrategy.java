package com.github.manikmagar.maven.versioner.version;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.manikmagar.semver4j.SemVer;

/**
 * Create Semantic version string
 */
public class SemVerStrategy extends AbstractVersionStrategy {

	private final SemVer semVer;

	private final AtomicInteger commitCount = new AtomicInteger(0);

	public SemVerStrategy(int major, int minor, int patch, String branchName, String hashRef) {
		super(branchName, hashRef);
		semVer = SemVer.of(major, minor, patch);
	}
	public SemVerStrategy(String branchName, String hashRef) {
		super(branchName, hashRef);
		semVer = SemVer.zero();
	}

	@Override
	public String strategyName() {
		return "SemVer";
	}

	@Override
	public String toVersionString() {
		if (commitCount.get() > 0) {
			semVer.withNew(SemVer.build(getHashShort()));
		}
		return semVer.toString();
	}

	@Override
	public Version getVersion() {
		return new Version(getBranchName(), getHash(), semVer.getMajor(), semVer.getMinor(), semVer.getPatch());
	}

	@Override
	public void increment(VersionComponentType type, String hashRef) {
		Objects.requireNonNull(type, "Version component type must not be null");
		Objects.requireNonNull(hashRef, "Hash Reference must not be null");
		setHash(hashRef);
		switch (type) {
			case MAJOR :
				onMajorIncrement();
				break;
			case MINOR :
				onMinorIncrement();
				break;
			case PATCH :
				onPatchIncrement();
				break;
			case COMMIT :
				onCommitIncrement();
				break;
		}
	}
	private void onMajorIncrement() {
		semVer.incrementMajor();
		commitCount.set(0);
	}

	private void onMinorIncrement() {
		semVer.incrementMinor();
		commitCount.set(0);
	}

	private void onPatchIncrement() {
		semVer.incrementPatch();
		commitCount.set(0);
	}

	private void onCommitIncrement() {
		commitCount.incrementAndGet();
	}

}
