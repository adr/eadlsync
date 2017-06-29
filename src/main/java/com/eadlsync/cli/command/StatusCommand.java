package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.model.repo.RepoStatus;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

import static com.eadlsync.cli.command.StatusCommand.DESCRIPTION;

/**
 * Status command used to display the current status of the eadl-sync repository.
 *
 * @option help
 */
@Parameters(commandDescription = DESCRIPTION)
public class StatusCommand extends EADLSyncCommand {

    public static final String NAME = "status";
    public static final String DESCRIPTION = "use 'eadlsync status' to show the status of the eadlsync";

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this command", help = true)
    private boolean help = false;

    public void printStatus() throws IOException, UnirestException {
        if (readConfig()) {

            readDecisions();

            printStatusMessage(repo.status());
        }
    }

    private void printStatusMessage(RepoStatus status) {
        CLI.println(String.format("Working with se-repo %s.", config.getCore().getProjectName()));
        if (!status.isLocalChanged()) {
            if (!status.isRemoteChanged()) {
                printUpToDate();
            } else {
                printHasToPull();
            }
        } else {
            if (!status.isRemoteChanged()) {
                CLI.println("The local decisions have changed.");
                CLI.println(CommitCommand.class);
                CLI.newLine();
                status.getChangedDecisionsIds().forEach(id -> CLI.println(String.format("\t\t\tmodified:\t\t%s", id)));
                CLI.newLine();
                status.getDeletedDecisionsIds().forEach(id -> CLI.println(String.format("\t\t\tdeleted:\t\t%s", id)));
                CLI.newLine();
            } else {
                if (status.isAutoMerge()) {
                    printHasToSyncNoConflict();
                } else {
                    printHasToSyncConflict();
                }
            }
        }
    }

    public boolean isHelp() {
        return help;
    }
}
