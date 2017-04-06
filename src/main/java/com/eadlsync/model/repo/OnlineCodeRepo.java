package com.eadlsync.model.repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.report.EADLSyncReport;

/**
 * Created by tobias on 07/03/2017.
 */
public class OnlineCodeRepo implements ICodeRepo {

    private final URL repositoryURL;

    public OnlineCodeRepo(String path) throws MalformedURLException {
        this.repositoryURL = new URL(path);
    }

    @Override
    public List<YStatementJustification> findObsoleteEADs() {
        return null;
    }

    @Override
    public List<YStatementJustification> findAdditionalEADs() {
        return null;
    }

    @Override
    public List<YStatementJustification> findDifferentEADs() {
        return null;
    }

    @Override
    public EADLSyncReport getEADSyncReport() {
        return null;
    }
}
