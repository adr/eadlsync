package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.EADLSyncExecption;
import com.eadlsync.model.config.ConfigCore;

/**
 *
 */
@Parameters(separators = "=", commandDescription = "Commits the code decisions to the se-repo")
public class CommitAndPushCommand extends EADLSyncCommand {

    @Parameter(names = "-m")
    private String message;

    @Parameter(names = "-f")
    private boolean forceOption;

    public String commitAndPush() throws EADLSyncExecption {
        ConfigCore core = config.getCore();
        String baseUrl = core.getBaseUrl();
        String projectName = core.getProjectName();
        String commitId = config.getSync().getRevisionBase();
        String id = "";
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

}
