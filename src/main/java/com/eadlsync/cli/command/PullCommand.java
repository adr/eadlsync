package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.exception.EADLSyncException;

import static com.eadlsync.cli.command.PullCommand.DESCRIPTION;

/**
 * Pull command used to pull all decisions of the configured se-repo project.
 *
 * @option help
 */
@Parameters(commandDescription = DESCRIPTION)
public class PullCommand extends EADLSyncCommand {

    public static final String NAME = "pull";
    public static final String DESCRIPTION = "use 'eadlsync pull' to update the local decisions";

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this command", help = true)
    private boolean help = false;

    public void pull() throws Exception {
        if (readConfig()) {
            readDecisions();

            try {
                repo.pull();
            } catch (EADLSyncException e) {
                printEadlSyncException(e);
            }
        }
    }

    public boolean isHelp() {
        return help;
    }
}
