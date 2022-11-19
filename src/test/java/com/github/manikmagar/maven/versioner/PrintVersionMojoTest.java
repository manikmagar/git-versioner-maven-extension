package com.github.manikmagar.maven.versioner;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PrintVersionMojoTest {

  @Rule
  public MojoRule rule = new MojoRule()
  {
    @Override
    protected void before() throws Throwable
    {
    }

    @Override
    protected void after()
    {
    }
  };

  /**
   * @throws Exception if any
   */
  @Test
  public void testVersion()
          throws Exception
  {
    File pom = new File( "target/test-classes/project-to-test/" );
    assertNotNull( pom );
    assertTrue( pom.exists() );

    PrintVersionMojo printVersionMojo = ( PrintVersionMojo ) rule.lookupConfiguredMojo( pom, "print-version" );
    assertNotNull( printVersionMojo );
    printVersionMojo.execute();

  }
}