package com.eadlsync.cli.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.cli.command.SyncCommand.DESCRIPTION;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = DESCRIPTION)
public class SyncCommand extends EADLSyncCommand {

    public static final String NAME = "sync";
    public static final String DESCRIPTION = "use 'eadlsync sync' to update the local decisions and afterwards update the decisions of the se-repo";
    private static final Logger LOG = LoggerFactory.getLogger(SyncCommand.class);

    private final String SYNC_MESSAGE = "Automatic sync " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

    public void sync() throws IOException, UnirestException {
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

}
