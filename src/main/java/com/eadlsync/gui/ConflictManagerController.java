package com.eadlsync.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import com.eadlsync.model.diff.ConflictManagerViewModel;
import com.eadlsync.model.diff.DiffManager;

/**
 * Created by tobias on 17/06/2017.
 */
public class ConflictManagerController {

    private ConflictManagerViewModel conflictManagerViewModel;
    private DiffManager diffManager;
    private boolean isFinishedSuccessfully = false;

    @FXML private Label lblTitle;
    @FXML  private ListView listDecisions;
    @FXML  private Button btnFinish;
    @FXML  private Button btnNext;
    @FXML  private Label lblMergedContext;
    @FXML  private Label lblMergedFacing;
    @FXML  private Label lblMergedChosen;
    @FXML  private Label lblMergedNeglected;
    @FXML  private Label lblMergedAchieving;
    @FXML  private Label lblMergedAccepting;
    @FXML  private TextFlow txtLocalContext;
    @FXML  private TextFlow txtLocalFacing;
    @FXML  private TextFlow txtLocalChosen;
    @FXML  private TextFlow txtLocalNeglected;
    @FXML  private TextFlow txtLocalAchieving;
    @FXML  private TextFlow txtLocalAccepting;
    @FXML  private TextFlow txtRemoteContext;
    @FXML  private TextFlow txtRemoteFacing;
    @FXML  private TextFlow txtRemoteChosen;
    @FXML  private TextFlow txtRemoteNeglected;
    @FXML  private TextFlow txtRemoteAchieving;
    @FXML  private TextFlow txtRemoteAccepting;
    @FXML  private RadioButton btnLocalContext;
    @FXML  private RadioButton btnLocalFacing;
    @FXML  private RadioButton btnLocalChosen;
    @FXML  private RadioButton btnLocalNeglected;
    @FXML  private RadioButton btnLocalAchieving;
    @FXML  private RadioButton btnLocalAccepting;
    @FXML  private RadioButton btnRemoteContext;
    @FXML  private RadioButton btnRemoteFacing;
    @FXML  private RadioButton btnRemoteChosen;
    @FXML  private RadioButton btnRemoteNeglected;
    @FXML  private RadioButton btnRemoteAchieving;
    @FXML  private RadioButton btnRemoteAccepting;
    @FXML  private RadioButton btnLocalContextNeglected;
    @FXML  private RadioButton btnLocalFacingNeglected;
    @FXML  private RadioButton btnLocalChosenNeglected;
    @FXML  private RadioButton btnLocalNeglectedNeglected;
    @FXML  private RadioButton btnLocalAchievingNeglected;
    @FXML  private RadioButton btnLocalAcceptingNeglected;
    @FXML  private RadioButton btnRemoteContextNeglected;
    @FXML  private RadioButton btnRemoteFacingNeglected;
    @FXML  private RadioButton btnRemoteChosenNeglected;
    @FXML  private RadioButton btnRemoteNeglectedNeglected;
    @FXML  private RadioButton btnRemoteAchievingNeglected;
    @FXML  private RadioButton btnRemoteAcceptingNeglected;
    @FXML  private RadioButton btnAllLocal;
    @FXML  private RadioButton btnAllLocalNeglected;
    @FXML  private RadioButton btnAllRemote;
    @FXML  private RadioButton btnAllRemoteNeglected;


    public ConflictManagerController(DiffManager diffManager) {
        this.diffManager = diffManager;
        conflictManagerViewModel = new ConflictManagerViewModel(diffManager);
    }

    @FXML
    private void initialize() {
        lblTitle.textProperty().bind(conflictManagerViewModel.currentIdProperty());
        btnNext.disableProperty().bind(conflictManagerViewModel.canGoToNextConflictProperty().not());
        btnFinish.disableProperty().bind(conflictManagerViewModel.isAllConflictsResolvedProperty().not());

        txtLocalContext.getChildren().add(new Text(conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision().getContext()));
        txtLocalFacing.getChildren().add(new Text(conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision().getFacing()));
        txtLocalChosen.getChildren().add(new Text(conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision().getChosen()));
        txtLocalNeglected.getChildren().add(new Text(conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision().getNeglected()));
        txtLocalAchieving.getChildren().add(new Text(conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision().getAchieving()));
        txtLocalAccepting.getChildren().add(new Text(conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision().getAccepting()));

        conflictManagerViewModel.currentLocalDecisionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtLocalContext.getChildren().setAll(new Text(newValue.getChangedDecision().getContext()));
                txtLocalFacing.getChildren().setAll(new Text(newValue.getChangedDecision().getFacing()));
                txtLocalChosen.getChildren().setAll(new Text(newValue.getChangedDecision().getChosen()));
                txtLocalNeglected.getChildren().setAll(new Text(newValue.getChangedDecision().getNeglected()));
                txtLocalAchieving.getChildren().setAll(new Text(newValue.getChangedDecision().getAchieving()));
                txtLocalAccepting.getChildren().setAll(new Text(newValue.getChangedDecision().getAccepting()));
            }
        });

        txtRemoteContext.getChildren().add(new Text(conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision().getContext()));
        txtRemoteFacing.getChildren().add(new Text(conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision().getFacing()));
        txtRemoteChosen.getChildren().add(new Text(conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision().getChosen()));
        txtRemoteNeglected.getChildren().add(new Text(conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision().getNeglected()));
        txtRemoteAchieving.getChildren().add(new Text(conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision().getAchieving()));
        txtRemoteAccepting.getChildren().add(new Text(conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision().getAccepting()));

        conflictManagerViewModel.currentRemoteDecisionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtRemoteContext.getChildren().setAll(new Text(newValue.getChangedDecision().getContext()));
                txtRemoteFacing.getChildren().setAll(new Text(newValue.getChangedDecision().getFacing()));
                txtRemoteChosen.getChildren().setAll(new Text(newValue.getChangedDecision().getChosen()));
                txtRemoteNeglected.getChildren().setAll(new Text(newValue.getChangedDecision().getNeglected()));
                txtRemoteAchieving.getChildren().setAll(new Text(newValue.getChangedDecision().getAchieving()));
                txtRemoteAccepting.getChildren().setAll(new Text(newValue.getChangedDecision().getAccepting()));
            }
        });

        lblMergedContext.textProperty().bind(conflictManagerViewModel.mergedContextProperty());
        lblMergedFacing.textProperty().bind(conflictManagerViewModel.mergedFacingProperty());
        lblMergedChosen.textProperty().bind(conflictManagerViewModel.mergedChosenProperty());
        lblMergedNeglected.textProperty().bind(conflictManagerViewModel.mergedNeglectedProperty());
        lblMergedAchieving.textProperty().bind(conflictManagerViewModel.mergedAchievingProperty());
        lblMergedAccepting.textProperty().bind(conflictManagerViewModel.mergedAcceptingProperty());


        // bindings for visibility of checkboxes
        btnLocalContext.visibleProperty().bind(conflictManagerViewModel.isLocalContextChangedProperty());
        btnLocalFacing.visibleProperty().bind(conflictManagerViewModel.isLocalFacingChangedProperty());
        btnLocalChosen.visibleProperty().bind(conflictManagerViewModel.isLocalChosenChangedProperty());
        btnLocalNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalNeglectedChangedProperty());
        btnLocalAchieving.visibleProperty().bind(conflictManagerViewModel.isLocalAchievingChangedProperty());
        btnLocalAccepting.visibleProperty().bind(conflictManagerViewModel.isLocalAcceptingChangedProperty());
        btnRemoteContext.visibleProperty().bind(conflictManagerViewModel.isRemoteContextChangedProperty());
        btnRemoteFacing.visibleProperty().bind(conflictManagerViewModel.isRemoteFacingChangedProperty());
        btnRemoteChosen.visibleProperty().bind(conflictManagerViewModel.isRemoteChosenChangedProperty());
        btnRemoteNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteNeglectedChangedProperty());
        btnRemoteAchieving.visibleProperty().bind(conflictManagerViewModel.isRemoteAchievingChangedProperty());
        btnRemoteAccepting.visibleProperty().bind(conflictManagerViewModel.isRemoteAcceptingChangedProperty());
        btnLocalContextNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalContextChangedProperty());
        btnLocalFacingNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalFacingChangedProperty());
        btnLocalChosenNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalChosenChangedProperty());
        btnLocalNeglectedNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalNeglectedChangedProperty());
        btnLocalAchievingNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalAchievingChangedProperty());
        btnLocalAcceptingNeglected.visibleProperty().bind(conflictManagerViewModel.isLocalAcceptingChangedProperty());
        btnRemoteContextNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteContextChangedProperty());
        btnRemoteFacingNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteFacingChangedProperty());
        btnRemoteChosenNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteChosenChangedProperty());
        btnRemoteNeglectedNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteNeglectedChangedProperty());
        btnRemoteAchievingNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteAchievingChangedProperty());
        btnRemoteAcceptingNeglected.visibleProperty().bind(conflictManagerViewModel.isRemoteAcceptingChangedProperty());

        // bindings for selection of checkboxes
        btnLocalContext.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalContextSelectedProperty());
        btnLocalFacing.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalFacingSelectedProperty());
        btnLocalChosen.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalChosenSelectedProperty());
        btnLocalNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalNeglectedSelectedProperty());
        btnLocalAchieving.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalAchievingSelectedProperty());
        btnLocalAccepting.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalAcceptingSelectedProperty());
        btnRemoteContext.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteContextSelectedProperty());
        btnRemoteFacing.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteFacingSelectedProperty());
        btnRemoteChosen.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteChosenSelectedProperty());
        btnRemoteNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteNeglectedSelectedProperty());
        btnRemoteAchieving.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteAchievingSelectedProperty());
        btnRemoteAccepting.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteAcceptingSelectedProperty());
        btnLocalContextNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalContextNeglectedProperty());
        btnLocalFacingNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalFacingNeglectedProperty());
        btnLocalChosenNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalChosenNeglectedProperty());
        btnLocalNeglectedNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalNeglectedNeglectedProperty());
        btnLocalAchievingNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalAchievingNeglectedProperty());
        btnLocalAcceptingNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isLocalAcceptingNeglectedProperty());
        btnRemoteContextNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteContextNeglectedProperty());
        btnRemoteFacingNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteFacingNeglectedProperty());
        btnRemoteChosenNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteChosenNeglectedProperty());
        btnRemoteNeglectedNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteNeglectedNeglectedProperty());
        btnRemoteAchievingNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteAchievingNeglectedProperty());
        btnRemoteAcceptingNeglected.selectedProperty().bindBidirectional(conflictManagerViewModel.isRemoteAcceptingNeglectedProperty());

        conflictManagerViewModel.isAllLocalSelectedProperty().addListener((observable, oldValue, newValue) -> btnAllLocal.setSelected(newValue));
        btnAllLocal.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) conflictManagerViewModel.setAllLocalSelected(true);
        });
        conflictManagerViewModel.isAllLocalNeglectedProperty().addListener((observable, oldValue, newValue) -> btnAllLocalNeglected.setSelected(newValue));
        btnAllLocalNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) conflictManagerViewModel.setAllLocalSelected(false);
        });
        conflictManagerViewModel.isAllRemoteSelectedProperty().addListener((observable, oldValue, newValue) -> btnAllRemote.setSelected(newValue));
        btnAllRemote.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) conflictManagerViewModel.setAllRemoteSelected(true);
        });
        conflictManagerViewModel.isAllRemoteNeglectedProperty().addListener((observable, oldValue, newValue) -> btnAllRemoteNeglected.setSelected(newValue));
        btnAllRemoteNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) conflictManagerViewModel.setAllRemoteSelected(false);
        });

        listDecisions.itemsProperty().bind(conflictManagerViewModel.idsProperty());
        listDecisions.getSelectionModel().selectFirst();
        listDecisions.setDisable(true);

        conflictManagerViewModel.currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                listDecisions.getSelectionModel().select(newValue.intValue());
            }
        });

        setListenersForStyling();
    }

    private void setListenersForStyling() {
        btnLocalContext.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedContext.getParent().getStyleClass().add("modified-merge");
                txtLocalContext.getParent().getParent().getStyleClass().add("modified");
            } else {
                lblMergedContext.getParent().getStyleClass().remove("modified-merge");
                txtLocalContext.getParent().getParent().getStyleClass().remove("modified");
            }
        });
        btnLocalFacing.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedFacing.getParent().getStyleClass().add("modified-merge");
                txtLocalFacing.getParent().getParent().getStyleClass().add("modified");
            } else {
                lblMergedFacing.getParent().getStyleClass().remove("modified-merge");
                txtLocalFacing.getParent().getParent().getStyleClass().remove("modified");
            }
        });
        btnLocalChosen.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedChosen.getParent().getStyleClass().add("modified-merge");
                txtLocalChosen.getParent().getParent().getStyleClass().add("modified");
            } else {
                lblMergedChosen.getParent().getStyleClass().remove("modified-merge");
                txtLocalChosen.getParent().getParent().getStyleClass().remove("modified");
            }
        });
        btnLocalNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedNeglected.getParent().getStyleClass().add("modified-merge");
                txtLocalNeglected.getParent().getParent().getStyleClass().add("modified");
            } else {
                lblMergedNeglected.getParent().getStyleClass().remove("modified-merge");
                txtLocalNeglected.getParent().getParent().getStyleClass().remove("modified");
            }
        });
        btnLocalAchieving.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedAchieving.getParent().getStyleClass().add("modified-merge");
                txtLocalAchieving.getParent().getParent().getStyleClass().add("modified");
            } else {
                lblMergedAchieving.getParent().getStyleClass().remove("modified-merge");
                txtLocalAchieving.getParent().getParent().getStyleClass().remove("modified");
            }
        });
        btnLocalAccepting.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedAccepting.getParent().getStyleClass().add("modified-merge");
                txtLocalAccepting.getParent().getParent().getStyleClass().add("modified");
            } else {
                lblMergedAccepting.getParent().getStyleClass().remove("modified-merge");
                txtLocalAccepting.getParent().getParent().getStyleClass().remove("modified");
            }
        });
        btnRemoteContext.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedContext.getParent().getStyleClass().add("modified-merge-remote");
                txtRemoteContext.getParent().getParent().getStyleClass().add("modified-remote");
            } else {
                lblMergedContext.getParent().getStyleClass().remove("modified-merge-remote");
                txtRemoteContext.getParent().getParent().getStyleClass().remove("modified-remote");
            }
        });
        btnRemoteFacing.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedFacing.getParent().getStyleClass().add("modified-merge-remote");
                txtRemoteFacing.getParent().getParent().getStyleClass().add("modified-remote");
            } else {
                lblMergedFacing.getParent().getStyleClass().remove("modified-merge-remote");
                txtRemoteFacing.getParent().getParent().getStyleClass().remove("modified-remote");
            }
        });
        btnRemoteChosen.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedChosen.getParent().getStyleClass().add("modified-merge-remote");
                txtRemoteChosen.getParent().getParent().getStyleClass().add("modified-remote");
            } else {
                lblMergedChosen.getParent().getStyleClass().remove("modified-merge-remote");
                txtRemoteChosen.getParent().getParent().getStyleClass().remove("modified-remote");
            }
        });
        btnRemoteNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedNeglected.getParent().getStyleClass().add("modified-merge-remote");
                txtRemoteNeglected.getParent().getParent().getStyleClass().add("modified-remote");
            } else {
                lblMergedNeglected.getParent().getStyleClass().remove("modified-merge-remote");
                txtRemoteNeglected.getParent().getParent().getStyleClass().remove("modified-remote");
            }
        });
        btnRemoteAchieving.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedAchieving.getParent().getStyleClass().add("modified-merge-remote");
                txtRemoteAchieving.getParent().getParent().getStyleClass().add("modified-remote");
            } else {
                lblMergedAchieving.getParent().getStyleClass().remove("modified-merge-remote");
                txtRemoteAchieving.getParent().getParent().getStyleClass().remove("modified-remote");
            }
        });
        btnRemoteAccepting.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblMergedAccepting.getParent().getStyleClass().add("modified-merge-remote");
                txtRemoteAccepting.getParent().getParent().getStyleClass().add("modified-remote");
            } else {
                lblMergedAccepting.getParent().getStyleClass().remove("modified-merge-remote");
                txtRemoteAccepting.getParent().getParent().getStyleClass().remove("modified-remote");
            }
        });
        btnLocalContextNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtLocalContext.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtLocalContext.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnLocalFacingNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtLocalFacing.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtLocalFacing.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnLocalChosenNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtLocalChosen.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtLocalChosen.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnLocalNeglectedNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtLocalNeglected.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtLocalNeglected.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnLocalAchievingNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtLocalAchieving.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtLocalAchieving.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnLocalAcceptingNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtLocalAccepting.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtLocalAccepting.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnRemoteContextNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtRemoteContext.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtRemoteContext.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnRemoteFacingNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtRemoteFacing.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtRemoteFacing.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnRemoteChosenNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtRemoteChosen.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtRemoteChosen.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnRemoteNeglectedNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtRemoteNeglected.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtRemoteNeglected.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnRemoteAchievingNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtRemoteAchieving.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtRemoteAchieving.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
        btnRemoteAcceptingNeglected.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtRemoteAccepting.getChildren().forEach(node -> node.getStyleClass().add("neglected"));
            } else {
                txtRemoteAccepting.getChildren().forEach(node -> node.getStyleClass().remove("neglected"));
            }
        });
    }

    @FXML
    public void goNext() {
        conflictManagerViewModel.goToNextConflict();
    }

    @FXML
    public void resolveConflicts() {
        conflictManagerViewModel.finishResolvingCurrentConflict();
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
