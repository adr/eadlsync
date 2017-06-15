package com.eadlsync.model.repo;

import com.eadlsync.EADLSyncExecption;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by tobias on 07/03/2017.
 * <p>
 * This interface specifies the methods a repository with architectural decisions has to implement to
 * synchronize and update its embedded architectural decisions.
 */
public interface IRepo {

    /**
     * Commits and pushes the local changes to the se-repo
     * Use the isForcing option to commit local changes even if there are changes in the se-repo.
     *
     * @param message
     * @param isForcing
     * @return the commit id
     * @throws Exception
     */
    String commit(String message, boolean isForcing) throws Exception;

    /**
     * Pulls the changes of the latest commit of the se-repo and applies it to the local decisions.
     *
     * @throws Exception
     */
    void pull() throws Exception;

    /**
     * Merges the local decisions with the decisions of the se-repo with the given commit id.
     *
     * @throws Exception
     */
    void merge(String commitId) throws Exception;

    /**
     * Resets the local decisions to the decisions of the se-repo with the given commit id.
     *
     * @param commitId
     * @throws Exception
     */
    void reset(String commitId) throws Exception;

}
