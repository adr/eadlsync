package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.exception.EADLSyncException;

import static com.eadlsync.cli.command.MergeCommand.DESCRIPTION;

/**
 * Merge command used to merge the decisions of the local code repository with the decisions provided by the configured se-repo project.
 *
 * @option help
 * @option commitId of the commit to merge with
 */
@Parameters(commandDescription = DESCRIPTION)
public class MergeCommand extends EADLSyncCommand {

    public static final String NAME = "merge";
    public static final String DESCRIPTION = "use 'eadlsync merge <commit-id>' to merge the local decisions with the decisions of the selected commit from the se-repo";

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this command", help = true)
    private boolean help = false;

    @Parameter(required = true)
    private String commitId;

    public void merge() throws Exception {
        if (readConfig()) {
            readDecisions();

            try {
                repo.merge(commitId);
            } catch (EADLSyncException e) {
                printEadlSyncException(e);
            }
        }
    }

    public boolean isHelp() {
        return help;
    }
}
