package com.eadlsync.model.repo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import ch.hsr.isf.serepo.data.restinterface.common.User;
import com.eadlsync.exception.EADLSyncException;
import com.eadlsync.gui.ConflictManagerView;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.diff.DiffManager;
import com.eadlsync.util.io.JavaDecisionParser;
import com.eadlsync.util.net.SeRepoConector;
import com.eadlsync.util.net.YStatementSeItemHelper;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 *
 */
public class CodeRepo implements IRepo {

    private final Path repositoryPath;
    private final String baseUrl;
    private final String project;
    private final String lastSyncId;
    private DiffManager diffManager;

    public CodeRepo(Path path, String baseUrl, String project, String baseRevision) throws IOException, UnirestException {
        this.repositoryPath = path;
        this.baseUrl = baseUrl;
        this.project = project;
        this.lastSyncId = baseRevision;
    }

    private DiffManager initDiff(String commitIdToSyncWith) throws UnirestException, IOException {
        return new DiffManager(loadBaseRevision(), loadLocalDecisions(), loadRemoteDecisions(commitIdToSyncWith));
    }

    private List<YStatementJustificationWrapper> loadBaseRevision() throws UnirestException {
        return YStatementSeItemHelper.getYStatementJustifications(baseUrl, project, lastSyncId);
    }

    private List<YStatementJustificationWrapper> loadRemoteDecisions(String commitIdToSyncWith) throws UnirestException {
        return YStatementSeItemHelper.getYStatementJustifications(baseUrl, project, commitIdToSyncWith);
    }

    private List<YStatementJustificationWrapper> loadLocalDecisions() throws IOException {
        return JavaDecisionParser.readYStatementsFromDirectory(repositoryPath);
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

    @Override
    public String commit(User user, String message, boolean isForcing) throws EADLSyncException, IOException, UnirestException {
        diffManager = initDiff(latestCommitId());
        if (!diffManager.hasRemoteDiff() || isForcing) {
            if (diffManager.hasLocalDiff()) {
                diffManager.applyLocalDiff();
                return YStatementSeItemHelper.commitYStatement(user, message, diffManager.getCurrentDecisions(), baseUrl, project);
            } else {
                throw EADLSyncException.ofState(EADLSyncException.EADLSyncOperationState.SYNCED);
            }
        } else {
            throw EADLSyncException.ofState(EADLSyncException.EADLSyncOperationState.PULL_FIRST);
        }
    }

    private String latestCommitId() throws UnirestException {
        return SeRepoConector.getLatestCommitId(baseUrl, project);
    }

    @Override
    public String pull() throws EADLSyncException, IOException, UnirestException {
        return merge(latestCommitId());
    }

    @Override
    public String merge(String mergeCommitId) throws IOException, UnirestException, EADLSyncException {
        diffManager = initDiff(mergeCommitId);
        if (diffManager.hasRemoteDiff()) {
            if (diffManager.hasLocalDiff()) {
                if (!diffManager.canAutoMerge()) {
                    diffManager.applyNonConflictingLocalAndRemoteDiff();
                    if (new ConflictManagerView(diffManager).showDialog()) {
                        writeEadsToDisk();
                    } else {
                        return lastSyncId;
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
        return mergeCommitId;
    }

    @Override
    public String reset(String resetCommitId) throws EADLSyncException, IOException, UnirestException {
        DiffManager diffManager = initDiff(resetCommitId);
        diffManager.getLocalDiff().clear();
        diffManager.applyRemoteDiff();
        writeEadsToDisk();
        return resetCommitId;
    }

    public RepoStatus status() throws IOException, UnirestException {
        diffManager = initDiff(latestCommitId());
        return new RepoStatus(diffManager);
    }

}
