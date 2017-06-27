package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

import static com.eadlsync.cli.command.ResetCommand.DESCRIPTION;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = DESCRIPTION)
public class ResetCommand extends EADLSyncCommand {

    public static final String NAME = "reset";
    public static final String DESCRIPTION = "use 'eadlsync reset <commit-id>' to reset the local decisions to the decisions of the selected commit from the se-repo";

    @Parameter(required = true)
    private String commitId;

    public void resetLocalChanges() throws IOException, UnirestException, EADLSyncExecption {
        readConfig();
        readDecisions();
        repo.reset(commitId);
    }

}
