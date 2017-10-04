package io.github.adr.eadlsync.model.diff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import io.github.adr.eadlsync.util.ystatement.YStatementField;
import io.github.adr.eadlsync.util.ystatement.YStatementJustificationComparator;

/**
 * Holds a diff for a {@link YStatementJustificationWrapper} object based on their fields.
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
            /** so far no additional decisions are supported **/
            this.id = diffDecision.getId();
            this.changedDecision = diffDecision;
            diff.put(YStatementField.CONTEXT, diffDecision.getContext());
            diff.put(YStatementField.FACING, diffDecision.getFacing());
            diff.put(YStatementField.CHOSEN, diffDecision.getChosen());
            diff.put(YStatementField.NEGLECTED, diffDecision.getNeglected());
            diff.put(YStatementField.ACHIEVING, diffDecision.getAchieving());
            diff.put(YStatementField.ACCEPTING, diffDecision.getAccepting());
            diff.put(YStatementField.MORE_INFORMATION, diffDecision.getMoreInformation());
        } else if (diffDecision == null) {
            this.id = baseDecision.getId();
            this.changedDecision = YStatementJustificationWrapper.deleted(id);
            diff.put(YStatementField.CONTEXT, changedDecision.getContext());
            diff.put(YStatementField.FACING, changedDecision.getFacing());
            diff.put(YStatementField.CHOSEN, changedDecision.getChosen());
            diff.put(YStatementField.NEGLECTED, changedDecision.getNeglected());
            diff.put(YStatementField.ACHIEVING, changedDecision.getAchieving());
            diff.put(YStatementField.ACCEPTING, changedDecision.getAccepting());
            diff.put(YStatementField.MORE_INFORMATION, changedDecision.getMoreInformation());
        } else {
            this.id = baseDecision.getId();
            this.changedDecision = diffDecision;
            if (!YStatementJustificationComparator.isContextEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.CONTEXT, diffDecision.getContext());
            }
            if (!YStatementJustificationComparator.isFacingEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.FACING, diffDecision.getFacing());
            }
            if (!YStatementJustificationComparator.isChosenEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.CHOSEN, diffDecision.getChosen());
            }
            if (!YStatementJustificationComparator.isNeglectedEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.NEGLECTED, diffDecision.getNeglected());
            }
            if (!YStatementJustificationComparator.isAchievingEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.ACHIEVING, diffDecision.getAchieving());
            }
            if (!YStatementJustificationComparator.isAcceptingEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.ACCEPTING, diffDecision.getAccepting());
            }
            if (!YStatementJustificationComparator.isMoreInformationEqual(baseDecision, diffDecision)) {
                diff.put(YStatementField.MORE_INFORMATION, diffDecision.getMoreInformation());
            }
        }
    }

    public boolean conflictsWith(YStatementDiff yStatementDiff) {
        if (!this.id.equals(yStatementDiff.id)) {
            return false;
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
            /** so far no additional decisions are supported **/
//            baseDecision = new YStatementJustificationWrapperBuilder(id, changedDecision.getSource()).build();
//            baseDecisions.add(baseDecision);
            return baseDecisions;
        } else {
            baseDecision = decision.get(0);
        }
        if (YStatementJustificationComparator.isEqual(changedDecision, YStatementJustificationWrapper.deleted(id))) {
            baseDecisions.remove(baseDecision);
            return baseDecisions;
        }
        for (Map.Entry<YStatementField, String> entry : diff.entrySet()) {
            if (entry.getKey() == YStatementField.CONTEXT) {
                baseDecision.setContext(entry.getValue());
            } else if (entry.getKey() == YStatementField.FACING) {
                baseDecision.setFacing(entry.getValue());
            } else if (entry.getKey() == YStatementField.CHOSEN) {
                baseDecision.setChosen(entry.getValue());
            } else if (entry.getKey() == YStatementField.NEGLECTED) {
                baseDecision.setNeglected(entry.getValue());
            } else if (entry.getKey() == YStatementField.ACHIEVING) {
                baseDecision.setAchieving(entry.getValue());
            } else if (entry.getKey() == YStatementField.ACCEPTING) {
                baseDecision.setAccepting(entry.getValue());
            } else if (entry.getKey() == YStatementField.MORE_INFORMATION) {
                baseDecision.setMoreInformation(entry.getValue());
            }
        }
        return baseDecisions;
    }

    public String getId() {
        return id;
    }

    public YStatementJustificationWrapper getChangedDecision() {
        return changedDecision;
    }

    public Map<YStatementField, String> getDiff() {
        return diff;
    }
}
