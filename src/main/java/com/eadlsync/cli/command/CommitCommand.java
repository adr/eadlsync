package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Commit command used to commit all decisions of the local code repository to the configured se-repo project.
 *
 * @option message the commit message
 * @option forceOption the option to force a commit
 */
@Parameters(commandDescription = CommitCommand.DESCRIPTION)
public class CommitCommand extends SyncCommand {

    public static final String NAME = "commit";
    public static final String DESCRIPTION = "use 'eadlsync commit -m <message>' to update the decisions in the se-repo";

    @Parameter(names = {"-m", "--message"}, required = true, description = "the commit message that appears in the se-repo")
    private String message;

    @Parameter(names = {"-f", "--force"}, description = "force a commit even if the se-repo has changes")
    private boolean forceOption;


    public void commit() throws Exception {
        if (readConfig()) {

            readDecisions();

            super.commit(this.message, this.forceOption);
        }
    }

}
