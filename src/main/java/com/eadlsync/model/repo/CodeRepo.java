package com.eadlsync.model.repo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.model.diff.DiffObject;
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
public class CodeRepo extends ARepo {

    private final Logger LOG = LoggerFactory.getLogger(CodeRepo.class);
    private final Path repositoryPath;
    private final DiffObject diffObject;
    private final APIConnector connector;
    private Map<String, String> classPaths = new HashMap<>();

    public CodeRepo(Path path, String baseUrl, String project, String baseRevision) throws IOException, UnirestException {
        this.repositoryPath = path;
        SeRepoUrlObject seRepoUrlObject = new SeRepoUrlObject(baseUrl, project, baseRevision);
        this.connector = APIConnector.withSeRepoUrl(seRepoUrlObject);
        this.yStatements.addAll(loadBaseRevision());
        this.diffObject = new DiffObject(this.yStatements, loadLocalDecisions(), loadRemoteDecisions());
    }

    private List<YStatementJustificationWrapper> loadBaseRevision() throws UnirestException {
        return connector.getYStatementJustifications();
    }

    private List<YStatementJustificationWrapper> loadRemoteDecisions() throws UnirestException {
        return connector.getLatestYStatementJustifications();
    }

    private List<YStatementJustificationWrapper> loadLocalDecisions() throws IOException {
        List<YStatementJustificationWrapper> localYStatements = new ArrayList<>();
        classPaths.clear();
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
                        classPaths.put(yStatementJustification.id(), classPath);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("Could not instantiate class.", e);
                }
            }
        });
        return localYStatements;
    }

    private void writeEadsToDisk() throws IOException {
        for (YStatementJustificationWrapper yStatementJustificationWrapper : yStatements) {
            writeEadToClass(yStatementJustificationWrapper.getId());
        }
    }

    private void writeEadToClass(String id) throws IOException {
        List<YStatementJustificationWrapper> decisions = yStatements.stream().filter(y -> id.equals(y
                .getId())).collect(Collectors.toList());
        if (decisions.isEmpty()) {
            return;
        }
        String classPath = classPaths.get(id);
        JavaDecisionParser.writeModifiedYStatementToFile(decisions.get(0), convertToRealPath(classPath));
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

    private Path convertToRealPath(String classPath) {
        classPath = classPath.replaceAll("\\.", "/");
        classPath += ".java";
        return repositoryPath.resolve(classPath);
    }

    @Override
    public String commit(String message) throws Exception {
        return "N/A";
    }

    @Override
    public void pull() throws Exception {
        writeEadsToDisk();
    }

    @Override
    public void merge(String commitId) throws Exception {

    }

    @Override
    public void reset(String commitId) throws Exception {

    }

    @Override
    public void reloadEADs() throws IOException {
        loadLocalDecisions();
    }
}
