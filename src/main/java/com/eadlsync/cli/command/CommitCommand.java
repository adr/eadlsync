package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncException;
import com.eadlsync.cli.CLI;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

/**
 *
 */
@Parameters(separators = "=", commandDescription = CommitCommand.DESCRIPTION)
public class CommitCommand extends EADLSyncCommand {

    public static final String NAME = "commit";
    public static final String DESCRIPTION = "use 'eadlsync commit -m=<message>' to update the decisions in the se-repo";

    @Parameter(names = "-m", required = true)
    private String message;

    @Parameter(names = "-f")
    private boolean forceOption;


    public void commit() throws IOException, UnirestException {
        if (readConfig()) {

            readDecisions();

            try {
                updateCommitId(repo.commit(config.getUser(), message, forceOption));
            } catch (EADLSyncException eadlSyncException) {
                printEadlSyncException(eadlSyncException);
            }
        }
    }

}
