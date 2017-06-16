package com.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.Parameters;
import com.eadlsync.model.repo.RepoStatus;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Shows the status of the eadl repository")
public class StatusCommand extends EADLSyncCommand {

    public static final String NAME = "status";

    public RepoStatus getStatus() throws IOException, UnirestException {
        readConfig();
        readDecisions();
        return repo.getStatus();
    }

}
