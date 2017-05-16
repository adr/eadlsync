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
    public ListProperty<YStatementJustificationWrapper> additionalYStatementsProperty();

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustificationWrapper} which are in the code repository but not in the
     * serepo.
     *
     * @return a list of {@link YStatementJustificationWrapper}
     */
    public ListProperty<YStatementJustificationWrapper> obsoleteYStatementsProperty();

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustificationComparisionObject} which holds two
     * {@link YStatementJustificationWrapper} Objects which are present in both repos but have
     * different fields.
     *
     * @return a list of {@link YStatementJustificationComparisionObject}
     */
    public ListProperty<YStatementJustificationComparisionObject> differentYStatementsProperty();

    /**
     * Updates the decision with the given id in the code repo.
     * The decision with the given id has to be in the list of differentYStatementsProperty
     *
     * @param id as String
     */
    public void updateYStatementInCodeRepo(String id);

    /**
     * Updates the decision with the given id in the se-repo.
     * The decision with the given id has to be in the list of differentYStatementsProperty
     *
     * @param id as String
     */
    public void updateYStatementInSeRepo(String id);


    /**
     * This will write the ystatements to the files in the code repository and creates a commit for
     * the changed ystatements of the se-repo.
     *
     * @param message for the commit
     */
    public void commit(String message) throws Exception;

    /**
     * This will generate an eadlsync report.
     *
     * @return a {@link EADLSyncReport}
     */
    public EADLSyncReport getEadlSyncReport();


    /**
     * Reinitialize the code repository and the se-repo. This essentially reloads the eadl for the
     * code repo and the se-items for the se-repo.
     *
     * @throws Exception
     */
    void reinitialize() throws Exception;
}
