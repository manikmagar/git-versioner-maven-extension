package com.github.manikmagar.maven.versioner.core.git;

import com.github.manikmagar.maven.versioner.core.params.VersionConfig;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JGitVersionerTest {
    private VersionConfig versionConfig = new VersionConfig();;
    private JGitVersioner jGitVersioner = new JGitVersioner(versionConfig);

    @Test
    @Parameters(value = {
            "Fix: [minor] corrected typo, .*\\[minor\\].*, true",
            "[minor] Fix: corrected typo, ^\\[minor\\].*, true",
            "Fix: [minor] corrected typo, ^\\[minor\\].*, false",
            "Update: improved performance, [minor], false"})
    public void testHasValueWithRegex(String commitMessage, String keyword, boolean expected) {
        versionConfig.getKeywords().setUseRegex(true);

        assertThat(jGitVersioner.hasValue(commitMessage, keyword)).isEqualTo(expected);
    }

    @Test
    public void testHasValueWithoutRegex() {
        versionConfig.getKeywords().setUseRegex(false);

        String commitMessage = "Fix: [minor] corrected typo";
        assertThat(jGitVersioner.hasValue(commitMessage, "[minor]")).isTrue();

        commitMessage = "Update: improved performance";
        assertThat(jGitVersioner.hasValue(commitMessage, "[minor]")).isFalse();
    }
}
