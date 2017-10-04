package io.github.adr.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import io.github.adr.eadlsync.cli.CLI;
import io.github.adr.eadlsync.cli.CommitIdValidator;
import io.github.adr.eadlsync.exception.EADLSyncException;

import static io.github.adr.eadlsync.cli.command.MergeCommand.DESCRIPTION;

/**
 * Merge command used to merge the decisions of the local code repository with the decisions provided by the configured se-repo project.
 *
 * @option commitId of the commit to merge with
 */
@Parameters(commandDescription = DESCRIPTION)
public class MergeCommand extends EADLSyncCommand {

    public static final String NAME = "merge";
    public static final String DESCRIPTION = "use 'eadlsync merge <commit-id>' to merge the local decisions with the decisions of the selected commit from the se-repo";

    @Parameter(required = true, description = "commit-id", validateWith = CommitIdValidator.class)
    private String commitId;

    public void merge() throws Exception {
        if (readConfig()) {
            initializeRepo();

            try {
                CLI.println("Merge with se-repo");
                CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
                repo.merge(commitId);
            } catch (EADLSyncException e) {
                printEadlSyncException(e);
            }
        }
    }

}
