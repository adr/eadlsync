package io.github.adr.eadlsync.cli.command;

import java.io.IOException;

import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import io.github.adr.eadlsync.data.TestDataProvider;
import io.github.adr.eadlsync.util.io.JavaDecisionParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class PullCommandTest extends CommandTest {

    private String commitId;

    @Before
    public void setCurrentCommitId() throws IOException {
        commitId = PULL_COMMAND.readCommitId();
    }

    @Test
    public void testPullUpToDateResultsInNoChange() throws Exception {
        PULL_COMMAND.pull();

        Assert.assertEquals(commitId, COMMIT_COMMAND.readCommitId());
    }

    @Test
    public void testPullLocalChangesOnlyResultsInNoChange() throws Exception {
        JavaDecisionParser.writeModifiedYStatementToFile(TestDataProvider.createTestChangedModifiedYStatementJustificationWrapper());
        PULL_COMMAND.pull();

        Assert.assertEquals(commitId, COMMIT_COMMAND.readCommitId());
    }

    @Test
    public void testPullRemoteChangesOnlyResultsInModifiedLocalDecisions() throws Exception {
        seRepoTestServer.createCommit(TestDataProvider.createChangedTestSeItemsWithContentModified(),
                CommitMode.ADD_UPDATE);

        PULL_COMMAND.pull();

        Assert.assertNotEquals(commitId, COMMIT_COMMAND.readCommitId());
    }


}
