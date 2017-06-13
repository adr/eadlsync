package com.eadlsync.model.repo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eadlsync.EADLSyncExecption;
import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.model.diff.Decisions;
import com.eadlsync.util.OS;
import com.eadlsync.util.io.JavaDecisionParser;
import com.eadlsync.util.net.APIConnector;
import com.eadlsync.util.net.SeRepoUrlObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tobias on 07/03/2017.
 */
public class CodeRepo implements IRepo {

    private final Logger LOG = LoggerFactory.getLogger(CodeRepo.class);
    private final Path repositoryPath;
    private final APIConnector connector;
    private Decisions decisions;

    public CodeRepo(Path path, String baseUrl, String project, String baseRevision) throws IOException, UnirestException {
        this.repositoryPath = path;
        SeRepoUrlObject seRepoUrlObject = new SeRepoUrlObject(baseUrl, project, baseRevision);
        this.connector = APIConnector.withSeRepoUrl(seRepoUrlObject);
        initDecisions();
    }

    private void initDecisions() throws UnirestException, IOException {
        this.decisions = new Decisions(loadBaseRevision(), loadLocalDecisions(), loadRemoteDecisions());
    }

    private List<YStatementJustificationWrapper> loadBaseRevision() throws UnirestException {
        return connector.getYStatementJustifications();
    }

    private List<YStatementJustificationWrapper> loadRemoteDecisions() throws UnirestException {
        return connector.getLatestYStatementJustifications();
    }

    private List<YStatementJustificationWrapper> loadLocalDecisions() throws IOException {
        List<YStatementJustificationWrapper> localYStatements = new ArrayList<>();
        URLClassLoader finalUrlClassLoader = getUrlClassLoader();
        Files.walk(repositoryPath, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
            if (isPathToJavaFile(path)) {
                String classPath = convertToClassPath(path);
                try {
                    Class clazz = finalUrlClassLoader.loadClass(classPath);
                    for (Annotation annotation : clazz.getAnnotationsByType(YStatementJustification
                            .class)) {
                        YStatementJustification yStatementJustification = (YStatementJustification)
                                annotation;
                        localYStatements.add(new YStatementJustificationWrapperBuilder(yStatementJustification, path.toString()).build());
                    }
                } catch (ClassNotFoundException e) {
                    LOG.debug("Could not instantiate class.", e);
                }
            }
        });
        return localYStatements;
    }

    private void writeEadsToDisk() throws IOException {
        for (YStatementJustificationWrapper yStatementJustificationWrapper : decisions.getCurrentDecisions()) {
            writeEadToClass(yStatementJustificationWrapper);
        }
        for (YStatementJustificationWrapper yStatementJustificationWrapper : decisions.getBaseDecisions().stream().filter(
                decision -> !decisions.getCurrentDecisions().contains(decision)).collect(Collectors.toList())) {
            removeEadFromClass(yStatementJustificationWrapper);
        }
    }

    private void writeEadToClass(YStatementJustificationWrapper yStatementJustification) throws IOException {
        JavaDecisionParser.writeModifiedYStatementToFile(yStatementJustification);
    }

    private void removeEadFromClass(YStatementJustificationWrapper yStatementJustification) throws IOException {
        JavaDecisionParser.removeYStatementFromFile(yStatementJustification);
    }

    private URLClassLoader getUrlClassLoader() throws MalformedURLException {
        URLClassLoader urlClassLoader;
        urlClassLoader = new URLClassLoader(new URL[]{repositoryPath.toUri().toURL()});
        return urlClassLoader;
    }

    private boolean isPathToJavaFile(Path path) {
        return path.toString().endsWith(".java") && !Files.isDirectory(path);
    }

    private String convertToClassPath(Path path) {
        if (OS.IS_WINDOWS) {
            return repositoryPath.relativize(path).toString().replace(".java", "").
                    replaceAll("\\\\", ".");
        } else {
            return repositoryPath.relativize(path).toString().replace(".java", "").
                    replaceAll(OS.FS, ".");
        }
    }

    @Override
    public String commit(String message, boolean isForcing) throws EADLSyncExecption, UnsupportedEncodingException, UnirestException {
        if (!decisions.hasRemoteDiff() || isForcing) {
            if (decisions.hasLocalDiff()) {
                decisions.applyLocalDiff();
                return connector.commitYStatement(decisions.getCurrentDecisions(), message);
            } else {
                throw EADLSyncExecption.ofState(EADLSyncExecption.EADLSyncOperationState.NOTHING_TO_COMMIT);
            }
        } else {
            throw EADLSyncExecption.ofState(EADLSyncExecption.EADLSyncOperationState.NON_FORWARD);
        }
    }

    @Override
    public void pull() throws EADLSyncExecption, IOException {
        if (decisions.hasRemoteDiff()) {
            if (decisions.hasLocalDiff()) {
                if (decisions.canAutoMerge()) {
                    decisions.applyLocalDiff();
                    decisions.applyRemoteDiff();
                    writeEadsToDisk();
                } else {
                    writeConflicts();
                    throw EADLSyncExecption.ofState(EADLSyncExecption.EADLSyncOperationState.CONFLICT);
                }
            } else {
                decisions.applyRemoteDiff();
                writeEadsToDisk();
            }
        } else {
            throw EADLSyncExecption.ofState(EADLSyncExecption.EADLSyncOperationState.UP_TO_DATE);
        }
    }

    private void writeConflicts() {
        // TODO: write conflicting decisions to a conflicts file in the eadl directory
    }

    @Override
    public void merge(String commitId) throws IOException, UnirestException, EADLSyncExecption {
        connector.changeToCommit(commitId);
        initDecisions();
        pull();
    }

    @Override
    public void reset(String commitId) throws EADLSyncExecption, IOException, UnirestException {
        connector.changeToCommit(commitId);
        initDecisions();
        decisions.applyRemoteDiff();
        writeEadsToDisk();
    }

    public RepoStatus getStatus() {
        return RepoStatus.of(decisions);
    }

}
