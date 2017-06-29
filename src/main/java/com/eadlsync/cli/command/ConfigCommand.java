package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;

import static com.eadlsync.cli.command.ConfigCommand.DESCRIPTION;

/**
 * Config command used to update the eadlsync configuration file.
 *
 * @option help
 * @option name with which the program commits
 * @option email with which the program commits
 * @option root specifies the project root on the local file system
 * @option baseUrl of the se-repo, has to end with /serepo
 * @option project name of the se-repo project
 */
@Parameters(separators = "=", commandDescription = DESCRIPTION)
public class ConfigCommand extends EADLSyncCommand {

    public static final String NAME = "config";
    public static final String DESCRIPTION = "use 'eadlsync config --[user|core].<key>=<value>' to update the decisions in the se-repo";

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this command", help = true)
    private boolean help = false;

    @Parameter(names = "--user.name")
    private String name;

    @Parameter(names = "--user.email")
    private String email;

    @Parameter(names = "--core.root", description = "Set the root directory of the code base", hidden = true)
    private String root;

    @Parameter(names = "--core.url", description = "Set the base url for the se-repo, the url shall be in a format of '<host>/serepo'")
    private String baseUrl;

    @Parameter(names = "--core.project", description = "Set the project to work with, this shall match a se-repo repository name")
    private String project;

    public void configure() throws IOException {
        if (readConfig()) {
            if (notBlank(name)) {
                config.getUserConfig().setName(this.name);
            } else if (notBlank(email)) {
                config.getUserConfig().setName(this.email);
            } else if (notBlank(root)) {
                config.getCore().setProjectRoot(this.root);
            } else if (notBlank(baseUrl)) {
                config.getCore().setBaseUrl(this.baseUrl);
            } else if (notBlank(project)) {
                config.getCore().setProjectName(this.project);
            }
            updateConfig();
        }
    }

    public boolean isHelp() {
        return help;
    }
}
