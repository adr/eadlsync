package com.eadlsync.diff;

import com.eadlsync.model.diff.ConflictManagerViewModel;
import com.eadlsync.model.diff.DiffManager;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by tobias on 18/06/2017.
 */
public class ConflictManagerViewModelTest extends DiffTest {

    private ConflictManagerViewModel conflictManagerViewModel;
    private DiffManager diffManager;

    @Before
    public void methodSetUp() {
        diffManager = decisionsOfLocalAndRemoteChangesConflict();
        conflictManagerViewModel = new ConflictManagerViewModel(diffManager);
    }

    @Test
    public void testCanGoNextBinding() {
        Assert.assertFalse(conflictManagerViewModel.canGoToNextConflictProperty().get());
    }

    @Test
    public void testCanNotFinishWhenNoDecisionIsSelected() {
        Assert.assertFalse(conflictManagerViewModel.isAllConflictsResolvedProperty().get());
    }

    @Test
    public void testCanFinishWhenLastDecisionIsResolved() {
        conflictManagerViewModel.setAllLocalSelected(true);
        conflictManagerViewModel.setAllRemoteSelected(false);
        Assert.assertTrue(conflictManagerViewModel.isAllConflictsResolvedProperty().get());
    }

    @Test
    public void testSelectingLocalDecisionResultsInLocalDecision() {
        conflictManagerViewModel.setAllLocalSelected(true);
        conflictManagerViewModel.setAllRemoteSelected(false);
        conflictManagerViewModel.finishResolvingCurrentConflict();

        Assert.assertTrue(YStatementJustificationComparator.isEqual(someDecision, conflictManagerViewModel.getResultingDecisions().get(0)));
    }

    @Test
    public void testSelectingRemoteDecisionResultsInRemoteDecision() {
        conflictManagerViewModel.setAllLocalSelected(false);
        conflictManagerViewModel.setAllRemoteSelected(true);
        conflictManagerViewModel.finishResolvingCurrentConflict();

        Assert.assertTrue(YStatementJustificationComparator.isEqual(someConflictingOtherDecision, conflictManagerViewModel.getResultingDecisions().get(0)));
    }

    @Test
    public void testGetInitialResultingDecisionsEqualsBaseDecisions() {
        Assert.assertEquals(diffManager.getBaseDecisions(), conflictManagerViewModel.getResultingDecisions());
    }

}
