package com.eadlsync.cli.command;

import java.io.IOException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Modify the eadlsync config file")
public class ConfigCommand extends EADLSyncCommand {

    @Parameter(names = "--user.name")
    private String name;

    @Parameter(names = "--user.email")
    private String email;

    @Parameter(names = "--core.root", description = "Set the root directory of the code base",  hidden = true)
    private String root;

    @Parameter(names = "--core.url", description = "Set the base url for the se-repo, the url shall be in a format of '<host>/serepo'")
    private String baseUrl;

    @Parameter(names = "--core.project", description = "Set the project to work with, this shall match a se-repo repository name")
    private String project;

    public void configure() throws IOException {
        readConfig();
        if (notBlank(name)) {
            config.getUser().setName(this.name);
        } else if (notBlank(email)) {
            config.getUser().setName(this.email);
        } else if (notBlank(root)) {
            config.getCore().setProjectRoot(this.root);
        } else if (notBlank(baseUrl)) {
            config.getCore().setBaseUrl(this.baseUrl);
        } else if (notBlank(project)) {
            config.getCore().setProjectName(this.project);
        }
        createConfigFile();
    }

    private void createConfigFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(EADL_CONFIG.toFile(), this.config);
    }

}
