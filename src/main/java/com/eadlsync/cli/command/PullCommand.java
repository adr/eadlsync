package com.eadlsync.cli.command;

import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Pull all changes from the latest commit of the se-repo")
public class PullCommand extends EADLSyncCommand {

    public void pull() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.pull();
    }

}
