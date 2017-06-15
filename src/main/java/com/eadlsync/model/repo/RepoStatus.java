package com.eadlsync.model.repo;

import com.eadlsync.model.diff.Decisions;

/**
 * Created by tobias on 08/06/2017.
 */
public class RepoStatus {

    public RepoStatus(Decisions descisions) {
        // TODO:
    }

    public static RepoStatus of(Decisions descisions) {
        return new RepoStatus(descisions);
    }

}
