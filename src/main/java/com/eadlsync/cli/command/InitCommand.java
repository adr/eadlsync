package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.model.config.Config;
import com.eadlsync.model.config.ConfigCore;
import com.eadlsync.model.config.ConfigSync;
import com.eadlsync.model.config.ConfigUser;
import com.eadlsync.util.net.SeRepoUrlObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by tobias on 01/06/2017.
 */
@Parameters(separators = "=", commandDescription = "Initializes an eadl code repository")
public class InitCommand extends EADLSyncCommand {

    @Parameter(names = "-r", required = true)
    private String commitUrl;

    public void initialize() throws IOException {
        if (Files.exists(EADL_ROOT)) {
            throw new IOException("EadlSync root directory already exists for this project");
        } else {
            Files.createDirectory(EADL_ROOT);
            createConfigFile(EADL_CONFIG);
        }
    }

    private void createConfigFile(Path filePath) throws IOException {
        Files.createFile(filePath);
        Config config = createConfig();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(filePath.toFile(), config);
    }

    private Config createConfig() {
        ConfigUser user = new ConfigUser();
        user.setName("EADLSynchronizer");
        user.setEmail("eadl@sync.com");

        SeRepoUrlObject seRepoUrlObject = SeRepoUrlObject.ofCommmitUrl(this.commitUrl);

        ConfigCore core = new ConfigCore();
        core.setBaseUrl(seRepoUrlObject.SEREPO_BASE_URL);
        core.setProjectName(seRepoUrlObject.SEREPO_PROJECT);

        ConfigSync sync = new ConfigSync();
        sync.setRevisionBase(seRepoUrlObject.SEREPO_COMMIT_ID);

        Config config = new Config();
        config.setCore(core);
        config.setUser(user);
        config.setSync(sync);
        return config;
    }

}
