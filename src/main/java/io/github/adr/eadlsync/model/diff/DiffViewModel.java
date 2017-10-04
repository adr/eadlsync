package io.github.adr.eadlsync.model.diff;

import io.github.adr.eadlsync.model.decision.YStatementJustificationComparisionObject;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the underlying model for the diff view class. The diffController class maps
 * the user input to the appropriate method call in this class. This class will then process
 * the logic and change its state. The DiffController will gets notified about the changes
 * and updates the view.
 */
public class DiffViewModel {

    private List<YStatementJustificationWrapper> localDecisions = new ArrayList<>();
    private List<YStatementJustificationWrapper> remoteDecisions = new ArrayList<>();
    private ListProperty<String> ids = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<YStatementJustificationWrapper> currentLocalDecision = new SimpleObjectProperty<>();
    private ObjectProperty<YStatementJustificationWrapper> currentRemoteDecision = new SimpleObjectProperty<>();
    private StringProperty currentId = new SimpleStringProperty();
    private IntegerProperty currentIndex = new SimpleIntegerProperty();
    private BooleanProperty canGoToNextDecision = new SimpleBooleanProperty(false);
    private BooleanProperty canGoToPreviousDecision = new SimpleBooleanProperty(false);

    public DiffViewModel(List<YStatementJustificationComparisionObject> diff) {
        currentIndex.set(-1);

        diff.forEach(yDiff -> {
            localDecisions.add(yDiff.getCodeDecision());
            remoteDecisions.add(yDiff.getSeDecision());
        });

        ids.addAll(localDecisions.stream().map(YStatementJustificationWrapper::getId).collect(Collectors
                .toList()));

        setBindings();

        currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                updateDecisions();
            }
        });

        goToNextDecision();
    }

    private void setBindings() {
        canGoToNextDecision.bind(currentIndex.lessThan(localDecisions.size() - 1));
        canGoToPreviousDecision.bind(currentIndex.greaterThan(0));

        currentLocalDecision.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentId.setValue(newValue.getId());
            }
        });
    }

    private void updateDecisions() {
        currentLocalDecision.set(localDecisions.get(currentIndex.get()));
        currentRemoteDecision.set(remoteDecisions.get(currentIndex.get()));
    }

    public void goToNextDecision() {
        currentIndex.set(currentIndex.getValue() + 1);
        updateDecisions();
    }

    public void goToPreviousDecision() {
        currentIndex.set(currentIndex.getValue() - 1);
        updateDecisions();
    }

    public YStatementJustificationWrapper getCurrentLocalDecision() {
        return currentLocalDecision.get();
    }

    public YStatementJustificationWrapper getCurrentRemoteDecision() {
        return currentRemoteDecision.get();
    }

    public ListProperty idsProperty() {
        return ids;
    }

    public StringProperty currentIdProperty() {
        return this.currentId;
    }

    public BooleanProperty canGoToPreviousDecisionProperty() {
        return canGoToPreviousDecision;
    }

    public BooleanProperty canGoToNextDecisionProperty() {
        return canGoToNextDecision;
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public ObjectProperty<YStatementJustificationWrapper> currentLocalDecisionProperty() {
        return currentLocalDecision;
    }

    public ObjectProperty<YStatementJustificationWrapper> currentRemoteDecisionProperty() {
        return currentRemoteDecision;
    }
}
