package com.eadlsync.diff;

import java.util.Arrays;

import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.diff.Decisions;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class DecisionsTest implements YStatementTestData {

    @Test
    public void testLocalDiffFieldsOnlyHasLocalChanges() {
        Assert.assertTrue(decisionsOfOnlyLocalChanges().hasLocalDiff());
    }

    @Test
    public void testLocalDiffFieldsOnlyHasNoRemoteDiff() {
        Assert.assertFalse(decisionsOfOnlyLocalChanges().hasRemoteDiff());
    }

    @Test
    public void testLocalDiffFieldsOnlyHasNoMergeConflicts() {
        Assert.assertTrue(decisionsOfOnlyLocalChanges().canAutoMerge());
    }

    @Test
    public void testRemoteDiffFieldsOnlyHasNoLocalChanges() {
        Assert.assertFalse(decisionsOfOnlyRemoteChanges().hasLocalDiff());
    }

    @Test
    public void testRemoteDiffFieldsOnlyHasRemoteDiff() {
        Assert.assertTrue(decisionsOfOnlyRemoteChanges().hasRemoteDiff());
    }

    @Test
    public void testRemoteDiffFieldsOnlyHasNoMergeConflicts() {
        Assert.assertTrue(decisionsOfOnlyRemoteChanges().canAutoMerge());
    }

    private Decisions decisionsOfOnlyLocalChanges() {
        return createDecisions(baseDecision, someDecision, baseDecision);
    }

    private Decisions decisionsOfOnlyRemoteChanges() {
        return createDecisions(baseDecision, baseDecision, someDecision);
    }

    private Decisions createDecisions(YStatementJustificationWrapper base, YStatementJustificationWrapper local, YStatementJustificationWrapper remote) {
        return new Decisions(Arrays.asList(base), Arrays.asList(local), Arrays.asList(remote));
    }

}
