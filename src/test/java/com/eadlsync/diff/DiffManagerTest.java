package com.eadlsync.diff;

import java.util.Arrays;

import com.eadlsync.EADLSyncExecption;
import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.diff.DiffManager;
import com.eadlsync.util.YStatementJustificationComparator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class DiffManagerTest extends DiffTest {

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

    @Test(expected = EADLSyncExecption.class)
    public void testLocalAndRemoteConflictDiffFieldsApplyChangesThrowsException() throws
            EADLSyncExecption {
        Assert.assertTrue(YStatementJustificationComparator.isEqual
                (mergedBaseAndSomeAndSomeNonConflictingDecision,
                        decisionsOfLocalAndRemoteChangesConflict().applyLocalAndRemoteDiff().get(0)));
    }

    @Test
    public void testAdditionalLocalDecisionHasNoEffectOnLocalAndRemoteChanges() {
        Assert.assertFalse(decisionsOfAdditionalLocalDecision().hasLocalDiff());
        Assert.assertFalse(decisionsOfAdditionalLocalDecision().hasRemoteDiff());
    }

    @Test
    public void testAdditionalRemoteDecisionHasNoEffectOnLocalAndRemoteChanges() {
        Assert.assertFalse(decisionsOfAdditionalRemoteDecision().hasLocalDiff());
        Assert.assertFalse(decisionsOfAdditionalRemoteDecision().hasRemoteDiff());
    }

    @Test
    public void testRemovedLocalDecisionHasLocalChanges() {
        Assert.assertTrue(decisionsOfRemovedLocalDecision().hasLocalDiff());
    }

    @Test
    public void testRemovedRemoteDecisionHasRemoteChanges() {
        Assert.assertTrue(decisionsOfRemovedRemoteDecision().hasRemoteDiff());
    }



}
