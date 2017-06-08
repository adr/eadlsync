package com.eadlsync.model.repo;

import com.eadlsync.EADLSyncExecption;

/**
 * Created by tobias on 07/03/2017.
 * <p>
 * This interface specifies the methods a repository with architectural decisions has to implement to
 * synchronize and update its embedded architectural decisions.
 */
public interface IRepo {

    /**
     * Commits and pushes the local changes to the se-repo
     *
     * @param message for the commitToBaseRepo
     * @return the commit id
     * @throws EADLSyncExecption
     */
    String commit(String message) throws EADLSyncExecption;

    /**
     * Pulls the changes of the latest commit of the se-repo and applies it to the local decisions.
     *
     * @throws EADLSyncExecption
     */
    void pull() throws EADLSyncExecption;

    /**
     * Resets the decisions to the decisions of the se-repo with the given commit id.
     *
     * @param commitId
     * @throws EADLSyncExecption
     */
    void reset(String commitId) throws EADLSyncExecption;

    /**
     * Reloads the embedded ads for this repository.
     *
     * @throws EADLSyncExecption
     */
    void reloadEADs() throws EADLSyncExecption;

}
