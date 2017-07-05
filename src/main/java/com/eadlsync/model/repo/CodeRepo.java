package com.eadlsync.model.repo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.hsr.isf.serepo.data.restinterface.common.User;
import com.eadlsync.exception.EADLSyncException;
import com.eadlsync.gui.ConflictManagerView;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.diff.DiffManager;
import com.eadlsync.util.io.JavaDecisionParser;
import com.eadlsync.util.net.SeRepoUrlObject;
import com.eadlsync.util.net.YStatementAPI;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tobias on 07/03/2017.
 */
public class CodeRepo implements IRepo {

    private final Logger LOG = LoggerFactory.getLogger(CodeRepo.class);
    private final Path repositoryPath;
    private final YStatementAPI connector;
    private DiffManager diffManager;

    public CodeRepo(Path path, String baseUrl, String project, String baseRevision) throws IOException, UnirestException {
        this.repositoryPath = path;
        SeRepoUrlObject seRepoUrlObject = new SeRepoUrlObject(baseUrl, project, baseRevision);
        this.connector = YStatementAPI.withSeRepoUrl(seRepoUrlObject);
        initDecisions();
    }

    private void initDecisions() throws UnirestException, IOException {
        this.diffManager = new DiffManager(loadBaseRevision(), loadLocalDecisions(), loadRemoteDecisions());
    }

    private List<YStatementJustificationWrapper> loadBaseRevision() throws UnirestException {
        return connector.getYStatementJustifications();
    }

    private List<YStatementJustificationWrapper> loadRemoteDecisions() throws UnirestException {
        return connector.getLatestYStatementJustifications();
    }

    private List<YStatementJustificationWrapper> loadLocalDecisions() throws IOException {
        List<YStatementJustificationWrapper> localYStatements = new ArrayList<>();
        Files.walk(repositoryPath, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
            if (isPathToJavaFile(path)) {
                try {
                    localYStatements.add(JavaDecisionParser.readYStatementFromFile(path));
                } catch (IOException e) {
                    LOG.debug("Failed to read annotations, skipping file {}", path);
                }
            }
        });
        return localYStatements;
    }

    private void writeEadsToDisk() throws IOException {
        for (YStatementJustificationWrapper yStatementJustificationWrapper : diffManager.getCurrentDecisions()) {
            writeEadToClass(yStatementJustificationWrapper);
        }
        for (YStatementJustificationWrapper yStatementJustificationWrapper : diffManager.getBaseDecisions().stream().filter(
                decision -> !diffManager.getCurrentDecisions().contains(decision)).collect(Collectors.toList())) {
            removeEadFromClass(yStatementJustificationWrapper);
        }
        /** so far no additional decisions are supported **/
    }

    private void writeEadToClass(YStatementJustificationWrapper yStatementJustification) throws IOException {
        JavaDecisionParser.writeModifiedYStatementToFile(yStatementJustification);
    }

    private void removeEadFromClass(YStatementJustificationWrapper yStatementJustification) throws IOException {
        JavaDecisionParser.removeYStatementFromFile(yStatementJustification);
    }

    private boolean isPathToJavaFile(Path path) {
        return path.toString().endsWith(".java") && !Files.isDirectory(path);
    }

    @Override
    public String commit(User user, String message, boolean isForcing) throws EADLSyncException, UnsupportedEncodingException {
        if (!diffManager.hasRemoteDiff() || isForcing) {
            if (diffManager.hasLocalDiff()) {
                diffManager.applyLocalDiff();
                return connector.commitYStatement(user, message, diffManager.getCurrentDecisions());
            } else {
                throw EADLSyncException.ofState(EADLSyncException.EADLSyncOperationState.SYNCED);
            }
        } else {
            throw EADLSyncException.ofState(EADLSyncException.EADLSyncOperationState.PULL_FIRST);
        }
    }

    @Override
    public String pull() throws EADLSyncException, IOException, UnirestException {
        if (diffManager.hasRemoteDiff()) {
            if (diffManager.hasLocalDiff()) {
                if (!diffManager.canAutoMerge()) {
                    diffManager.applyNonConflictingLocalAndRemoteDiff();
                    if (new ConflictManagerView(diffManager).showDialog()) {
                        writeEadsToDisk();
                    }
                } else {
                    diffManager.applyLocalDiff();
                    diffManager.applyRemoteDiff();
                    writeEadsToDisk();
                }
            } else {
                diffManager.applyRemoteDiff();
                writeEadsToDisk();
            }
        } else {
            if (diffManager.hasLocalDiff()) {
                throw EADLSyncException.ofState(EADLSyncException.EADLSyncOperationState.COMMIT);
            } else {
                throw EADLSyncException.ofState(EADLSyncException.EADLSyncOperationState.UP_TO_DATE);
            }
        }
        return connector.getLatestCommitId();
    }

    @Override
    public String merge(String mergeCommitId) throws IOException, UnirestException, EADLSyncException {
        connector.changeToCommit(mergeCommitId);
        initDecisions();
        pull();
        return mergeCommitId;
    }

    @Override
    public String reset(String resetCommitId) throws EADLSyncException, IOException, UnirestException {
        connector.changeToCommit(resetCommitId);
        initDecisions();
        diffManager.applyRemoteDiff();
        writeEadsToDisk();
        return resetCommitId;
    }

    public RepoStatus status() {
        return new RepoStatus(diffManager);
    }

}
