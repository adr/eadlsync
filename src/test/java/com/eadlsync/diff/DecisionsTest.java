package com.eadlsync.diff;

import java.util.Arrays;

import com.eadlsync.EADLSyncExecption;
import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.diff.Decisions;
import com.eadlsync.util.YStatementJustificationComparator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class DecisionsTest extends YStatementTestData {

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
    public void testLocalDiffFieldsOnlyApplyChanges() throws EADLSyncExecption {
        Assert.assertTrue(YStatementJustificationComparator.isEqual(mergedBaseAndSomeDecision,
                decisionsOfOnlyLocalChanges().applyLocalDiff().get(0)));
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

    @Test
    public void testRemoteDiffFieldsOnlyApplyChanges() throws EADLSyncExecption {
        Assert.assertTrue(YStatementJustificationComparator.isEqual(mergedBaseAndSomeDecision,
                decisionsOfOnlyRemoteChanges().applyRemoteDiff().get(0)));
    }

    @Test
    public void testLocalAndRemoteDiffFieldsHaveLocalDiff() {
        Assert.assertTrue(decisionsOfLocalAndRemoteChangesNoConflict().hasLocalDiff());
    }

    @Test
    public void testLocalAndRemoteDiffFieldsHaveRemoteDiff() {
        Assert.assertTrue(decisionsOfLocalAndRemoteChangesNoConflict().hasRemoteDiff());
    }

    @Test
    public void testLocalAndRemoteDiffFieldsHaveNoConflicts() {
        Assert.assertTrue(decisionsOfLocalAndRemoteChangesNoConflict().canAutoMerge());
    }

    @Test
    public void testLocalAndRemoteDiffNoConflictsApplyChanges() throws EADLSyncExecption {
        Assert.assertTrue(YStatementJustificationComparator.isEqual
                (mergedBaseAndSomeAndSomeNonConflictingDecision,
                        decisionsOfLocalAndRemoteChangesNoConflict().applyLocalAndRemoteDiff().get(0)));
    }

    @Test
    public void testLocalAndRemoteConflictDiffFieldsHaveLocalDiff() {
        Assert.assertTrue(decisionsOfLocalAndRemoteChangesConflict().hasLocalDiff());
    }

    @Test
    public void testLocalAndRemoteConflictDiffFieldsHaveRemoteDiff() {
        Assert.assertTrue(decisionsOfLocalAndRemoteChangesConflict().hasRemoteDiff());
    }

    @Test
    public void testLocalAndRemoteConflictDiffFieldsHaveConflicts() {
        Assert.assertFalse(decisionsOfLocalAndRemoteChangesConflict().canAutoMerge());
    }

    private Decisions decisionsOfOnlyLocalChanges() {
        return createDecisions(clone(baseDecision), clone(someDecision), clone(baseDecision));
    }

    private Decisions decisionsOfOnlyRemoteChanges() {
        return createDecisions(clone(baseDecision), clone(baseDecision), clone(someDecision));
    }

    private Decisions decisionsOfLocalAndRemoteChangesNoConflict() {
        return createDecisions(clone(baseDecision), clone(someDecision), clone(someNonConflictingDecision));
    }

    private Decisions decisionsOfLocalAndRemoteChangesConflict() {
        return createDecisions(clone(baseDecision), clone(someDecision), clone(someConflictingOtherDecision));
    }

    private Decisions createDecisions(YStatementJustificationWrapper base,
                                      YStatementJustificationWrapper local,
                                      YStatementJustificationWrapper remote) {
        return new Decisions(Arrays.asList(base), Arrays.asList(local), Arrays.asList(remote));
    }

}
