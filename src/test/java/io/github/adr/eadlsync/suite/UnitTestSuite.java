package io.github.adr.eadlsync.suite;

import io.github.adr.eadlsync.diff.ConflictManagerViewModelTest;
import io.github.adr.eadlsync.diff.DiffManagerTest;
import io.github.adr.eadlsync.diff.YStatementDiffTest;
import io.github.adr.eadlsync.javaparser.JavaDecisionParserTest;
import io.github.adr.eadlsync.util.YStatementJustificationComparatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for normal unit tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConflictManagerViewModelTest.class,
        DiffManagerTest.class,
        YStatementDiffTest.class,
        JavaDecisionParserTest.class,
        YStatementJustificationComparatorTest.class
})
public class UnitTestSuite {
    // placeholder class to run all unit tests that do not need the se-repo server
}
