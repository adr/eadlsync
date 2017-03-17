package com.eadlsync.repo;

import com.eadlsync.core.EADLSyncReport;
import com.eadlsync.eadl.annotations.DecisionMade;
import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tobias on 07/03/2017.
 */
public class OfflineCodeRepo implements ICodeRepo {

    private final Log LOG = LogFactory.getLog(ICodeRepo.class);
    private final Path repositoryPath;
    private final ListProperty<DecisionMade> decisions = new SimpleListProperty<>();
    private final ListProperty<YStatementJustification> yStatements = new SimpleListProperty<>();
    private final Iterable<SeItem> items;

    public OfflineCodeRepo(String path, List<SeItem> items) {
        this.repositoryPath = Paths.get(path);
        this.items = items;
        init();
    }


    @Override
    public List<SeItem> getObsoleteEADs(List<SeItem> items) {
        List<SeItem> obsoleteItems = new ArrayList<>();
        for (DecisionMade decisionMade : decisions) {
            boolean isNotAvailable = items.stream().filter(seItem -> seItem.getId().equals(decisionMade.id())).collect(Collectors.toList()).isEmpty();
            if (isNotAvailable) {
                SeItem obsoleteItem = new SeItem();
                obsoleteItem.setId(URI.create(decisionMade.id()));
                obsoleteItems.add(obsoleteItem);
            }
        }
        return obsoleteItems;
    }

    @Override
    public List<SeItem> getAdditionalEADs(List<SeItem> items) {
        return null;
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
                        LOG.info("Loading class: " + classPath);
                        Class clazz = finalUrlClassLoader.loadClass(classPath);
                        for (Annotation annotation : clazz.getAnnotationsByType(DecisionMade.class)) {
                            decisions.add((DecisionMade) annotation);
                        }
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
