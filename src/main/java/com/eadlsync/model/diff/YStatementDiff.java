package com.eadlsync.model.diff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.util.YStatementField;
import com.eadlsync.util.YStatementJustificationComparator;

import static com.eadlsync.util.YStatementField.CONTEXT;
import static com.eadlsync.util.YStatementField.FACING;
import static com.eadlsync.util.YStatementField.CHOSEN;
import static com.eadlsync.util.YStatementField.NEGLECTED;
import static com.eadlsync.util.YStatementField.ACHIEVING;
import static com.eadlsync.util.YStatementField.ACCEPTING;
import static com.eadlsync.util.YStatementField.MORE_INFORMATION;

/**
 *
 */
public class YStatementDiff {

    private final String id;
    private YStatementJustificationWrapper changedDecision = null;
    private Map<YStatementField, String> diff = new HashMap<>();

    public static YStatementDiff of(YStatementJustificationWrapper baseDecision, YStatementJustificationWrapper diffDecision) {
        return new YStatementDiff(baseDecision, diffDecision);
    }

    private YStatementDiff (YStatementJustificationWrapper baseDecision, YStatementJustificationWrapper diffDecision) {
        if (baseDecision == null) {
            this.id = diffDecision.getId();
            this.changedDecision = diffDecision;
            diff.put(CONTEXT, diffDecision.getContext());
            diff.put(FACING, diffDecision.getFacing());
            diff.put(CHOSEN, diffDecision.getChosen());
            diff.put(NEGLECTED, diffDecision.getNeglected());
            diff.put(ACHIEVING, diffDecision.getAchieving());
            diff.put(ACCEPTING, diffDecision.getAccepting());
            diff.put(MORE_INFORMATION, diffDecision.getMoreInformation());
        } else if (diffDecision == null) {
            this.id = baseDecision.getId();
        } else {
            this.id = baseDecision.getId();
            this.changedDecision = diffDecision;
            if (!YStatementJustificationComparator.isContextEqual(baseDecision, diffDecision)) {
                diff.put(CONTEXT, diffDecision.getContext());
            }
            if (!YStatementJustificationComparator.isFacingEqual(baseDecision, diffDecision)) {
                diff.put(FACING, diffDecision.getFacing());
            }
            if (!YStatementJustificationComparator.isChosenEqual(baseDecision, diffDecision)) {
                diff.put(CHOSEN, diffDecision.getChosen());
            }
            if (!YStatementJustificationComparator.isNeglectedEqual(baseDecision, diffDecision)) {
                diff.put(NEGLECTED, diffDecision.getNeglected());
            }
            if (!YStatementJustificationComparator.isAchievingEqual(baseDecision, diffDecision)) {
                diff.put(ACHIEVING, diffDecision.getAchieving());
            }
            if (!YStatementJustificationComparator.isAcceptingEqual(baseDecision, diffDecision)) {
                diff.put(ACCEPTING, diffDecision.getAccepting());
            }
            if (!YStatementJustificationComparator.isMoreInformationEqual(baseDecision, diffDecision)) {
                diff.put(MORE_INFORMATION, diffDecision.getMoreInformation());
            }
        }
    }

    public boolean conflictsWith(YStatementDiff yStatementDiff) {
        if (!this.id.equals(yStatementDiff.id)) {
            return false;
        }

        //local changes but remote deleted
        if (yStatementDiff.changedDecision == null && !diff.isEmpty()) {
            return true;
        }

        //local deleted but remote changes
        if (!yStatementDiff.diff.isEmpty() && changedDecision == null) {
            return true;
        }

        // different local and remote change
        for (Map.Entry<YStatementField, String> entry : diff.entrySet()) {
            String stringDiff = yStatementDiff.diff.get(entry.getKey());
            if (stringDiff != null) {
                if (!stringDiff.equals(entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean conflictsWith(List<YStatementDiff> yStatementDiffs) {
        List<YStatementDiff> sameIds = yStatementDiffs.stream().filter(diff -> this.id.equals(diff.id)).collect(Collectors.toList());
        if (!sameIds.isEmpty()) {
            return conflictsWith(sameIds.get(0));
        }
        return false;
    }

    public List<YStatementJustificationWrapper> applyDiff(List<YStatementJustificationWrapper> baseDecisions) {
        List<YStatementJustificationWrapper> decision = baseDecisions.stream().filter(y -> y.getId().equals(id)).collect(Collectors.toList());
        YStatementJustificationWrapper baseDecision;
        if (decision.isEmpty()) {
            baseDecision = new YStatementJustificationWrapperBuilder(id, changedDecision.getSource()).build();
            baseDecisions.add(baseDecision);
        } else {
            baseDecision = decision.get(0);
        }
        if (changedDecision == null) {
            baseDecisions.remove(baseDecision);
            return baseDecisions;
        }
        for (Map.Entry<YStatementField, String> entry : diff.entrySet()) {
            if (entry.getKey() == CONTEXT) {
                baseDecision.setContext(entry.getValue());
            } else if (entry.getKey() == FACING) {
                baseDecision.setFacing(entry.getValue());
            } else if (entry.getKey() == CHOSEN) {
                baseDecision.setChosen(entry.getValue());
            } else if (entry.getKey() == NEGLECTED) {
                baseDecision.setNeglected(entry.getValue());
            } else if (entry.getKey() == ACHIEVING) {
                baseDecision.setAchieving(entry.getValue());
            } else if (entry.getKey() == ACCEPTING) {
                baseDecision.setAccepting(entry.getValue());
            } else if (entry.getKey() == MORE_INFORMATION) {
                baseDecision.setMoreInformation(entry.getValue());
            }
        }
        return baseDecisions;
    }

    public String getId() {
        return id;
    }
}
