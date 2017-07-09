package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import com.beust.jcommander.JCommander;
import com.eadlsync.CodeRepoMock;
import com.eadlsync.cli.option.MainOption;
import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.net.serepo.SeRepoTestServer;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import static com.eadlsync.data.TestDataProvider.TEST_REPO;
import static com.eadlsync.data.TestDataProvider.getBasicDecisionsAsEadl;
import static com.eadlsync.data.TestDataProvider.getBasicDecisionsAsSeItemsWithContent;

/**
 *
 */
public class CommandTest {

    static final SeRepoTestServer seRepoTestServer = new SeRepoTestServer();
    final InitCommand INIT_COMMAND = new InitCommand();
    final DeInitCommand DE_INIT_COMMAND = new DeInitCommand();
    final ConfigCommand CONFIG_COMMAND = new ConfigCommand();
    final StatusCommand STATUS_COMMAND = new StatusCommand();
    final CommitCommand COMMIT_COMMAND = new CommitCommand();
    final PullCommand PULL_COMMAND = new PullCommand();
    final MergeCommand MERGE_COMMAND = new MergeCommand();
    final ResetCommand RESET_COMMAND = new ResetCommand();
    final SyncCommand SYNC_COMMAND = new SyncCommand();
    final MainOption option = new MainOption();
    final JCommander commander = JCommander.newBuilder().addObject(option).
            addCommand(InitCommand.NAME, INIT_COMMAND).
            addCommand(DeInitCommand.NAME, DE_INIT_COMMAND).
            addCommand(ConfigCommand.NAME, CONFIG_COMMAND).
            addCommand(StatusCommand.NAME, STATUS_COMMAND).
            addCommand(CommitCommand.NAME, COMMIT_COMMAND).
            addCommand(PullCommand.NAME, PULL_COMMAND).
            addCommand(MergeCommand.NAME, MERGE_COMMAND).
            addCommand(ResetCommand.NAME, RESET_COMMAND).
            addCommand(SyncCommand.NAME, SYNC_COMMAND).
            build();

    @ClassRule
    public static TemporaryFolder tempRoot = new TemporaryFolder();

    @Rule
    public TemporaryFolder codeBase = new TemporaryFolder(tempRoot.getRoot());

    CodeRepoMock codeRepoMock;

    @BeforeClass
    public static void classSetUp() throws Exception {
        seRepoTestServer.start();
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        seRepoTestServer.stop();
    }

    @Before
    public void methodSetUp() throws IOException, UnirestException {
        // one commit with four decisions is always available and we have the same decisions as eadls
        // in our code repo
        seRepoTestServer.createRepository();
        seRepoTestServer.createCommit(getBasicDecisionsAsSeItemsWithContent(), CommitMode.ADD_UPDATE);

        DecisionSourceMapping.clear();
        Path code = codeBase.getRoot().toPath();
        codeRepoMock = new CodeRepoMock(code);
        codeRepoMock.createClassesForEadls(getBasicDecisionsAsEadl());

        commander.parse(InitCommand.NAME, "-u", seRepoTestServer.LOCALHOST_SEREPO, "-p", TEST_REPO,
                "-s", code.toString());
        INIT_COMMAND.initialize();
    }

    @After
    public void methodTearDown() throws IOException {
        seRepoTestServer.deleteRepository();

        commander.parse(DeInitCommand.NAME);
        DE_INIT_COMMAND.deInit();
    }

    public boolean assertYStatementJustificationListAreEqualIgnoringOrder(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> toBeChecked) {
        for (YStatementJustificationWrapper y1 : base) {
            if (toBeChecked.stream().filter(y2 -> YStatementJustificationComparator.isEqual(y1, y2)).collect(Collectors.toList()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
