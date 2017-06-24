package com.eadlsync.model.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.util.YStatementField;
import com.eadlsync.util.YStatementJustificationComparator;

/**
 * Created by tobias on 17/06/2017.
 */
public class ConflictManagerViewModel {

    private List<YStatementDiff> localConflictingDecisions = new ArrayList<>();
    private List<YStatementDiff> remoteConflictingDecisions = new ArrayList<>();
    private List<YStatementJustificationWrapper> resultingDecisions = new ArrayList<>();
    private ListProperty<String> ids = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<YStatementDiff> currentLocalDecision = new SimpleObjectProperty<>();
    private ObjectProperty<YStatementDiff> currentRemoteDecision = new SimpleObjectProperty<>();
    private StringProperty currentId = new SimpleStringProperty();
    private IntegerProperty currentIndex = new SimpleIntegerProperty();
    private BooleanProperty isCurrentConflictResolved = new SimpleBooleanProperty(false);
    private BooleanProperty canGoToNextConflict = new SimpleBooleanProperty(false);
    private BooleanProperty isAllConflictsResolved = new SimpleBooleanProperty(false);
    private StringProperty mergedContext = new SimpleStringProperty();
    private StringProperty mergedFacing = new SimpleStringProperty();
    private StringProperty mergedChosen = new SimpleStringProperty();
    private StringProperty mergedNeglected = new SimpleStringProperty();
    private StringProperty mergedAchieving = new SimpleStringProperty();
    private StringProperty mergedAccepting = new SimpleStringProperty();
    private BooleanProperty isLocalContextChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isLocalFacingChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isLocalChosenChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isLocalNeglectedChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isLocalAchievingChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isLocalAcceptingChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isRemoteContextChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isRemoteFacingChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isRemoteChosenChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isRemoteNeglectedChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isRemoteAchievingChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isRemoteAcceptingChanged = new SimpleBooleanProperty(true);
    private BooleanProperty isLocalContextSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalFacingSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalChosenSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalNeglectedSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalAchievingSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalAcceptingSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteContextSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteFacingSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteChosenSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteNeglectedSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteAchievingSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteAcceptingSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalContextNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalFacingNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalChosenNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalNeglectedNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalAchievingNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isLocalAcceptingNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteContextNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteFacingNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteChosenNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteNeglectedNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteAchievingNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isRemoteAcceptingNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isAllLocalSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isAllLocalNeglected = new SimpleBooleanProperty(false);
    private BooleanProperty isAllRemoteSelected = new SimpleBooleanProperty(false);
    private BooleanProperty isAllRemoteNeglected = new SimpleBooleanProperty(false);
    private String context = "";
    private String facing = "";
    private String chosen = "";
    private String neglected = "";
    private String achieving = "";
    private String accepting = "";

    public ConflictManagerViewModel(DiffManager diffManager) {
        resultingDecisions.addAll(diffManager.getCurrentDecisions());
        diffManager.getLocalDiff().forEach(localYStatementDiff -> diffManager.getRemoteDiff().forEach
                (remoteYStatementDiff -> {
            if (YStatementJustificationComparator.isSame(localYStatementDiff.getChangedDecision(),
                    remoteYStatementDiff.getChangedDecision())) {
                if (localYStatementDiff.conflictsWith(remoteYStatementDiff)) {
                    localConflictingDecisions.add(localYStatementDiff);
                    remoteConflictingDecisions.add(remoteYStatementDiff);
                } else if (!isLocalContextSelected.get()) {
                    localYStatementDiff.applyDiff(resultingDecisions);
                    remoteYStatementDiff.applyDiff(resultingDecisions);
                }
            }
        }));
        ids.addAll(localConflictingDecisions.stream().map(YStatementDiff::getId).collect(Collectors
                .toList()));
        setBindings();
        goToNextConflict();
    }

    private void setBindings() {
        currentIndex.set(-1);
        isAllLocalSelected.bind(isLocalContextSelected.and(isLocalFacingSelected).and(isLocalChosenSelected).and(isLocalNeglectedSelected).and(isLocalAchievingSelected).and(isLocalAcceptingSelected));
        isAllLocalNeglected.bind(isLocalContextNeglected.and(isLocalFacingNeglected).and(isLocalChosenNeglected).and(isLocalNeglectedNeglected).and(isLocalAchievingNeglected).and(isLocalAcceptingNeglected));
        isAllRemoteSelected.bind(isRemoteContextSelected.and(isRemoteFacingSelected).and(isRemoteChosenSelected).and(isRemoteNeglectedSelected).and(isRemoteAchievingSelected).and(isRemoteAcceptingSelected));
        isAllRemoteNeglected.bind(isRemoteContextNeglected.and(isRemoteFacingNeglected).and(isRemoteChosenNeglected).and(isRemoteNeglectedNeglected).and(isRemoteAchievingNeglected).and(isRemoteAcceptingNeglected));
                
        isCurrentConflictResolved.bind(isLocalContextSelected.or(isLocalContextNeglected).and
                (isRemoteContextSelected.or(isRemoteContextNeglected)).
                and(isLocalFacingSelected.or(isLocalFacingNeglected).and(isRemoteFacingSelected.or
                        (isRemoteFacingNeglected))).
                and(isLocalChosenSelected.or(isLocalChosenNeglected).and(isRemoteChosenSelected.or
                        (isRemoteChosenNeglected))).
                and(isLocalNeglectedSelected.or(isLocalNeglectedNeglected).and
                        (isRemoteNeglectedSelected.or(isRemoteNeglectedNeglected))).
                and(isLocalAchievingSelected.or(isLocalAchievingNeglected).and
                        (isRemoteAchievingSelected.or(isRemoteAchievingNeglected))).
                and(isLocalAcceptingSelected.or(isLocalAcceptingNeglected).and
                        (isRemoteAcceptingSelected.or(isRemoteAcceptingNeglected))));

        canGoToNextConflict.bind(currentIndex.lessThan(localConflictingDecisions.size() - 1).and
                (isCurrentConflictResolved));
        isAllConflictsResolved.bind(isCurrentConflictResolved.and(canGoToNextConflict.not()));

        currentLocalDecision.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentId.setValue(newValue.getId());
                isLocalContextChanged.set(newValue.getDiff().containsKey(YStatementField.CONTEXT));
                isLocalContextNeglected.set(!isLocalContextChanged.get());
                isLocalContextSelected.set(!isLocalContextChanged.get());
                isLocalFacingChanged.set(newValue.getDiff().containsKey(YStatementField.FACING));
                isLocalFacingNeglected.set(!isLocalFacingChanged.get());
                isLocalFacingSelected.set(!isLocalFacingChanged.get());
                isLocalChosenChanged.set(newValue.getDiff().containsKey(YStatementField.CHOSEN));
                isLocalChosenNeglected.set(!isLocalChosenChanged.get());
                isLocalChosenSelected.set(!isLocalChosenChanged.get());
                isLocalNeglectedChanged.set(newValue.getDiff().containsKey(YStatementField.NEGLECTED));
                isLocalNeglectedNeglected.set(!isLocalNeglectedChanged.get());
                isLocalNeglectedSelected.set(!isLocalNeglectedChanged.get());
                isLocalAchievingChanged.set(newValue.getDiff().containsKey(YStatementField.ACHIEVING));
                isLocalAchievingNeglected.set(!isLocalAchievingChanged.get());
                isLocalAchievingSelected.set(!isLocalAchievingChanged.get());
                isLocalAcceptingChanged.set(newValue.getDiff().containsKey(YStatementField.ACCEPTING));
                isLocalAcceptingNeglected.set(!isLocalAcceptingChanged.get());
                isLocalAcceptingSelected.set(!isLocalAcceptingChanged.get());
            }
        });

        currentRemoteDecision.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isRemoteContextChanged.set(newValue.getDiff().containsKey(YStatementField.CONTEXT));
                isRemoteContextNeglected.set(!isRemoteContextChanged.get());
                isRemoteContextSelected.set(!isRemoteContextChanged.get());
                isRemoteFacingChanged.set(newValue.getDiff().containsKey(YStatementField.FACING));
                isRemoteFacingNeglected.set(!isRemoteFacingChanged.get());
                isRemoteFacingSelected.set(!isRemoteFacingChanged.get());
                isRemoteChosenChanged.set(newValue.getDiff().containsKey(YStatementField.CHOSEN));
                isRemoteChosenNeglected.set(!isRemoteChosenChanged.get());
                isRemoteChosenSelected.set(!isRemoteChosenChanged.get());
                isRemoteNeglectedChanged.set(newValue.getDiff().containsKey(YStatementField.NEGLECTED));
                isRemoteNeglectedNeglected.set(!isRemoteNeglectedChanged.get());
                isRemoteNeglectedSelected.set(!isRemoteNeglectedChanged.get());
                isRemoteAchievingChanged.set(newValue.getDiff().containsKey(YStatementField.ACHIEVING));
                isRemoteAchievingNeglected.set(!isRemoteAchievingChanged.get());
                isRemoteAchievingSelected.set(!isRemoteAchievingChanged.get());
                isRemoteAcceptingChanged.set(newValue.getDiff().containsKey(YStatementField.ACCEPTING));
                isRemoteAcceptingNeglected.set(!isRemoteAcceptingChanged.get());
                isRemoteAcceptingSelected.set(!isRemoteAcceptingChanged.get());
            }
        });

        currentId.addListener((observable, oldValue, newValue) -> {
            List<YStatementJustificationWrapper> decisions = resultingDecisions.stream().filter(d ->
                    newValue.equals(d.getId())).collect(Collectors.toList());
            if (!decisions.isEmpty()) {
                YStatementJustificationWrapper decision = decisions.get(0);
                mergedContext.set(decision.getContext());
                mergedFacing.set(decision.getFacing());
                mergedChosen.set(decision.getChosen());
                mergedNeglected.set(decision.getNeglected());
                mergedAchieving.set(decision.getAchieving());
                mergedAccepting.set(decision.getAccepting());
                context = decision.getContext();
                facing = decision.getFacing();
                chosen = decision.getChosen();
                neglected = decision.getNeglected();
                achieving = decision.getAchieving();
                accepting = decision.getAccepting();
            }
        });

        isLocalContextSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedContext.set(getCurrentLocalDecision().getChangedDecision().getContext());
                isRemoteContextNeglected.set(true);
            } else if (!isRemoteContextSelected.get()) {
                setOriginalContext();
            }
        });
        isLocalFacingSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedFacing.set(getCurrentLocalDecision().getChangedDecision().getFacing());
                isRemoteFacingNeglected.set(true);
            } else if (!isRemoteFacingSelected.get()) {
                setOriginalFacing();
            }
        });
        isLocalChosenSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedChosen.set(getCurrentLocalDecision().getChangedDecision().getChosen());
                isRemoteChosenNeglected.set(true);
            } else if (!isRemoteChosenSelected.get()) {
                setOriginalChosen();
            }
        });
        isLocalNeglectedSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedNeglected.set(getCurrentLocalDecision().getChangedDecision().getNeglected());
                isRemoteNeglectedNeglected.set(true);
            } else if (!isRemoteNeglectedSelected.get()) {
                setOriginalNeglected();
            }
        });
        isLocalAchievingSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedAchieving.set(getCurrentLocalDecision().getChangedDecision().getAchieving());
                isRemoteAchievingNeglected.set(true);
            } else if (!isRemoteAchievingSelected.get()) {
                setOriginalAchieving();
            }
        });
        isLocalAcceptingSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedAccepting.set(getCurrentLocalDecision().getChangedDecision().getAccepting());
                isRemoteAcceptingNeglected.set(true);
            } else if (!isRemoteAcceptingSelected.get()) {
                setOriginalAccepting();
            }
        });
        isRemoteContextSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedContext.set(getCurrentRemoteDecision().getChangedDecision().getContext());
                isLocalContextNeglected.set(true);
            } else if (!isLocalContextSelected.get()) {
                setOriginalContext();
            }
        });
        isRemoteFacingSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedFacing.set(getCurrentRemoteDecision().getChangedDecision().getFacing());
                isLocalFacingNeglected.set(true);
            } else if (!isLocalFacingSelected.get()) {
                setOriginalFacing();
            }
        });
        isRemoteChosenSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedChosen.set(getCurrentRemoteDecision().getChangedDecision().getChosen());
                isLocalChosenNeglected.set(true);
            } else if (!isLocalChosenSelected.get()) {
                setOriginalChosen();
            }
        });
        isRemoteNeglectedSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedNeglected.set(getCurrentRemoteDecision().getChangedDecision().getNeglected());
                isLocalNeglectedNeglected.set(true);
            } else if (!isLocalNeglectedSelected.get()) {
                setOriginalNeglected();
            }
        });
        isRemoteAchievingSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedAchieving.set(getCurrentRemoteDecision().getChangedDecision().getAchieving());
                isLocalAchievingNeglected.set(true);
            } else if (!isLocalAchievingSelected.get()) {
                setOriginalAchieving();
            }
        });
        isRemoteAcceptingSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mergedAccepting.set(getCurrentRemoteDecision().getChangedDecision().getAccepting());
                isLocalAcceptingNeglected.set(true);
            } else if (!isLocalAcceptingSelected.get()) if (!isLocalAcceptingSelected.get()) {
                setOriginalAccepting();
            }
        });
    }

    private void setOriginalContext() {
        mergedContext.set(context);
    }

    private void setOriginalFacing() {
        mergedFacing.set(facing);
    }

    private void setOriginalChosen() {
        mergedChosen.set(chosen);
    }

    private void setOriginalNeglected() {
        mergedNeglected.set(neglected);
    }

    private void setOriginalAchieving() {
        mergedAchieving.set(achieving);
    }

    private void setOriginalAccepting() {
        mergedAccepting.set(accepting);
    }

    public void goToNextConflict() {
        finishResolvingCurrentConflict();
        currentIndex.set(currentIndex.getValue() + 1);
        currentLocalDecision.set(localConflictingDecisions.get(currentIndex.get()));
        currentRemoteDecision.set(remoteConflictingDecisions.get(currentIndex.get()));
    }

    public void finishResolvingCurrentConflict() {
        if (isCurrentConflictResolved.get()) {
            YStatementJustificationWrapper chosenDecision = new YStatementJustificationWrapperBuilder
                    (currentId.get(), currentLocalDecision.get().getChangedDecision().getSource()).
                    context(mergedContext.get()).
                    facing(mergedFacing.get()).
                    chosen(mergedChosen.get()).
                    neglected(mergedNeglected.get()).
                    achieving(mergedAchieving.get()).
                    accepting(mergedAccepting.get()).
                    build();
            for (int index = 0; index < resultingDecisions.size(); index++) {
                YStatementJustificationWrapper decision = resultingDecisions.get(index);
                // if resultingDecision is null find way to remove decision with current id
                if (YStatementJustificationComparator.isSame(decision, chosenDecision)) {
                    resultingDecisions.remove(index);
                    resultingDecisions.add(chosenDecision);
                    break;
                }
            }
        }
    }
    
    public void setAllLocalSelected(boolean value) {
        isLocalContextSelected.set(value);
        isLocalFacingSelected.set(value);
        isLocalChosenSelected.set(value);
        isLocalNeglectedSelected.set(value);
        isLocalAchievingSelected.set(value);
        isLocalAcceptingSelected.set(value);
        isLocalContextNeglected.set(!value);
        isLocalFacingNeglected.set(!value);
        isLocalChosenNeglected.set(!value);
        isLocalNeglectedNeglected.set(!value);
        isLocalAchievingNeglected.set(!value);
        isLocalAcceptingNeglected.set(!value);
    }

    public void setAllRemoteSelected(boolean value) {
        isRemoteContextSelected.set(value);
        isRemoteFacingSelected.set(value);
        isRemoteChosenSelected.set(value);
        isRemoteNeglectedSelected.set(value);
        isRemoteAchievingSelected.set(value);
        isRemoteAcceptingSelected.set(value);
        isRemoteContextNeglected.set(!value);
        isRemoteFacingNeglected.set(!value);
        isRemoteChosenNeglected.set(!value);
        isRemoteNeglectedNeglected.set(!value);
        isRemoteAchievingNeglected.set(!value);
        isRemoteAcceptingNeglected.set(!value);
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

    public BooleanProperty canGoToNextConflictProperty() {
        return canGoToNextConflict;
    }

    public BooleanProperty isAllConflictsResolvedProperty() {
        return isAllConflictsResolved;
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public ObjectProperty<YStatementDiff> currentLocalDecisionProperty() {
        return currentLocalDecision;
    }

    public ObjectProperty<YStatementDiff> currentRemoteDecisionProperty() {
        return currentRemoteDecision;
    }

    public StringProperty mergedContextProperty() {
        return mergedContext;
    }

    public StringProperty mergedFacingProperty() {
        return mergedFacing;
    }

    public StringProperty mergedChosenProperty() {
        return mergedChosen;
    }

    public StringProperty mergedNeglectedProperty() {
        return mergedNeglected;
    }

    public StringProperty mergedAchievingProperty() {
        return mergedAchieving;
    }

    public StringProperty mergedAcceptingProperty() {
        return mergedAccepting;
    }

    public BooleanProperty isLocalContextChangedProperty() {
        return isLocalContextChanged;
    }

    public BooleanProperty isLocalFacingChangedProperty() {
        return isLocalFacingChanged;
    }

    public BooleanProperty isLocalChosenChangedProperty() {
        return isLocalChosenChanged;
    }

    public BooleanProperty isLocalNeglectedChangedProperty() {
        return isLocalNeglectedChanged;
    }

    public BooleanProperty isLocalAchievingChangedProperty() {
        return isLocalAchievingChanged;
    }

    public BooleanProperty isLocalAcceptingChangedProperty() {
        return isLocalAcceptingChanged;
    }

    public BooleanProperty isRemoteContextChangedProperty() {
        return isRemoteContextChanged;
    }

    public BooleanProperty isRemoteFacingChangedProperty() {
        return isRemoteFacingChanged;
    }

    public BooleanProperty isRemoteChosenChangedProperty() {
        return isRemoteChosenChanged;
    }

    public BooleanProperty isRemoteNeglectedChangedProperty() {
        return isRemoteNeglectedChanged;
    }

    public BooleanProperty isRemoteAchievingChangedProperty() {
        return isRemoteAchievingChanged;
    }

    public BooleanProperty isRemoteAcceptingChangedProperty() {
        return isRemoteAcceptingChanged;
    }

    public YStatementDiff getCurrentLocalDecision() {
        return currentLocalDecision.get();
    }

    public YStatementDiff getCurrentRemoteDecision() {
        return currentRemoteDecision.get();
    }

    public BooleanProperty isLocalContextSelectedProperty() {
        return isLocalContextSelected;
    }

    public BooleanProperty isLocalFacingSelectedProperty() {
        return isLocalFacingSelected;
    }

    public BooleanProperty isLocalChosenSelectedProperty() {
        return isLocalChosenSelected;
    }

    public BooleanProperty isLocalNeglectedSelectedProperty() {
        return isLocalNeglectedSelected;
    }

    public BooleanProperty isLocalAchievingSelectedProperty() {
        return isLocalAchievingSelected;
    }

    public BooleanProperty isLocalAcceptingSelectedProperty() {
        return isLocalAcceptingSelected;
    }

    public BooleanProperty isRemoteContextSelectedProperty() {
        return isRemoteContextSelected;
    }

    public BooleanProperty isRemoteFacingSelectedProperty() {
        return isRemoteFacingSelected;
    }

    public BooleanProperty isRemoteChosenSelectedProperty() {
        return isRemoteChosenSelected;
    }

    public BooleanProperty isRemoteNeglectedSelectedProperty() {
        return isRemoteNeglectedSelected;
    }

    public BooleanProperty isRemoteAchievingSelectedProperty() {
        return isRemoteAchievingSelected;
    }

    public BooleanProperty isRemoteAcceptingSelectedProperty() {
        return isRemoteAcceptingSelected;
    }

    public BooleanProperty isLocalContextNeglectedProperty() {
        return isLocalContextNeglected;
    }

    public BooleanProperty isLocalFacingNeglectedProperty() {
        return isLocalFacingNeglected;
    }

    public BooleanProperty isLocalChosenNeglectedProperty() {
        return isLocalChosenNeglected;
    }

    public BooleanProperty isLocalNeglectedNeglectedProperty() {
        return isLocalNeglectedNeglected;
    }

    public BooleanProperty isLocalAchievingNeglectedProperty() {
        return isLocalAchievingNeglected;
    }

    public BooleanProperty isLocalAcceptingNeglectedProperty() {
        return isLocalAcceptingNeglected;
    }

    public BooleanProperty isRemoteContextNeglectedProperty() {
        return isRemoteContextNeglected;
    }

    public BooleanProperty isRemoteFacingNeglectedProperty() {
        return isRemoteFacingNeglected;
    }

    public BooleanProperty isRemoteChosenNeglectedProperty() {
        return isRemoteChosenNeglected;
    }

    public BooleanProperty isRemoteNeglectedNeglectedProperty() {
        return isRemoteNeglectedNeglected;
    }

    public BooleanProperty isRemoteAchievingNeglectedProperty() {
        return isRemoteAchievingNeglected;
    }

    public BooleanProperty isRemoteAcceptingNeglectedProperty() {
        return isRemoteAcceptingNeglected;
    }

    public BooleanProperty isAllLocalSelectedProperty() {
        return isAllLocalSelected;
    }

    public BooleanProperty isAllLocalNeglectedProperty() {
        return isAllLocalNeglected;
    }

    public BooleanProperty isAllRemoteSelectedProperty() {
        return isAllRemoteSelected;
    }

    public BooleanProperty isAllRemoteNeglectedProperty() {
        return isAllRemoteNeglected;
    }
}
