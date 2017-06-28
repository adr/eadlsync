package com.eadlsync.model.decision;

import java.util.HashMap;

/**
 * Created by tobias on 28/06/2017.
 */
public abstract class DecisionSourceMapping {

    private static HashMap<String, String> localSource = new HashMap<>();
    private static HashMap<String, String> remoteSource = new HashMap<>();

    public static void putLocalSource(String id, String source) {
        localSource.put(id, source);
    }

    public static void putRemoteSource(String id, String source) {
        remoteSource.put(id, source);
    }

    public static String getLocalSource(String id) {
        return localSource.get(id);
    }

    public static String getRemoteSource(String id) {
        return remoteSource.get(id);
    }


}
