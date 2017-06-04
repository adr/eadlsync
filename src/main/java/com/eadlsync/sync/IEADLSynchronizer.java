package com.eadlsync.sync;

import javafx.beans.property.ListProperty;

import com.eadlsync.model.decision.YStatementJustificationComparisionObject;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.report.EADLSyncReport;

/**
 * Created by Tobias on 28.04.2017.
 */
public interface IEADLSynchronizer {

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustificationWrapper} which are in the se-repo but not in the code
     * repository.
     *
     * @return a list of {@link YStatementJustificationWrapper}
     */
    ListProperty<YStatementJustificationWrapper> additionalYStatementsProperty();

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustificationWrapper} which are in the code repository but not in the
     * serepo.
     *
     * @return a list of {@link YStatementJustificationWrapper}
     */
    ListProperty<YStatementJustificationWrapper> obsoleteYStatementsProperty();

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustificationComparisionObject} which holds two
     * {@link YStatementJustificationWrapper} Objects which are present in both repos but have
     * different fields.
     *
     * @return a list of {@link YStatementJustificationComparisionObject}
     */
    ListProperty<YStatementJustificationComparisionObject> differentYStatementsProperty();

    /**
     * Updates the decision with the given id in the code repo.
     * The decision with the given id has to be in the list of differentYStatementsProperty
     *
     * @param id as String
     */
    void updateYStatementInCodeRepo(String id);

    /**
     * Updates the decision with the given id in the se-repo.
     * The decision with the given id has to be in the list of differentYStatementsProperty
     *
     * @param id as String
     */
    void updateYStatementInSeRepo(String id);


    /**
     * This will write the ystatements of the code repository to filesystem.
     *
     * @param message for the commit
     * @return the commit id
     */
    String commitToBaseRepo(String message) throws Exception;

    /**
     * This will commit the ystatements of the se-repo. It also changes the working commit and
     * reinitialize the ystatements of the se-repo.
     *
     * @param message for the commit
     * @return the commit id
     */
    String commitToRemoteRepo(String message) throws Exception;

    /**
     * This will generate an eadlsync report.
     *
     * @return a {@link EADLSyncReport}
     */
    EADLSyncReport getEadlSyncReport();

    /**
     * Reinitialize the code repository. Discards any unwritten changes to the code repo and reloads
     * eadls from the file system.
     *
     * @throws Exception
     */
    void reinitializeCodeRepo() throws Exception;

    /**
     * Reinitialize the se-repo. Discards any unwritten changes to the se-repo and reloads eadls from
     * the server.
     *
     * @throws Exception
     */
    void reinitializeSeRepo() throws Exception;
}
