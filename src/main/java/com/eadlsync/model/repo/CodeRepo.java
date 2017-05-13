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
import java.util.Map;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
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
                        yStatements.add(new YStatementJustificationWrapper(yStatementJustification));
                        classPaths.put(yStatementJustification.id(), classPath);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("Could not instantiate class.", e);
                }
            }
        });
    }

    private void writeEadsToDisk() throws MalformedURLException, ClassNotFoundException {
        for (YStatementJustificationWrapper yStatementJustificationWrapper : yStatements) {
            writeEadToClass(yStatementJustificationWrapper.getId());
        }
    }

    private void writeEadToClass(String id) throws MalformedURLException, ClassNotFoundException {
        URLClassLoader finalUrlClassLoader = getUrlClassLoader();
        String classPath = classPaths.get(id);
        Class clazz = finalUrlClassLoader.loadClass(classPath);
        // TODO: write annotation to class
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
        return repositoryPath.relativize(path).toString().replace(".java", "").
                // replace file separator on unix systems
                        replaceAll("/", ".").
                // replace file separator on windows systems
                        replaceAll("\\\\", ".");
    }

    /**
     * For a offline code repo this will write the changed decisions to the disk.
     * It can be called right after any field of an embedded architectural decision is updated.
     *
     * @throws Exception
     */
    @Override
    public void commit() throws Exception {
        // TODO: only write changed eads and not all
        writeEadsToDisk();
    }

    @Override
    public void reloadEADs() throws IOException {
        loadEadsFromDisk();
    }
}
