package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.exception.EADLSyncException;

/**
 * Commit command used to commit all decisions of the local code repository to the configured se-repo project.
 *
 * @option help
 * @option message the commit message
 * @option forceOption the option to force a commit
 */
@Parameters(commandDescription = CommitCommand.DESCRIPTION)
public class CommitCommand extends EADLSyncCommand {

    public static final String NAME = "commit";
    public static final String DESCRIPTION = "use 'eadlsync commit -m <message>' to update the decisions in the se-repo";

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this command", help = true)
    private boolean help = false;

    @Parameter(names = {"-m", "--message"}, required = true)
    private String message;

    @Parameter(names = {"-f", "--force"})
    private boolean forceOption;


    public void commit() throws Exception {
        if (readConfig()) {

            readDecisions();

            try {
                updateCommitId(repo.commit(config.getUser(), message, forceOption));
            } catch (EADLSyncException eadlSyncException) {
                printEadlSyncException(eadlSyncException);
            }
        }
    }

    public boolean isHelp() {
        return help;
    }
}
