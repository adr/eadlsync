package com.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Pull all changes from the latest commit of the se-repo")
public class PullCommand extends EADLSyncCommand {

    public static final String NAME = "pull";

    public void pull() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.pull();
    }

}
