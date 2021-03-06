package io.github.adr.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.Parameters;
import io.github.adr.eadlsync.cli.CLI;
import io.github.adr.eadlsync.model.repo.RepoStatus;
import com.mashape.unirest.http.exceptions.UnirestException;

import static io.github.adr.eadlsync.cli.command.StatusCommand.DESCRIPTION;

/**
 * Status command used to display the current status of the eadl-sync repository.
 */
@Parameters(commandDescription = DESCRIPTION)
public class StatusCommand extends EADLSyncCommand {

    public static final String NAME = "status";
    public static final String DESCRIPTION = "use 'eadlsync status' to show the status of the eadlsync";

    public void printStatus() throws IOException, UnirestException {
        if (readConfig()) {

            initializeRepo();

            printStatusMessage(repo.status());
        }
    }

    private void printStatusMessage(RepoStatus status) {
        CLI.println(String.format("Status of the embedded architectural decisions at %s.", config.getConfigCore().getProjectRoot()));
        CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
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

}
