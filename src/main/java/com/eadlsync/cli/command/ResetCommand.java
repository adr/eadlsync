package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Reset the local decisions to the ones of the selected commit of the se-repo")
public class ResetCommand extends EADLSyncCommand{

    @Parameter(required = true)
    private String commitId;

    public void resetLocalChanges() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.reset(commitId);
    }

}
