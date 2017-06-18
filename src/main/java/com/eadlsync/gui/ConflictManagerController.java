package com.eadlsync.gui;

import com.eadlsync.model.diff.ConflictManagerViewModel;
import com.eadlsync.model.diff.DiffManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Created by tobias on 17/06/2017.
 */
public class ConflictManagerController {

    private ConflictManagerViewModel conflictManagerViewModel;
    private DiffManager diffManager;
    private boolean isFinishedSuccessfully = false;

    @FXML
    private Label lblTitle;

    @FXML
    private TextArea txtLocalDecision;

    @FXML
    private TextArea txtRemoteDecision;

    @FXML
    private ListView listDecisions;

    @FXML
    private Button btnFinish;

    @FXML
    private Button btnNext;

    @FXML
    private RadioButton radioLocalDecision;

    @FXML
    private RadioButton radioRemoteDecision;

    public ConflictManagerController(DiffManager diffManager) {
        this.diffManager = diffManager;
        conflictManagerViewModel = new ConflictManagerViewModel(diffManager);
    }

    @FXML
    private void initialize() {
        lblTitle.textProperty().bind(conflictManagerViewModel.currentIdProperty());
        txtLocalDecision.textProperty().bind(conflictManagerViewModel.currentLocalDecisionProperty().asString());
        txtRemoteDecision.textProperty().bind(conflictManagerViewModel.currentRemoteDecisionProperty().asString());
        btnNext.disableProperty().bind(conflictManagerViewModel.canGoToNextConflictProperty().not());
        btnFinish.disableProperty().bind(conflictManagerViewModel.isAllConflictsResolvedProperty().not());
        radioLocalDecision.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalDecisionSelectedProperty());
        radioRemoteDecision.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteDecisionSelectedProperty());

        ToggleGroup selectedDecisionGroup = new ToggleGroup();
        radioLocalDecision.setToggleGroup(selectedDecisionGroup);
        radioRemoteDecision.setToggleGroup(selectedDecisionGroup);

        listDecisions.itemsProperty().bind(conflictManagerViewModel.idsProperty());
        listDecisions.getSelectionModel().selectFirst();
        listDecisions.setDisable(true);

        conflictManagerViewModel.currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                listDecisions.getSelectionModel().select(newValue.intValue());
            }
        });

        btnNext.disableProperty().addListener((observable, oldValue, newValue) -> {
            String tooltip;
            if (newValue && btnFinish.disableProperty().get()) {
                tooltip = "Select the decision you want to choose";
            } else if (newValue) {
                tooltip = "No more conflicts, you can finish the merging process";
            } else {
                tooltip = "Go to the next conflicting decision";
            }
            btnNext.setTooltip(new Tooltip(tooltip));
        });

        btnNext.disableProperty().addListener((observable, oldValue, newValue) -> {
            String tooltip;
            if (newValue && btnNext.disableProperty().get()) {
                tooltip = "Select the decision you want to choose";
            } else if (newValue) {
                tooltip = "You need to resolve all the conflicts before you can finish the merging process";
            } else {
                tooltip = "Finish the merging process";
            }
            btnFinish.setTooltip(new Tooltip(tooltip));
        });
    }

    @FXML
    public void goNext() {
        conflictManagerViewModel.goToNextConflict();
    }

    @FXML
    public void resolveConflicts() {
        conflictManagerViewModel.finishResolvingConflicts();
        diffManager.setCurrentDecisions(conflictManagerViewModel.getResultingDecisions());
        isFinishedSuccessfully = true;
        close();
    }

    @FXML
    public void close() {
        Stage stage = (Stage) btnFinish.getScene().getWindow();
        stage.close();
    }

    public boolean isFinishedSuccessfully() {
        return isFinishedSuccessfully;
    }
}
