package com.eadlsync.model.repo;

import java.util.ArrayList;
import java.util.List;

import com.eadlsync.model.diff.DiffManager;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;

import static com.eadlsync.model.decision.YStatementJustificationWrapper.deleted;

/**
 *
 */
public class RepoStatus {

    private boolean localChanged = false;
    private boolean remoteChanged = false;
    private boolean autoMerge = false;
    private List<String> deletedDecisionsIds = new ArrayList<>();
    private List<String> changedDecisionsIds = new ArrayList<>();

    public RepoStatus(DiffManager decisions) {
        localChanged = !decisions.getLocalDiff().isEmpty();
        remoteChanged = !decisions.getRemoteDiff().isEmpty();
        autoMerge = decisions.canAutoMerge();
        decisions.getLocalDiff().forEach(yStatementDiff -> {
            String id = yStatementDiff.getId();
            if (YStatementJustificationComparator.isEqual(yStatementDiff.getChangedDecision(), deleted(id))) {
                deletedDecisionsIds.add(id);
            } else {
                changedDecisionsIds.add(id);
            }
        });
    }

    public boolean isLocalChanged() {
        return localChanged;
    }

    public boolean isRemoteChanged() {
        return remoteChanged;
    }

    public boolean isAutoMerge() {
        return autoMerge;
    }

    public List<String> getDeletedDecisionsIds() {
        return deletedDecisionsIds;
    }

    public List<String> getChangedDecisionsIds() {
        return changedDecisionsIds;
    }

}
