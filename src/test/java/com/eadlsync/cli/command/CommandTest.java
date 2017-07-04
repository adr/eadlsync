package com.eadlsync.cli.command;

import com.beust.jcommander.JCommander;
import com.eadlsync.CodeRepoMock;
import com.eadlsync.cli.option.MainOption;
import com.eadlsync.net.serepo.SeRepoTestServer;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

import static com.eadlsync.data.TestDataProvider.*;

/**
 *
 */
public class CommandTest extends SeRepoTestServer {

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

    static final CodeRepoMock codeRepoMock = new CodeRepoMock();
    static final SeRepoTestServer seRepoTestServer = new SeRepoTestServer();

    @BeforeClass
    public static void classSetUp() throws Exception {
        seRepoTestServer.start();
        codeRepoMock.createCodeRepo();
    }

    @Before
    public void methodSetUp() throws IOException, UnirestException {
        // one commit with four decisions is always available and we have the same decisions as eadls in our code repo
        seRepoTestServer.createRepository();
        seRepoTestServer.createCommit(getBasicDecisionsAsSeItemsWithContent());

        codeRepoMock.createClassesForEadls(getBasicDecisionsAsEadl());

        commander.parse(InitCommand.NAME, "-u", LOCALHOST_SEREPO, "-p", TEST_REPO);
        INIT_COMMAND.initialize();
    }

    @After
    public void methodTearDown() throws IOException {
        seRepoTestServer.deleteRepository();
        codeRepoMock.cleanCodeRepo();

        commander.parse(DeInitCommand.NAME);
        DE_INIT_COMMAND.deInit();
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        seRepoTestServer.stop();
        codeRepoMock.deleteCodeRepo();
    }


}
