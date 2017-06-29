package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.exception.EADLSyncException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.eadlsync.cli.command.SyncCommand.DESCRIPTION;

/**
 * Sync command used to sync the decisions of the local code repository with the decisions of the configured se-repo project.
 * This essentially performs a pull and afterwards a commit operation.
 */
@Parameters(commandDescription = DESCRIPTION)
public class SyncCommand extends EADLSyncCommand {

    public static final String NAME = "sync";
    public static final String DESCRIPTION = "use 'eadlsync sync' to update the local decisions and afterwards update the decisions of the se-repo";
    private static final String SYNC_MESSAGE = "Automatic sync message" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

    public void sync() throws Exception {
        if (readConfig()) {
            readDecisions();

            pull();
            commit(SYNC_MESSAGE, false);
        }
    }

    void pull() throws Exception {
        try {
            CLI.println("Pull to from se-repo");
            CLI.println(String.format("\tproject '%s' at %s", config.getCore().getProjectName(), config.getCore().getBaseUrl()));
            String pullId = repo.pull();
            updateCommitId(pullId);
            CLI.println(String.format("\tsync id -> %s", pullId));
        } catch (EADLSyncException eadlSyncException) {
            printEadlSyncException(eadlSyncException);
        }
    }

    void commit(String message, boolean forceOption) throws Exception {
        try {
            CLI.println("Commit to se-repo");
            CLI.println(String.format("\tproject '%s' at %s", config.getCore().getProjectName(), config.getCore().getBaseUrl()));
            String newId = repo.commit(config.getUser(), message, forceOption);
            updateCommitId(newId);
            CLI.println(String.format("\tsync id -> %s", newId));
        } catch (EADLSyncException eadlSyncException) {
            printEadlSyncException(eadlSyncException);
        }
    }

}
