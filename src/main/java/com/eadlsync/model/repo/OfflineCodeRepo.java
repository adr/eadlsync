package com.eadlsync.model.repo;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
public class OfflineCodeRepo implements ICodeRepo {

    private final Logger LOG = LoggerFactory.getLogger(OfflineCodeRepo.class);
    private final Path repositoryPath;
    private ListProperty<YStatementJustification> yStatements = new SimpleListProperty<>();
    private List<YStatementJustification> seRepoYStatements = new ArrayList<>();

    public OfflineCodeRepo(String path, List<YStatementJustification> seRepoYStatements) {
        this.repositoryPath = Paths.get(path);
        this.seRepoYStatements = seRepoYStatements;
        init();
    }


    @Override
    public List<YStatementJustification> findObsoleteEADs() {
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

}
