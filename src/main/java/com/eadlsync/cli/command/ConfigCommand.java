package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import static com.eadlsync.cli.command.ConfigCommand.DESCRIPTION;

/**
 * Config command used to update the eadlsync configuration file.
 *
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

    @Parameter(names = "--user.name", description = "change the commit name of this program")
    private String name;

    @Parameter(names = "--user.email", description = "change the commit email of this program")
    private String email;

    @Parameter(names = "--core.root", description = "set the root directory of the code base")
    private String root;

    @Parameter(names = "--core.url", description = "set the base url for the se-repo, the url shall be in a format of '<host>/serepo' or 'localhost'")
    private String baseUrl;

    @Parameter(names = "--core.project", description = "set the project to work with, this shall match a se-repo repository name")
    private String project;

    public void configure() throws IOException {
        if (readConfig()) {
            if (notBlank(name)) {
                config.getConfigUser().setName(this.name);
            } else if (notBlank(email)) {
                config.getConfigUser().setName(this.email);
            } else if (notBlank(root)) {
                Path absolute = Paths.get(root);
                if (!absolute.isAbsolute()) root = PROJECT_ROOT.resolve(root).toString();
                config.getConfigCore().setProjectRoot(this.root);
            } else if (notBlank(baseUrl)) {
                baseUrl = baseUrl.contains("localhost") ? "http://localhost:8080/serepo" : baseUrl;
                config.getConfigCore().setBaseUrl(this.baseUrl);
            } else if (notBlank(project)) {
                config.getConfigCore().setProjectName(this.project);
            }
            updateConfig();
        }
    }

}
