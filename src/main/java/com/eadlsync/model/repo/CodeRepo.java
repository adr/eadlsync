package com.eadlsync.model.repo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.util.OS;
import com.eadlsync.util.io.JavaDecisionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tobias on 07/03/2017.
 */
public class CodeRepo extends ARepo {

    private final Logger LOG = LoggerFactory.getLogger(CodeRepo.class);
    private final Path repositoryPath;
    private Map<String, String> classPaths = new HashMap<>();

    public CodeRepo(String path) throws IOException {
        this.repositoryPath = Paths.get(path);
        loadEadsFromDisk();
    }

    private void loadEadsFromDisk() throws IOException {
        yStatements.clear();
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
                        yStatements.add(new YStatementJustificationWrapperBuilder(yStatementJustification).build());
                        classPaths.put(yStatementJustification.id(), classPath);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("Could not instantiate class.", e);
                }
            }
        });
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
        classPath = classPath.replaceAll(".", OS.FS);
        classPath += ".java";
        return repositoryPath.resolve(classPath);

    }

    /**
     * For a offline code repo this will write the changed decisions to the disk.
     * It can be called right after any field of an embedded architectural decision is updated.
     * The commit message is ignored for offline repositories.
     *
     * @param message for the commit
     * @throws Exception
     */
    @Override
    public void commit(String message) throws Exception {
        // TODO: only write changed eads and not all
        writeEadsToDisk();
    }

    @Override
    public void reloadEADs() throws IOException {
        loadEadsFromDisk();
    }
}
