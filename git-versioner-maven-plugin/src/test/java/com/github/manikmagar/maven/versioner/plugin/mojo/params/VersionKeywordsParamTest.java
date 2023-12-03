package com.github.manikmagar.maven.versioner.plugin.mojo.params;

import com.github.manikmagar.maven.versioner.core.params.VersionKeywords;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionKeywordsParamTest {

	@Test
	public void newVersionKeywords() {
		assertThat(new VersionKeywords("[big]", "[medium]", "[small]", false)).extracting("majorKey", "minorKey", "patchKey")
				.containsExactly("[big]", "[medium]", "[small]");
	}

	@Test
	public void toDefaultVersionKeywords() {
		assertThat(new VersionKeywordsParam()).extracting(VersionKeywordsParam::toVersionKeywords)
				.isEqualTo(new VersionKeywords());
	}
	@Test
	public void toCustomVersionKeywords() {
		assertThat(new VersionKeywordsParam("[big]", "[medium]", "[small]", false))
				.extracting(VersionKeywordsParam::toVersionKeywords)
				.isEqualTo(new VersionKeywords("[big]", "[medium]", "[small]", false));
	}
}
