package io.github.adr.eadlsync.model.repo;

import java.util.ArrayList;
import java.util.List;

import io.github.adr.eadlsync.model.diff.DiffManager;
import io.github.adr.eadlsync.util.ystatement.YStatementJustificationComparator;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;

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
            if (YStatementJustificationComparator.isEqual(yStatementDiff.getChangedDecision(),
                    YStatementJustificationWrapper.deleted(id))) {
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
