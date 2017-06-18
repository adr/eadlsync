package com.eadlsync.model.diff;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.YStatementJustificationComparator;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tobias on 17/06/2017.
 */
public class ConflictManagerViewModel {

    private List<YStatementDiff> localConflictingDecisions = new ArrayList<>();
    private List<YStatementDiff> remoteConflictingDecisions = new ArrayList<>();
    private ListProperty<String> ids = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<YStatementJustificationWrapper> currentLocalDecision = new SimpleObjectProperty<>();
    private ObjectProperty<YStatementJustificationWrapper> currentRemoteDecision = new SimpleObjectProperty<>();
    private StringProperty currentId = new SimpleStringProperty();
    private IntegerProperty currentIndex = new SimpleIntegerProperty();
    private BooleanProperty isCurrentConflictResolved = new SimpleBooleanProperty(false);
    private BooleanProperty canGoToNextConflict = new SimpleBooleanProperty(false);
    private BooleanProperty isAllConflictsResolved = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalDecisionSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteDecisionSelected = new SimpleBooleanProperty(false);
    private List<YStatementJustificationWrapper> resultingDecisions = new ArrayList<>();

    public ConflictManagerViewModel(DiffManager diffManager) {
        resultingDecisions.addAll(diffManager.getCurrentDecisions());
        diffManager.getLocalDiff().forEach(localYStatementDiff -> diffManager.getRemoteDiff().forEach(remoteYStatementDiff -> {
            if (localYStatementDiff.conflictsWith(remoteYStatementDiff)) {
                localConflictingDecisions.add(localYStatementDiff);
                remoteConflictingDecisions.add(remoteYStatementDiff);
            } else {
                localYStatementDiff.applyDiff(resultingDecisions);
                remoteYStatementDiff.applyDiff(resultingDecisions);
            }
        }));
        ids.addAll(localConflictingDecisions.stream().map(YStatementDiff::getId).collect(Collectors.toList()));
        setBindings();
        goToNextConflict();
    }

    private void setBindings() {
        currentIndex.set(-1);
        isCurrentConflictResolved.bind(isLocalDecisionSelected.or(isRemoteDecisionSelected));
        canGoToNextConflict.bind(currentIndex.lessThan(localConflictingDecisions.size() - 1).and(isCurrentConflictResolved));
        isAllConflictsResolved.bind(isCurrentConflictResolved.and(canGoToNextConflict.not()));
        currentLocalDecision.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentId.setValue(newValue.getId());
            }
        });
    }

    public void goToNextConflict() {
        deselectDecisions();
        addResolvedConflict();
        currentIndex.set(currentIndex.getValue() +1);
        currentLocalDecision.set(localConflictingDecisions.get(currentIndex.get()).getChangedDecision());
        currentRemoteDecision.set(remoteConflictingDecisions.get(currentIndex.get()).getChangedDecision());
    }

    private void deselectDecisions() {
        isLocalDecisionSelected.setValue(false);
        isRemoteDecisionSelected.setValue(false);
    }

    public void finishResolvingConflicts() {
        addResolvedConflict();
    }

    private void addResolvedConflict() {
        if (isCurrentConflictResolved.get()) {
            YStatementJustificationWrapper chosenDecision;
            if (isLocalDecisionSelected.get()) {
                chosenDecision = currentLocalDecision.get();
            } else {
                chosenDecision = currentRemoteDecision.get();
            }
            for (int index = 0; index < resultingDecisions.size(); index++) {
                YStatementJustificationWrapper decision = resultingDecisions.get(index);
                if (YStatementJustificationComparator.isSame(decision, chosenDecision)) {
                    resultingDecisions.remove(index);
                    resultingDecisions.add(chosenDecision);
                    break;
                }
            }
        }
    }

    public List<YStatementJustificationWrapper> getResultingDecisions() {
        return resultingDecisions;
    }

    public ListProperty idsProperty() {
        return ids;
    }

    public StringProperty currentIdProperty() {
        return this.currentId;
    }

    public ObjectProperty<YStatementJustificationWrapper> currentLocalDecisionProperty() {
        return currentLocalDecision;
    }

    public ObjectProperty<YStatementJustificationWrapper> currentRemoteDecisionProperty() {
        return currentRemoteDecision;
    }

    public BooleanProperty canGoToNextConflictProperty() {
        return canGoToNextConflict;
    }

    public BooleanProperty isAllConflictsResolvedProperty() {
        return isAllConflictsResolved;
    }

    public BooleanProperty isLocalDecisionSelectedProperty() {
        return isLocalDecisionSelected;
    }

    public BooleanProperty isRemoteDecisionSelectedProperty() {
        return isRemoteDecisionSelected;
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }
}
