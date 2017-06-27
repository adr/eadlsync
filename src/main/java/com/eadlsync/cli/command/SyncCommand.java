package com.eadlsync.cli.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.eadlsync.EADLSyncExecption.EADLSyncOperationState;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Pulls the latest changes and commits any local changes afterwards")
public class SyncCommand extends EADLSyncCommand {

    public static final String NAME = "sync";
    public static final String DESCRIPTION = "use 'eadlsync sync' to update the local decisions and afterwards update the decisions of the se-repo";
    private static final Logger LOG = LoggerFactory.getLogger(SyncCommand.class);

    private final String SYNC_MESSAGE = "Automatic sync " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());

    public String sync() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        try {
            repo.pull();
        } catch (EADLSyncExecption eadlSyncExecption) {
            if (eadlSyncExecption.getState() == EADLSyncOperationState.CONFLICT) {
                throw EADLSyncExecption.ofState(EADLSyncOperationState.SYNC_FAILED);
            }
        }
        try {
            return repo.commit(SYNC_MESSAGE, false);
        } catch (EADLSyncExecption eadlSyncExecption) {
            LOG.debug("Error while committing after pulling", eadlSyncExecption);
        }
        return "";
    }

}
