package com.eadlsync.diff;

import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.diff.DiffManager;

import java.util.Arrays;

/**
 *
 */
public class DiffTest extends YStatementTestData {

    protected DiffManager decisionsOfOnlyLocalChanges() {
        return createDecisions(clone(baseDecision), clone(someDecision), clone(baseDecision));
    }

    protected DiffManager decisionsOfAdditionalLocalDecision() {
        return new DiffManager(Arrays.asList(clone(baseDecision)), Arrays.asList(clone(baseDecision),
                differentBaseDecision), Arrays.asList(clone(baseDecision)));
    }

    protected DiffManager decisionsOfRemovedLocalDecision() {
        return new DiffManager(Arrays.asList(clone(baseDecision), differentBaseDecision), Arrays
                .asList(clone(baseDecision)), Arrays.asList(clone(baseDecision), differentBaseDecision));
    }

    protected DiffManager decisionsOfOnlyRemoteChanges() {
        return createDecisions(clone(baseDecision), clone(baseDecision), clone(someDecision));
    }

    protected DiffManager decisionsOfAdditionalRemoteDecision() {
        return new DiffManager(Arrays.asList(clone(baseDecision)), Arrays.asList(clone(baseDecision)),
                Arrays.asList(clone(baseDecision), differentBaseDecision));
    }

    protected DiffManager decisionsOfRemovedRemoteDecision() {
        return new DiffManager(Arrays.asList(clone(baseDecision), differentBaseDecision), Arrays
                .asList(clone(baseDecision), differentBaseDecision), Arrays.asList(clone(baseDecision)));
    }

    protected DiffManager decisionsOfLocalAndRemoteChangesNoConflict() {
        return createDecisions(clone(baseDecision), clone(someDecision), clone
                (someNonConflictingDecision));
    }

    protected DiffManager decisionsOfLocalAndRemoteChangesConflict() {
        return createDecisions(clone(baseDecision), clone(someDecision), clone
                (someConflictingOtherDecision));
    }

    protected DiffManager createDecisions(YStatementJustificationWrapper base,
                                        YStatementJustificationWrapper local,
                                        YStatementJustificationWrapper remote) {
        return new DiffManager(Arrays.asList(base), Arrays.asList(local), Arrays.asList(remote));
    }

}
