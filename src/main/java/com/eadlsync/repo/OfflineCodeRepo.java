package com.eadlsync.repo;

import com.eadlsync.core.EADLSyncReport;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * Created by tobias on 07/03/2017.
 */
public class OfflineCodeRepo implements ICodeRepo {

    private final Log LOG = LogFactory.getLog(ICodeRepo.class);
    private final Path repositoryPath;

    public OfflineCodeRepo(String path) {
        this.repositoryPath = Paths.get(path);
    }


    @Override
    public List<SeItem> getObsoleteEADs(List<SeItem> items) {
        List<SeItem> obsoleteItems = new ArrayList<>();
        traverse(items);
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

    private Path traverse(List<SeItem> items) {
        URLClassLoader urlClassLoader = null;
        try {
            urlClassLoader = new URLClassLoader(new URL[]{repositoryPath.toUri().toURL()});
            URLClassLoader finalUrlClassLoader = urlClassLoader;
            Files.walk(repositoryPath, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
                if (path.toString().endsWith(".java") && !Files.isDirectory(path)) {
                    String classPath = getClassPath(path);
                    try {
                        LOG.info("Loading class: " + classPath);
                        Class clazz = finalUrlClassLoader.loadClass(classPath);
                        Annotation[] annotations = clazz.getAnnotations();
                        for (Annotation annotation : annotations) {

                        }
                    } catch (ClassNotFoundException e) {
                        LOG.error("Class could not be instantiated", e);
                    }
                }
            });
        } catch (IOException e) {
            LOG.error("Could not read file", e);
        }
        return null;
    }

    private String getClassPath(Path path) {
        return repositoryPath.relativize(path).toString().replace(".java", "").replaceAll("/", ".");
    }

}
