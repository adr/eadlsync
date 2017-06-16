package com.eadlsync.cli;

import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.eadlsync.EADLSyncExecption;
import com.eadlsync.cli.command.CommitCommand;
import com.eadlsync.cli.command.ConfigCommand;
import com.eadlsync.cli.command.DeInitCommand;
import com.eadlsync.cli.command.InitCommand;
import com.eadlsync.cli.command.MergeCommand;
import com.eadlsync.cli.command.PullCommand;
import com.eadlsync.cli.command.ResetCommand;
import com.eadlsync.cli.command.StatusCommand;
import com.eadlsync.cli.command.SyncCommand;
import com.eadlsync.cli.option.MainOption;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tobias on 23.04.2017.
 */
public class EADLSyncMain {

    private static final Logger LOG = LoggerFactory.getLogger(EADLSyncMain.class);
    private static final InitCommand INIT_COMMAND = new InitCommand();
    private static final DeInitCommand DE_INIT_COMMAND = new DeInitCommand();
    private static final ConfigCommand CONFIG_COMMAND = new ConfigCommand();
    private static final StatusCommand STATUS_COMMAND = new StatusCommand();
    private static final CommitCommand COMMIT_COMMAND = new CommitCommand();
    private static final PullCommand PULL_COMMAND = new PullCommand();
    private static final MergeCommand MERGE_COMMAND = new MergeCommand();
    private static final ResetCommand RESET_COMMAND = new ResetCommand();
    private static final SyncCommand SYNC_COMMAND = new SyncCommand();

    public static void main(String[] args) {

        MainOption option = new MainOption();
        JCommander commander = JCommander.newBuilder().addObject(option).
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

        commander.parse(args);

        if (InitCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                INIT_COMMAND.initialize();
            } catch (IOException e) {
                LOG.debug("Error initializing files", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            }
        }

        if (ConfigCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                CONFIG_COMMAND.configure();
            } catch (IOException e) {
                LOG.debug("Error writing or reading the config file", e);
            }
        }

        if (StatusCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                STATUS_COMMAND.getStatus();
            } catch (IOException e) {
                LOG.debug("Error reading the config file", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            }
        }

        if (PullCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                PULL_COMMAND.pull();
            } catch (IOException e) {
                LOG.debug("Error reading the config file", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            } catch (EADLSyncExecption eadlSyncExecption) {
                LOG.debug("Error while pulling decisions", eadlSyncExecption);
            }
        }

        if (CommitCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                COMMIT_COMMAND.commit();
            } catch (EADLSyncExecption eadlSyncExecption) {
                LOG.debug("Error while committing files", eadlSyncExecption);
            } catch (IOException e) {
                LOG.debug("Error reading the config file", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            }
        }

        if (MergeCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                MERGE_COMMAND.merge();
            } catch (IOException e) {
                LOG.debug("Error reading the config file", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            } catch (EADLSyncExecption eadlSyncExecption) {
                LOG.debug("Error while merging decisions", eadlSyncExecption);
            }
        }

        if (ResetCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                RESET_COMMAND.resetLocalChanges();
            } catch (IOException e) {
                LOG.debug("Error reading the config file", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            } catch (EADLSyncExecption eadlSyncExecption) {
                LOG.debug("Error while committing decisions", eadlSyncExecption);
            }
        }

        if (SyncCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                SYNC_COMMAND.sync();
            } catch (IOException e) {
                LOG.debug("Error reading the config file", e);
            } catch (UnirestException e) {
                LOG.debug("Error accessing the se-repo", e);
            } catch (EADLSyncExecption eadlSyncExecption) {
                LOG.debug("Error while syncing decisions", eadlSyncExecption);
            }
        }

        if (DeInitCommand.NAME.equals(commander.getParsedCommand())) {
            try {
                DE_INIT_COMMAND.deInit();
            } catch (IOException e) {
                LOG.debug("Error deleting files", e);
            }
        }

    }

}
