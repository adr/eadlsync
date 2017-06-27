package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

import static com.eadlsync.cli.command.MergeCommand.DESCRIPTION;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = DESCRIPTION)
public class MergeCommand extends EADLSyncCommand {

    public static final String NAME = "merge";
    public static final String DESCRIPTION = "use 'eadlsync merge <commit-id>' to merge the local decisions with the decisions of the selected commit from the se-repo";

    @Parameter(required = true)
    private String commitId;

    public void merge() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.merge(commitId);
    }

}
