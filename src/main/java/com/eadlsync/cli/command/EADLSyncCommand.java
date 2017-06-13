package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import com.eadlsync.model.config.Config;
import com.eadlsync.model.repo.CodeRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by tobias on 02/06/2017.
 */
public class EADLSyncCommand {

    protected static final Path PROJECT_ROOT = Paths.get(".").toAbsolutePath().normalize();
    protected static final Path EADL_ROOT = PROJECT_ROOT.resolve(".eadlsync");
    protected static final Path EADL_CONFIG = EADL_ROOT.resolve("config");
    protected static final Path EADL_REVISION = PROJECT_ROOT.resolve(".eadlsync-commitid");
    protected static CodeRepo repo = null;

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

    private String readCommitId() throws IOException {
        if (!Files.exists(EADL_REVISION)) {
            throw new IOException("No eadlsync-commitid found, please initialize first");
        } else {
            return Files.readAllLines(EADL_REVISION).stream().collect(Collectors.joining());
        }
    }

    protected void updateConfig() throws IOException {
        // we assume we only get called in the root directory of a project
        if (!Files.exists(EADL_CONFIG)) {
            throw new IOException("No eadlsync repository, please initialize first");
        } else if (!Files.isWritable(EADL_CONFIG)) {
            throw new IOException("Could not write config file, please check permissions");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(EADL_CONFIG.toFile(), config);
    }

    protected void updateCommitId(String commitId) throws IOException {
        if (!Files.exists(EADL_REVISION)) {
            Files.createFile(EADL_REVISION);
        }
        Files.write(EADL_REVISION, commitId.getBytes());
    }

    protected void readDecisions() throws IOException, UnirestException {
        repo = new CodeRepo(PROJECT_ROOT, config.getCore().getBaseUrl(), config.getCore()
                .getProjectName(), readCommitId());
    }

    protected boolean notBlank(String value) {
        return value != null && !value.isEmpty();
    }

}
