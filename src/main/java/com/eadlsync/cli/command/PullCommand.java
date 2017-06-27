package com.eadlsync.cli.command;

import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

import static com.eadlsync.cli.command.PullCommand.DESCRIPTION;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = DESCRIPTION)
public class PullCommand extends EADLSyncCommand {

    public static final String NAME = "pull";
    public static final String DESCRIPTION = "use 'eadlsync pull' to update the local decisions";

    public void pull() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.pull();
    }

}
