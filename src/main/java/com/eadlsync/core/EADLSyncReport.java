package com.eadlsync.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tobias on 07/03/2017.
 */
public class EADLSyncReport {

    // folder as key, decisions count as int
    private final Map<String, Integer> folderDecisions = new HashMap<>();

    // folder as key, decisions count as int
    private final Map<String, Integer> obsoleteDecisions = new HashMap<>();

    // folder as key, decisions count as int
    private final Map<String, Integer> additionalDecisions = new HashMap<>();

    @Override
    public String toString() {
        return "";
    }

}
