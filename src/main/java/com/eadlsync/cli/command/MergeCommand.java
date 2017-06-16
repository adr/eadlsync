package com.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Merge the local decisions with the decisions of the selected commit of the se-repo")
public class MergeCommand extends EADLSyncCommand {

    public static final String NAME = "merge";

    @Parameter(required = true)
    private String commitId;

    public void merge() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.merge(commitId);
    }

}
