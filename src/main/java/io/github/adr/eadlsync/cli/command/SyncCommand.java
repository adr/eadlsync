package io.github.adr.eadlsync.cli.command;

import ch.hsr.isf.serepo.data.restinterface.common.User;
import com.beust.jcommander.Parameters;
import io.github.adr.eadlsync.cli.CLI;
import io.github.adr.eadlsync.exception.EADLSyncException;

import static io.github.adr.eadlsync.cli.command.SyncCommand.DESCRIPTION;

/**
 * Sync command used to sync the decisions of the local code repository with the decisions of the configured se-repo project.
 * This essentially performs a pull and afterwards a commit operation.
 */
@Parameters(commandDescription = DESCRIPTION)
public class SyncCommand extends EADLSyncCommand {

    public static final String NAME = "sync";
    public static final String DESCRIPTION = "use 'eadlsync sync' to update the local decisions and afterwards update the decisions of the se-repo";
    private static final String SYNC_MESSAGE = "automatic sync";

    public void sync() throws Exception {
        if (readConfig()) {
            initializeRepo();

            final String oldCommitId = readCommitId();
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
        } catch (EADLSyncException eadlSyncException) {
            printEadlSyncException(eadlSyncException);
        }
    }

}
