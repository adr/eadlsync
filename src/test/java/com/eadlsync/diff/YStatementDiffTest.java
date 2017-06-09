package com.eadlsync.diff;

import com.eadlsync.YStatementTestData;
import com.eadlsync.model.diff.YStatementDiff;
import com.eadlsync.util.YStatementJustificationComparator;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class YStatementDiffTest implements YStatementTestData {

    @Test
    public void testConflictYStatements() {
        YStatementDiff yStatementDiff = YStatementDiff.of(baseDecision, someDecision);
        YStatementDiff conflictingDiff = YStatementDiff.of(baseDecision, someConflictingOtherDecision);
        Assert.assertTrue(yStatementDiff.conflictsWith(conflictingDiff));
    }

    @Test
    public void testConflictOnDeleteYStatements() {
        YStatementDiff yStatementDiff = YStatementDiff.of(baseDecision, someDecision);
        YStatementDiff conflictingDiff = YStatementDiff.of(baseDecision, null);
        Assert.assertTrue(yStatementDiff.conflictsWith(conflictingDiff));
    }

    @Test
    public void testConflictOnAddYStatements() {
        YStatementDiff yStatementDiff = YStatementDiff.of(null, someDecision);
        YStatementDiff conflictingDiff = YStatementDiff.of(null, someConflictingOtherDecision);
        Assert.assertTrue(yStatementDiff.conflictsWith(conflictingDiff));
    }

    @Test
    public void testNonConflictYStatements() {
        YStatementDiff yStatementDiff = YStatementDiff.of(baseDecision, someDecision);
        YStatementDiff nonConflictingDiff = YStatementDiff.of(baseDecision, someNonConflictingDecision);
        Assert.assertFalse(yStatementDiff.conflictsWith(nonConflictingDiff));
    }

}
