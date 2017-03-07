package com.eadlsync.repo;

import com.eadlsync.core.EADLSyncReport;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;

import java.util.List;

/**
 * Created by tobias on 07/03/2017.
 * <p>
 * This interface specifies the methods a code repository has to implement in regards
 * to the synchronization of embedded architectural decisions.
 */
public interface ICodeRepo {

    /**
     * Synchronizes the eads of a code repository with the seitems of a serepo and returns a
     * list of {@link SeItem}s which are in the code repository but not in the serepo.
     *
     * @return a list of {@link SeItem}
     */
    List<SeItem> getObsoleteEADs();

    /**
     * Synchronizes the eads of a code repository with the seitems of a serepo and returns a
     * list of {@link SeItem}s which are in the serepo but not in the code repository.
     *
     * @return a list of {@link SeItem}
     */
    List<SeItem> getAdditionalEADs();

    /**
     * This will generate an eadlsync report.
     *
     * @return a {@link EADLSyncReport}
     */
    EADLSyncReport getEADSyncReport();

}
