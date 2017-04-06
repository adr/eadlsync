package com.eadlsync.model.repo;

import java.util.List;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.report.EADLSyncReport;

/**
 * Created by tobias on 07/03/2017.
 * <p>
 * This interface specifies the methods a code repository has to implement in regards
 * to the synchronization of embedded architectural decisions.
 */
public interface ICodeRepo {


    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustification}s which are in the code repository but not in the serepo.
     *
     * @return a list of {@link YStatementJustification}
     */
    List<YStatementJustification> findObsoleteEADs();

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustification}s which are in the se-repo but not in the code repository.
     *
     * @return a list of {@link YStatementJustification}
     */
    List<YStatementJustification> findAdditionalEADs();

    /**
     * Synchronizes the eads of a code repository with ystatements of a se-repo and returns a
     * list of {@link YStatementJustification}s which are in the code repository and in the se-repo but have
     * different values in one or more fields.
     *
     * @return a list of {@link YStatementJustification}
     */
    List<YStatementJustification> findDifferentEADs();

    /**
     * This will generate an eadlsync report.
     *
     * @return a {@link EADLSyncReport}
     */
    EADLSyncReport getEADSyncReport();

}
