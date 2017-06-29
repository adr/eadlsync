package com.eadlsync.cli.command;

import com.beust.jcommander.Parameters;

import static com.eadlsync.cli.command.PullCommand.DESCRIPTION;

/**
 * Pull command used to pull all decisions of the configured se-repo project.
 */
@Parameters(commandDescription = DESCRIPTION)
public class PullCommand extends SyncCommand {

    public static final String NAME = "pull";
    public static final String DESCRIPTION = "use 'eadlsync pull' to update the local decisions";

    public void pull() throws Exception {
        if (readConfig()) {
            readDecisions();

            super.pull();
        }
    }

}
