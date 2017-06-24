package com.eadlsync.model.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eadlsync.EADLSyncExecption;
import com.eadlsync.model.decision.YStatementJustificationComparisionObject;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.YStatementJustificationComparator;

import static com.eadlsync.EADLSyncExecption.EADLSyncOperationState.CONFLICT;

/**
 *
 */
public class DiffManager {

    private final List<YStatementDiff> localDiff = new ArrayList<>();
    private final List<YStatementDiff> remoteDiff = new ArrayList<>();
    private final List<YStatementJustificationWrapper> baseDecisions;
    private List<YStatementJustificationWrapper> currentDecisions;

    public DiffManager(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> local, List<YStatementJustificationWrapper> remote) {
        this.baseDecisions = base;
        this.currentDecisions = base;
        this.localDiff.addAll(initDiffYStatements(base, local));
        this.remoteDiff.addAll(initDiffYStatements(base, remote));
    }

    private ArrayList<YStatementDiff> initDiffYStatements(List<YStatementJustificationWrapper> base, List<YStatementJustificationWrapper> changed) {
        ArrayList<YStatementDiff> diff = new ArrayList<>();
        initDifferentYStatements(base, changed).stream().forEach(y ->
                diff.add(YStatementDiff.of(y.getCodeDecision(), y.getSeDecision()))
        );
//        initAdditionalYStatements(base, changed).stream().forEach(y ->
//                diff.add(YStatementDiff.of(null, y))
//        );
        initRemovedYStatements(base, changed).stream().forEach(y ->
                diff.add(YStatementDiff.of(y, null))
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
                System.out.println("removed " + yStatementJustification);
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
                    System.out.println("changed " + decisionCompareObject);
                }
            }
        }
        return different;
    }

    /**
     * Checks whether there are local decisions which differ from the decisions of the base revision.
     * This means either some fields have different values or a decision has been locally deleted.
     * However additional local decisions are not taken into account just yet.
     *
     * @return true if local diff is not empty
     */
    public boolean hasLocalDiff() {
        return !localDiff.isEmpty();
    }

    /**
     * Checks whether there are remote decisions which differ from the decisions of the base revision.
     * This means either some fields have different values or a decision in the se-repo does not exist anymore.
     * However additional decisions of the se-repo are not taken into account just yet.
     *
     * @return true if remote diff is not empty
     */
    public boolean hasRemoteDiff() {
        return !remoteDiff.isEmpty();
    }

    /**
     * Checks whether there are merge conflicts with the local and the remote diff.
     *
     * Since additional decisions are not yet supported this will return false if there
     * are additional decisions on either the local or the remote repo.
     *
     * @return true if local and remote changes can be automatically merged
     */
    public boolean canAutoMerge() {
        return !localDiff.stream().map(diff -> diff.conflictsWith(remoteDiff)).collect(Collectors.toList()).contains(true);
    }

    /**
     * Applies the diff of the local-repo to the base decisions.
     *
     * @return the decisions of the local repo
     * @throws EADLSyncExecption if automatic merge can not be performed
     */
    public List<YStatementJustificationWrapper> applyLocalDiff() throws EADLSyncExecption {
        for (YStatementDiff diff : localDiff) {
            diff.applyDiff(currentDecisions);
        }
        return this.currentDecisions;
    }

    /**
     * Applies the diff of the se-repo to the base decisions.
     *
     * @return the decisions of the remote repo
     * @throws EADLSyncExecption if automatic merge can not be performed
     */
    public List<YStatementJustificationWrapper> applyRemoteDiff() throws EADLSyncExecption {
        for (YStatementDiff diff : remoteDiff) {
            diff.applyDiff(currentDecisions);
        }
        return this.currentDecisions;
    }

    public List<YStatementJustificationWrapper> applyLocalAndRemoteDiff() throws EADLSyncExecption {
        if (canAutoMerge()) {
            if (hasLocalDiff()) {
                applyLocalDiff();
            }
            if (hasRemoteDiff()) {
                applyRemoteDiff();
            }
        } else {
            throw EADLSyncExecption.ofState(CONFLICT);
        }
        return this.currentDecisions;
    }

    public List<YStatementJustificationWrapper> getCurrentDecisions() {
        return currentDecisions;
    }

    public void setCurrentDecisions(List<YStatementJustificationWrapper> currentDecisions) {
        this.currentDecisions = currentDecisions;
    }

    public List<YStatementJustificationWrapper> getBaseDecisions() {
        return baseDecisions;
    }

    public List<YStatementDiff> getLocalDiff() {
        return localDiff;
    }

    public List<YStatementDiff> getRemoteDiff() {
        return remoteDiff;
    }
}
