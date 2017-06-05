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
    private YStatementJustificationWrapper baseDecision;
    private YStatementJustificationWrapper changedDecision;
    private Map<YStatementField, StringDiff> diff = new HashMap<>();

    public static YStatementDiff of(YStatementJustificationWrapper baseDecision, YStatementJustificationWrapper diffDecision) {
        return new YStatementDiff(baseDecision, diffDecision);
    }

    private YStatementDiff (YStatementJustificationWrapper baseDecision, YStatementJustificationWrapper diffDecision) {
        this.id = baseDecision.getId();
        this.baseDecision = baseDecision;
        this.changedDecision = diffDecision;
        if (!YStatementJustificationComparator.isContextEqual(baseDecision, diffDecision)) {
            diff.put(CONTEXT, StringDiff.of(baseDecision.getContext(), diffDecision.getContext()));
        }
        if (!YStatementJustificationComparator.isFacingEqual(baseDecision, diffDecision)) {
            diff.put(FACING, StringDiff.of(baseDecision.getFacing(), diffDecision.getFacing()));
        }
        if (!YStatementJustificationComparator.isChosenEqual(baseDecision, diffDecision)) {
            diff.put(CHOSEN, StringDiff.of(baseDecision.getChosen(), diffDecision.getChosen()));
        }
        if (!YStatementJustificationComparator.isNeglectedEqual(baseDecision, diffDecision)) {
            diff.put(NEGLECTED, StringDiff.of(baseDecision.getNeglected(), diffDecision.getNeglected()));
        }
        if (!YStatementJustificationComparator.isAchievingEqual(baseDecision, diffDecision)) {
            diff.put(ACHIEVING, StringDiff.of(baseDecision.getAchieving(), diffDecision.getAchieving()));
        }
        if (!YStatementJustificationComparator.isAcceptingEqual(baseDecision, diffDecision)) {
            diff.put(ACCEPTING, StringDiff.of(baseDecision.getAccepting(), diffDecision.getAccepting()));
        }
        if (!YStatementJustificationComparator.isMoreInformationEqual(baseDecision, diffDecision)) {
            diff.put(MORE_INFORMATION, StringDiff.of(baseDecision.getMoreInformation(), diffDecision.getMoreInformation()));
        }
    }

    public boolean conflictsWith(YStatementDiff yStatementDiff) {
        if (!this.id.equals(yStatementDiff.id)) {
            return false;
        }

        for (Map.Entry<YStatementField, StringDiff> entry : diff.entrySet()) {
            StringDiff stringDiff = yStatementDiff.diff.get(entry.getKey());
            if (stringDiff != null) {
                if (entry.getValue().conflictsWith(stringDiff)) {
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

    public YStatementJustificationWrapper applyNonConflicting(YStatementDiff yStatementDiff) {
        YStatementJustificationWrapper appliedDecision =
                new YStatementJustificationWrapperBuilder(baseDecision).build();
        diff.entrySet().forEach(entry -> {
            StringDiff diff = yStatementDiff.diff.get(entry.getKey());
            switch (entry.getKey()) {
                case CONTEXT:
                    if (diff != null) {
                        appliedDecision.setContext(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setContext(entry.getValue().apply());
                    }
                    break;
                case FACING:
                    if (diff != null) {
                        appliedDecision.setFacing(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setFacing(entry.getValue().apply());
                    }
                    break;
                case CHOSEN:
                    if (diff != null) {
                        appliedDecision.setChosen(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setChosen(entry.getValue().apply());
                    }
                    break;
                case NEGLECTED:
                    if (diff != null) {
                        appliedDecision.setNeglected(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setNeglected(entry.getValue().apply());
                    }
                    break;
                case ACHIEVING:
                    if (diff != null) {
                        appliedDecision.setAchieving(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setAchieving(entry.getValue().apply());
                    }
                    break;
                case ACCEPTING:
                    if (diff != null) {
                        appliedDecision.setAccepting(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setAccepting(entry.getValue().apply());
                    }
                    break;
                case MORE_INFORMATION:
                    if (diff != null) {
                        appliedDecision.setMoreInformation(entry.getValue().applyNonConflicting(diff));
                    } else {
                        appliedDecision.setMoreInformation(entry.getValue().apply());
                    }
                    break;
            }

        });
        return appliedDecision;
    }

    public YStatementJustificationWrapper applyDiff() {
        return this.changedDecision;
    }

    public String getId() {
        return id;
    }
}
