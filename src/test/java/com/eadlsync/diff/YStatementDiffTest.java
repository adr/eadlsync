package com.eadlsync.diff;

import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.diff.YStatementDiff;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class YStatementDiffTest extends YStatementTestData {

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
