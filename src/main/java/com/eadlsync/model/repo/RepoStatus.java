package com.eadlsync.model.repo;

import com.eadlsync.model.diff.DiffManager;

/**
 * Created by tobias on 08/06/2017.
 */
public class RepoStatus {

    public RepoStatus(DiffManager descisions) {
        // TODO:
    }

    public static RepoStatus of(DiffManager descisions) {
        return new RepoStatus(descisions);
    }

}
