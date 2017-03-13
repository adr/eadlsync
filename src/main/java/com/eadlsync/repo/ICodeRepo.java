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
     * Synchronizes the eads of a code repository with the given seitems and returns a
     * list of {@link SeItem}s which are in the code repository but not in the parameter list.
     *
     * @param items the seitems to be synchronised with
     * @return a list of {@link SeItem}
     */
    List<SeItem> getObsoleteEADs(List<SeItem> items);

    /**
     * Synchronizes the eads of a code repository with the seitems of a serepo and returns a
     * list of {@link SeItem}s which are in the serepo but not in the code repository.
     *
     * @param items the seitems to be synchronised with*
     * @return a list of {@link SeItem}
     */
    List<SeItem> getAdditionalEADs(List<SeItem> items);

    /**
     * This will generate an eadlsync report.
     *
     * @return a {@link EADLSyncReport}
     */
    EADLSyncReport getEADSyncReport();

}
