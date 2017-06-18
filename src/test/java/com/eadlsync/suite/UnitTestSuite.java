package com.eadlsync.suite;

import com.eadlsync.diff.ConflictManagerViewModelTest;
import com.eadlsync.diff.DiffManagerTest;
import com.eadlsync.diff.YStatementDiffTest;
import com.eadlsync.javaparser.JavaDecisionParserTest;
import com.eadlsync.util.YStatementJustificationComparatorTest;
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
