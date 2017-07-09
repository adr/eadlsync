package com.eadlsync.cli.command;

import java.util.Arrays;
import java.util.List;

import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import com.eadlsync.data.TestDataProvider;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import org.junit.Assert;
import org.junit.Test;

import static com.eadlsync.data.TestDataProvider.createTestChangedModifiedYStatementJustificationWrapper;
import static com.eadlsync.data.TestDataProvider.getBasicDecisionsAsEadl;

/**
 *
 */
public class PullCommandTest extends CommandTest {

    @Test
    public void testPullUpToDateResultsInNoChange() throws Exception {
        PULL_COMMAND.pull();

        Assert.assertTrue(assertYStatementJustificationListAreEqualIgnoringOrder
                (getBasicDecisionsAsEadl(), codeRepoMock.readLocalDecisions()));
    }

    @Test
    public void testPullLocalChangesOnlyResultsInNoChange() throws Exception {
        codeRepoMock.createClassesForEadls(Arrays.asList(createTestChangedModifiedYStatementJustificationWrapper()));
        PULL_COMMAND.pull();

        List<YStatementJustificationWrapper> base = getBasicDecisionsAsEadl();
        base.add(createTestChangedModifiedYStatementJustificationWrapper());

        Assert.assertTrue(assertYStatementJustificationListAreEqualIgnoringOrder
                (base, codeRepoMock.readLocalDecisions()));
    }

    @Test
    public void testPullRemoteChangesOnlyResultsInModifiedLocalDecisions() throws Exception {
        seRepoTestServer.createCommit(TestDataProvider.createChangedTestSeItemsWithContentModified(),
                CommitMode.ADD_UPDATE);

        PULL_COMMAND.pull();

        Assert.assertFalse(assertYStatementJustificationListAreEqualIgnoringOrder
                (getBasicDecisionsAsEadl(), codeRepoMock.readLocalDecisions()));
    }


}
