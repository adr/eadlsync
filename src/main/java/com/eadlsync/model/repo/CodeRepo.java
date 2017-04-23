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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.report.EADLSyncReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tobias on 07/03/2017.
 */
public class CodeRepo implements ICodeRepo {

    private final static CodeRepo instance = new CodeRepo();
    private final Logger LOG = LoggerFactory.getLogger(CodeRepo.class);
    private Path repositoryPath;
    private ListProperty<YStatementJustification> yStatements = new SimpleListProperty<>();
    private ListProperty<YStatementJustification> seRepoYStatements = new SimpleListProperty<>();

    public static CodeRepo getInstance() {
        return instance;
    }

    public void initializeFromPath(String path) {
        getInstance().repositoryPath = Paths.get(path);
        //        getInstance().seRepoYStatements.addAll(seRepoYStatements);
        getInstance().init();
    }

    public void initializeFromUrl(String url) throws MalformedURLException {
        // TODO: clone online code repo and invoke fromPath method
    }


    @Override
    public List<YStatementJustification> findObsoleteEADs() {
        // TODO: create listeners on the lists and update list on change events
        List<YStatementJustification> obsoleteItems = new ArrayList<>();
        for (YStatementJustification yStatementJustification : yStatements) {
            boolean isNotAvailable = seRepoYStatements.stream().filter(y -> y.id().equals(yStatementJustification.id
                    ())).collect(Collectors.toList()).isEmpty();
            if (isNotAvailable) {
                obsoleteItems.add(yStatementJustification);
            }
        }
        return obsoleteItems;
    }

    @Override
    public List<YStatementJustification> findAdditionalEADs() {
        // TODO: create listeners on the lists and update list on change events
        List<YStatementJustification> additionalItems = new ArrayList<>();
        for (YStatementJustification yStatementJustification : seRepoYStatements) {
            boolean isNotAvailable = yStatements.stream().filter(y -> y.id().equals(yStatementJustification.id()))
                    .collect(Collectors.toList()).isEmpty();
            if (isNotAvailable) {
                additionalItems.add(yStatementJustification);
            }
        }
        return additionalItems;
    }

    @Override
    public List<YStatementJustification> findDifferentEADs() {
        // TODO: create listeners on the lists and update list on change events
        List<YStatementJustification> differentItems = new ArrayList<>();
        // TODO:
        return differentItems;
    }

    @Override
    public EADLSyncReport getEADSyncReport() {
        return null;
    }

    private void init() {
        URLClassLoader urlClassLoader;
        try {
            urlClassLoader = new URLClassLoader(new URL[]{repositoryPath.toUri().toURL()});
            URLClassLoader finalUrlClassLoader = urlClassLoader;
            Files.walk(repositoryPath, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
                if (isValid(path)) {
                    String classPath = convertToClassPath(path);
                    try {
                        LOG.info("Loading class {} ", classPath);
                        Class clazz = finalUrlClassLoader.loadClass(classPath);
                        for (Annotation annotation : clazz.getAnnotationsByType(YStatementJustification.class)) {
                            yStatements.add((YStatementJustification) annotation);
                        }
                    } catch (ClassNotFoundException e) {
                        LOG.error("Class could not be instantiated", e);
                    }
                }
            });
        } catch (IOException e) {
            LOG.error("Could not read file", e);
        }
    }

    private boolean isValid(Path path) {
        return path.toString().endsWith(".java") && !Files.isDirectory(path);
    }

    private String convertToClassPath(Path path) {
        return repositoryPath.relativize(path).toString().replace(".java", "").replaceAll("/", ".");
    }

    public void setSeRepoYStatements(List<YStatementJustification> seRepoYStatements) {
        this.seRepoYStatements.clear();
        this.seRepoYStatements.addAll(seRepoYStatements);
    }

}
