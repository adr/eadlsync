package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

/**
 *
 */
@Parameters(separators = "=", commandDescription = "Commits the code decisions to the se-repo")
public class CommitAndPushCommand extends EADLSyncCommand {

    @Parameter(names = "-m", required = true)
    private String message;

    @Parameter(names = "-f")
    private boolean forceOption;

    public String commitAndPush() throws EADLSyncExecption, IOException, UnirestException {
        readConfig();
        readDecisions();
        String newCommitId = repo.commit(message, forceOption);
        config.getSync().setRevisionBase(newCommitId);
        updateConfig();
        return newCommitId;
    }

}
