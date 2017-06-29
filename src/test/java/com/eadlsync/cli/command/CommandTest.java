package com.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.eadlsync.cli.option.MainOption;
import com.eadlsync.net.serepo.SeRepoServerTest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Before;

/**
 *
 */
public class CommandTest extends SeRepoServerTest {

    static final InitCommand INIT_COMMAND = new InitCommand();
    static final DeInitCommand DE_INIT_COMMAND = new DeInitCommand();
    static final ConfigCommand CONFIG_COMMAND = new ConfigCommand();
    static final StatusCommand STATUS_COMMAND = new StatusCommand();
    static final CommitCommand COMMIT_COMMAND = new CommitCommand();
    static final PullCommand PULL_COMMAND = new PullCommand();
    static final MergeCommand MERGE_COMMAND = new MergeCommand();
    static final ResetCommand RESET_COMMAND = new ResetCommand();
    static final SyncCommand SYNC_COMMAND = new SyncCommand();

    JCommander commander;

    @Before
    public void initializeEadlSync() throws IOException, UnirestException {
        MainOption option = new MainOption();
        commander = JCommander.newBuilder().addObject(option).
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
        commander.parse(InitCommand.NAME, "-u", LOCALHOST_SEREPO, "-p", TEST_REPO);
        INIT_COMMAND.initialize();
    }

    @After
    public void deInitializeEadlSync() throws IOException {
        commander.parse(DeInitCommand.NAME);
        DE_INIT_COMMAND.deInit();
    }


}
