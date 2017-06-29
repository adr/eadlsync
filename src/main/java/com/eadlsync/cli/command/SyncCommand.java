package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.exception.EADLSyncException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.eadlsync.cli.command.SyncCommand.DESCRIPTION;

/**
 * Sync command used to sync the decisions of the local code repository with the decisions of the configured se-repo project.
 * This essentially performs a pull and afterwards a commit operation.
 *
 * @option help
 */
@Parameters(commandDescription = DESCRIPTION)
public class SyncCommand extends EADLSyncCommand {

    public static final String NAME = "sync";
    public static final String DESCRIPTION = "use 'eadlsync sync' to update the local decisions and afterwards update the decisions of the se-repo";
    private static final String SYNC_MESSAGE = "Automatic sync message" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this command", help = true)
    private boolean help = false;

    public void sync() throws Exception {
        if (readConfig()) {
            readDecisions();

            try {
                repo.pull();
            } catch (EADLSyncException eadlSyncException) {
                printEadlSyncException(eadlSyncException);
            }
            try {
                updateCommitId(repo.commit(config.getUser(), SYNC_MESSAGE, false));
            } catch (EADLSyncException eadlSyncException) {
                printEadlSyncException(eadlSyncException);
            }
        }
    }

    public boolean isHelp() {
        return help;
    }
}
