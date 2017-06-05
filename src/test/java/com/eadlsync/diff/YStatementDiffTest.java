package com.eadlsync.diff;

import com.eadlsync.YStatementTestData;
import com.eadlsync.model.diff.YStatementDiff;
import com.eadlsync.util.YStatementJustificationComparator;
import org.junit.Assert;
import org.junit.Test;

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
    public void testNonConflictYStatements() {
        YStatementDiff yStatementDiff = YStatementDiff.of(baseDecision, someDecision);
        YStatementDiff nonConflictingDiff = YStatementDiff.of(baseDecision, someNonConflictingDecision);
        Assert.assertFalse(yStatementDiff.conflictsWith(nonConflictingDiff));
    }

    @Test
    public void testApplyNonConflicting() {
        YStatementDiff yStatementDiff = YStatementDiff.of(baseDecision, someDecision);
        YStatementDiff nonConflictingDiff = YStatementDiff.of(baseDecision, someNonConflictingDecision);
        Assert.assertTrue(YStatementJustificationComparator.isEqual(someNonConflictingAppliedDecision, yStatementDiff.applyNonConflicting(nonConflictingDiff)));
    }

}
