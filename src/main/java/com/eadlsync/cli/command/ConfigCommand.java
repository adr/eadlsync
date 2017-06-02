package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Modify the eadlsync config file")
public class ConfigCommand extends EADLSyncCommand {

    @Parameter(names = "--user.name")
    private String name;

    @Parameter(names = "--user.email")
    private String email;

    @Parameter(names = "--core.url", description = "Set the base url for the se-repo, the url shall be in a format of '<host>/serepo'")
    private String baseUrl;

    @Parameter(names = "--core.project", description = "Set the project to work with, this shall match a se-repo repository name")
    private String project;

    @Parameter(names = "--sync.revision", description = "Set the latest sync commit id, this is used to indicate local changes", hidden = true)
    private String lastRevision;


    public void configure() throws IOException {
        readConfig();
        if (notBlank(name)) {
            config.getUser().setName(this.name);
        } else if (notBlank(email)) {
            config.getUser().setName(this.email);
        } else if (notBlank(baseUrl)) {
            config.getCore().setBaseUrl(this.baseUrl);
        } else if (notBlank(project)) {
            config.getCore().setProjectName(this.project);
        } else if (notBlank(lastRevision)) {
            config.getSync().setRevisionBase(this.lastRevision);
        }
        createConfigFile();
    }

    private boolean notBlank(String value) {
        return value != null && !value.isEmpty();
    }

    private void createConfigFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(EADL_CONFIG.toFile(), this.config);
    }

}
