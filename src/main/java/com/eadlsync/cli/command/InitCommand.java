package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.model.config.Config;
import com.eadlsync.model.config.ConfigCore;
import com.eadlsync.model.config.ConfigUser;
import com.eadlsync.util.net.SeRepoConector;
import com.eadlsync.util.net.SeRepoUrlObject;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Initializes an eadl code repository")
public class InitCommand extends EADLSyncCommand {

    private SeRepoUrlObject seRepoUrlObject = null;

    @Parameter(names = "-r", required = true)
    private String commitUrl;

    public void initialize() throws IOException, UnirestException {
        if (Files.exists(EADL_ROOT)) {
            throw new IOException("EadlSync root directory already exists for this project");
        } else {
            seRepoUrlObject = SeRepoUrlObject.ofCommmitUrl(this.commitUrl);

            Files.createDirectory(EADL_ROOT);
            createConfigFile(EADL_CONFIG);
            updateCommitId(SeRepoConector.getLatestCommit(seRepoUrlObject.SEREPO_URL_COMMITS));
        }
    }

    private void createConfigFile(Path filePath) throws IOException {
        Files.createFile(filePath);
        config = createConfig();
        updateConfig();
    }

    private Config createConfig() {
        ConfigUser user = new ConfigUser();
        user.setName("EADLSynchronizer");
        user.setEmail("eadl@sync.com");

        ConfigCore core = new ConfigCore();
        core.setProjectRoot(PROJECT_ROOT.toString());
        core.setBaseUrl(seRepoUrlObject.SEREPO_BASE_URL);
        core.setProjectName(seRepoUrlObject.SEREPO_PROJECT);

        Config config = new Config();
        config.setCore(core);
        config.setUser(user);
        return config;
    }

}
