package com.eadlsync.repo;

import com.eadlsync.core.EADLSyncReport;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by tobias on 07/03/2017.
 */
public class OnlineCodeRepo implements ICodeRepo {

    private final URL repositoryURL;

    public OnlineCodeRepo(String path) throws MalformedURLException {
        this.repositoryURL = new URL(path);
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
