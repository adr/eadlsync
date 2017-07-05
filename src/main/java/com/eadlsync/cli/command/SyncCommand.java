package com.eadlsync.cli.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.hsr.isf.serepo.data.restinterface.common.User;
import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.exception.EADLSyncException;

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

            final String oldCommitId= readCommitId();
            pull();

            boolean forceNeeded = !oldCommitId.equals(readCommitId());
            commit(SYNC_MESSAGE, forceNeeded);
        }
    }

    void pull() throws Exception {
        try {
            CLI.println("Pull from se-repo");
            CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
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
            CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
            User user = new User(config.getConfigUser().getName(), config.getConfigUser().getEmail());
            String newId = repo.commit(user, message, forceOption);
            updateCommitId(newId);
            CLI.println(String.format("\tsync id -> %s", newId));
        } catch (EADLSyncException eadlSyncException) {
            printEadlSyncException(eadlSyncException);
        }
    }

}
