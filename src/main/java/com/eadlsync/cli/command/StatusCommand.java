package com.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.model.repo.RepoStatus;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.cli.command.StatusCommand.DESCRIPTION;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = DESCRIPTION)
public class StatusCommand extends EADLSyncCommand {

    private final Logger LOG = LoggerFactory.getLogger(StatusCommand.class);
    private RepoStatus status;

    public static final String NAME = "status";
    public static final String DESCRIPTION = "use 'eadlsync status' to show the status of the eadlsync";

    public void printStatus() {
        try {
            readConfig();
        } catch (IOException e) {
            printNotInitialized();
            LOG.debug("Failed to read config file", e);
        }
        try {
            readDecisions();
        } catch (IOException e) {
            LOG.debug("Failed to read decisions of local code base", e);
        } catch (UnirestException e) {
            LOG.debug("Failed to read decisions from the se-repo", e);
        }
        status = repo.getStatus();
        printStatusMessage();
    }

    private void printStatusMessage() {
        CLI.println(String.format("Working with se-repo %s.", config.getCore().getProjectName()));
        if (!status.isLocalChanged()) {
            if (!status.isRemoteChanged()) {
                CLI.println("The local decisions are in sync with the decisions of the se-repo.");
            } else {
                CLI.println("The decisions of the se-repo have changed.");
                CLI.println(PullCommand.class);
            }
        } else {
            if (!status.isRemoteChanged()) {
                CLI.println("The local decisions have changed.");
                CLI.println(CommitCommand.class);
                CLI.newLine();
                status.getChangedDecisionsIds().forEach(id -> {
                    CLI.println(String.format("\t\t\tmodified:\t\t", id));
                });
                CLI.newLine();
                status.getDeletedDecisionsIds().forEach(id -> {
                    CLI.println(String.format("\t\t\tdeleted:\t\t", id));
                });
                CLI.newLine();
            } else {
                if (status.isAutoMerge()) {
                    CLI.println("The local decisions and the ones in the se-repo have both changed and can be automatically merged.");
                    CLI.println(SyncCommand.class);
                } else {
                    CLI.println("The local decisions and the ones in the se-repo have both changed but you have to resolve merge conflicts.");
                    CLI.println(SyncCommand.class);
                }
            }
        }
    }

    public void printNotInitialized() {
        CLI.println("Eadlsync not initialized in this directory.");
        CLI.println(InitCommand.class);
    }

}
