package io.github.adr.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import io.github.adr.eadlsync.cli.CLI;
import io.github.adr.eadlsync.cli.CommitIdValidator;
import io.github.adr.eadlsync.exception.EADLSyncException;

import static io.github.adr.eadlsync.cli.command.ResetCommand.DESCRIPTION;

/**
 * Reset command used to reset all decisions of the local code repository to the decisions of the configured se-repo project.
 *
 * @option commitId of the commit to reset to
 */
@Parameters(commandDescription = DESCRIPTION)
public class ResetCommand extends EADLSyncCommand {

    public static final String NAME = "reset";
    public static final String DESCRIPTION = "use 'eadlsync reset <commit-id>' to reset the local decisions to the decisions of the selected commit from the se-repo";

    @Parameter(required = true, description = "commit-id", validateWith = CommitIdValidator.class)
    private String commitId;

    public void resetLocalChanges() throws Exception {
        if (readConfig()) {

            initializeRepo();

            try {
                CLI.println(String.format("Reset to %s from se-repo", commitId));
                CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
                repo.reset(commitId);
                updateCommitId(commitId);
            } catch (EADLSyncException e) {
                printEadlSyncException(e);
            }
        }
    }

}
