package com.eadlsync.repo;

import com.eadlsync.core.EADLSyncReport;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by tobias on 07/03/2017.
 */
public class OfflineCodeRepo implements ICodeRepo {

    private final Path repositoryPath;

    public OfflineCodeRepo(String path) {
        this.repositoryPath = Paths.get(path);
    }

    @Override
    public List<SeItem> getObsoleteEADs() {
        return null;
    }

    @Override
    public List<SeItem> getAdditionalEADs() {
        return null;
    }

    @Override
    public EADLSyncReport getEADSyncReport() {
        return null;
    }
}
