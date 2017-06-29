package com.eadlsync.cli.command;

import com.eadlsync.cli.CLI;
import com.eadlsync.exception.EADLSyncException;
import com.eadlsync.model.config.Config;
import com.eadlsync.model.repo.CodeRepo;
import com.eadlsync.model.repo.IRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

/**
 * Super class for any eadl-sync commands. Provides methods to read/write the config and the last commit id.
 */
class EADLSyncCommand {

    private static final Logger LOG = LoggerFactory.getLogger(EADLSyncCommand.class);
    static final Path PROJECT_ROOT = Paths.get(".").toAbsolutePath().normalize();
    static final Path EADL_ROOT = PROJECT_ROOT.resolve(".eadlsync");
    static final Path EADL_CONFIG = EADL_ROOT.resolve("config");
    static final Path EADL_REVISION = PROJECT_ROOT.resolve(".eadlsync-commitid");
    IRepo repo = null;
    Config config = null;

    boolean readConfig() throws IOException {
        // we assume we only get called in the root directory of a project
        if (!Files.exists(EADL_CONFIG)) {
            printNotInitialized();
            return false;
        } else if (!Files.isReadable(EADL_CONFIG)) {
            CLI.println("Could not read config file, please check permissions.");
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.config = mapper.readValue(EADL_CONFIG.toFile(), Config.class);
        } catch (IOException e) {
            LOG.error("Error reading eadl config file.", e);
            throw e;
        }
        return true;
    }

    String readCommitId() throws IOException {
        if (!Files.exists(EADL_REVISION)) {
            printNotInitialized();
            return "";
        } else {
            return Files.readAllLines(EADL_REVISION).stream().collect(Collectors.joining());
        }
    }

    void updateConfig() throws IOException {
        // we assume we only get called in the root directory of a project
        if (!Files.exists(EADL_CONFIG)) {
            printNotInitialized();
            return;
        } else if (!Files.isWritable(EADL_CONFIG)) {
            CLI.println("Could not write config file, please check permissions.");
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(EADL_CONFIG.toFile(), config);
        } catch (IOException e) {
            LOG.error("Error writing eadl config to file.", e);
        }
    }

    void updateCommitId(String commitId) throws IOException{
        try {
            Files.write(EADL_REVISION, commitId.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            LOG.error("Error writing new commit revision to file.", e);
            throw e;
        }
    }

    void readDecisions() throws IOException, UnirestException {
        repo = new CodeRepo(PROJECT_ROOT, config.getCore().getBaseUrl(), config.getCore()
                .getProjectName(), readCommitId());

    }

    boolean notBlank(String value) {
        return value != null && !value.isEmpty();
    }

    private void printNotInitialized() {
        CLI.println("Eadlsync not initialized in this directory.");
        CLI.println(InitCommand.class);
    }

    void printHasToSyncNoConflict() {
        CLI.println("The local decisions and the ones in the se-repo have both changed and can be automatically merged.");
        CLI.println(SyncCommand.class);
    }

    void printHasToSyncConflict() {
        CLI.println("The local decisions and the ones in the se-repo have both changed but you have to resolve merge conflicts.");
        CLI.println(SyncCommand.class);
    }

    void printHasToPull() {
        CLI.println("The decisions of the se-repo have changed.");
        CLI.println(PullCommand.class);
    }

    void printUpToDate() {
        CLI.println("The local decisions are in sync with the decisions of the se-repo.");
    }

    void printEadlSyncException(EADLSyncException execption) {
        switch (execption.getState()) {
            case CONFLICT:
                printHasToSyncConflict();
                break;
            case PULL_FIRST:
                printHasToPull();
                break;
            case UP_TO_DATE:
                printUpToDate();
                break;
            case SYNCED:
                printUpToDate();
                break;
            case NONE:
            default:
                CLI.println(execption.getMessage());
                break;
        }
    }

}
