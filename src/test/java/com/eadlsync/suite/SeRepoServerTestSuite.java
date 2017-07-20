package com.eadlsync.suite;

import com.eadlsync.cli.command.CommitCommandTest;
import com.eadlsync.cli.command.InitCommandTest;
import com.eadlsync.cli.command.PullCommandTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests which depend on the se-repo server need long setup and therefore take a long time to run.
 * Any se-repo server dependent tests should be listed in the SuiteClasses annotation.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        InitCommandTest.class,
        PullCommandTest.class,
        CommitCommandTest.class
})
public class SeRepoServerTestSuite {
    // placeholder class to run all tests that are dependent on the se-repo server
}
