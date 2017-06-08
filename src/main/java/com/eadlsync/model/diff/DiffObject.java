package com.eadlsync.model.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eadlsync.model.decision.YStatementJustificationComparisionObject;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.YStatementJustificationComparator;

/**
 *
 */
public class DiffObject {

    private final List<YStatementDiff> localDiff = new ArrayList<>();
    private final List<YStatementJustificationWrapper> additionalLocalYStatements = new ArrayList<>();
    private final List<YStatementJustificationWrapper> removedLocalYStatements = new ArrayList<>();

    private final List<YStatementDiff> remoteDiff = new ArrayList<>();
    private final List<YStatementJustificationWrapper> additionalRemoteYStatements = new ArrayList<>();
    private final List<YStatementJustificationWrapper> removedRemoteYStatements = new ArrayList<>();


    public DiffObject(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> local, List<YStatementJustificationWrapper> remote) {
        this.additionalLocalYStatements.addAll(initAdditionalYStatements(base, local));
        this.removedLocalYStatements.addAll(initRemovedYStatements(base, local));
        this.localDiff.addAll(initDiffYStatements(base, local));
        this.additionalRemoteYStatements.addAll(initAdditionalYStatements(base, remote));
        this.removedRemoteYStatements.addAll(initRemovedYStatements(base, remote));
        this.remoteDiff.addAll(initDiffYStatements(base, remote));
    }

    private ArrayList<YStatementDiff> initDiffYStatements(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> changed) {
        ArrayList<YStatementDiff> diff = new ArrayList<>();
        initDifferentYStatements(base, changed).stream().forEach(y ->
            diff.add(YStatementDiff.of(y.getCodeDecision(), y.getSeDecision()))
        );
        return diff;
    }

    private ArrayList<YStatementJustificationWrapper> initAdditionalYStatements(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> changed) {
        ArrayList<YStatementJustificationWrapper> additional = new ArrayList<>();
        for (YStatementJustificationWrapper yStatementJustification : changed) {
            boolean isNotAvailable = base.stream().filter(y ->
                    YStatementJustificationComparator.isSame(y, yStatementJustification))
                    .collect(Collectors.toList())
                    .isEmpty();
            if (isNotAvailable) {
                additional.add(yStatementJustification);
            }
        }
        return additional;
    }

    private ArrayList<YStatementJustificationWrapper> initRemovedYStatements(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> changed) {
        ArrayList<YStatementJustificationWrapper> removed = new ArrayList<>();
        for (YStatementJustificationWrapper yStatementJustification : base) {
            boolean isNotAvailable = changed.stream().filter(y ->
                    YStatementJustificationComparator.isSame(y, yStatementJustification))
                    .collect(Collectors.toList())
                    .isEmpty();
            if (isNotAvailable) {
                removed.add(yStatementJustification);
            }
        }
        return removed;
    }

    private ArrayList<YStatementJustificationComparisionObject> initDifferentYStatements(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> changed) {
        ArrayList<YStatementJustificationComparisionObject> different = new ArrayList<>();
        for (YStatementJustificationWrapper yStatement : base) {
            List<YStatementJustificationWrapper> seSameYStatements = changed.stream().filter(y ->
                    YStatementJustificationComparator.isSame(y, yStatement))
                    .collect(Collectors.toList());
            if (!seSameYStatements.isEmpty()) {
                YStatementJustificationComparisionObject decisionCompareObject = new
                        YStatementJustificationComparisionObject(yStatement, seSameYStatements.get(0));
                if (decisionCompareObject.hasSameObjectWithDifferentFields()) {
                    different.add(decisionCompareObject);
                }
            }
        }
        return different;
    }
}
