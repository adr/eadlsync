package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.exception.EADLSyncException;

import static com.eadlsync.cli.command.MergeCommand.DESCRIPTION;

/**
 * Merge command used to merge the decisions of the local code repository with the decisions provided by the configured se-repo project.
 *
 * @option commitId of the commit to merge with
 */
@Parameters(commandDescription = DESCRIPTION)
public class MergeCommand extends EADLSyncCommand {

    public static final String NAME = "merge";
    public static final String DESCRIPTION = "use 'eadlsync merge <commit-id>' to merge the local decisions with the decisions of the selected commit from the se-repo";

    @Parameter(required = true, description = "the commit id used to merge the local decisions with")
    private String commitId;

    public void merge() throws Exception {
        if (readConfig()) {
            readDecisions();

            try {
                CLI.println("Merge with se-repo");
                CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
                repo.merge(commitId);
                CLI.println(String.format("\tsync id -> %s", readCommitId()));
            } catch (EADLSyncException e) {
                printEadlSyncException(e);
            }
        }
    }

}
