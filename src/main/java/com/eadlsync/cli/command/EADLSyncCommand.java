package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.eadlsync.model.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by tobias on 02/06/2017.
 */
public class EADLSyncCommand {

    protected static final Path PROJECT_ROOT = Paths.get(".").toAbsolutePath().normalize();
    protected static final Path EADL_ROOT = PROJECT_ROOT.resolve(".eadlsync");
    protected static final Path EADL_CONFIG = EADL_ROOT.resolve("config");

    protected Config config = null;

    protected void readConfig() throws IOException {
        // we assume we only get called in the root directory of a project
        if (!Files.exists(EADL_CONFIG)) {
            throw new IOException("No eadlsync repository, please initialize first");
        } else if (!Files.isReadable(EADL_CONFIG)) {
            throw new IOException("Could not read config file, please check permissions");
        }
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.readValue(EADL_CONFIG.toFile(), Config.class);
    }

    protected boolean notBlank(String value) {
        return value != null && !value.isEmpty();
    }

}
