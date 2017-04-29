package com.eadlsync.sync;

/**
 * Info class which holds information about version, license, usage and more
 * <p>
 * Created by Tobias on 28.04.2017.
 */
public class EADLSyncInfo {

    private final static EADLSyncInfo instance = new EADLSyncInfo();

    private EADLSyncInfo() {

    }

    public static EADLSyncInfo getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
