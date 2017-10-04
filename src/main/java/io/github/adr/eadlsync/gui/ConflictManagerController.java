package io.github.adr.eadlsync.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import io.github.adr.eadlsync.model.diff.ConflictManagerViewModel;
import io.github.adr.eadlsync.model.diff.DiffManager;

import static io.github.adr.eadlsync.gui.DiffUtilFX.getDiffHighlightedTextNodes;

/**
 *
 */
public class ConflictManagerController {

    private ConflictManagerViewModel conflictManagerViewModel;
    private DiffManager diffManager;
    private boolean isFinishedSuccessfully = false;

    @FXML private Label lblTitle;
    @FXML private ComboBox<DiffUtilFX.DiffType> boxDiffType;
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

        for (DiffUtilFX.DiffType type : DiffUtilFX.DiffType.values()) {
            boxDiffType.getItems().add(type);
        }
        boxDiffType.setValue(DiffUtilFX.DiffType.WORDS);

        updateLocalDecisionFields(boxDiffType.getValue());
        updateRemoteDecisionFields(boxDiffType.getValue());
        
        conflictManagerViewModel.currentLocalDecisionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getChangedDecision() != null) {
                    updateLocalDecisionFields(boxDiffType.getValue());
                }
            }
        });

        conflictManagerViewModel.currentRemoteDecisionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getChangedDecision() != null) {
                    updateRemoteDecisionFields(boxDiffType.getValue());
                }
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

    private void updateLocalDecisionFields(DiffUtilFX.DiffType type) {
        YStatementJustificationWrapper localDecision = conflictManagerViewModel.getCurrentLocalDecision().getChangedDecision();
        txtLocalContext.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalContext(), localDecision.getContext(), type));
        txtLocalFacing.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalFacing(), localDecision.getFacing(), type));
        txtLocalChosen.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalChosen(), localDecision.getChosen(), type));
        txtLocalNeglected.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalNeglected(), localDecision.getNeglected(), type));
        txtLocalAchieving.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalAchieving(), localDecision.getAchieving(), type));
        txtLocalAccepting.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalAccepting(), localDecision.getAccepting(), type));
    }

    private void updateRemoteDecisionFields(DiffUtilFX.DiffType type) {
        YStatementJustificationWrapper remoteDecision = conflictManagerViewModel.getCurrentRemoteDecision().getChangedDecision();
        txtRemoteContext.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalContext(), remoteDecision.getContext(), type));
        txtRemoteFacing.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalFacing(), remoteDecision.getFacing(), type));
        txtRemoteChosen.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalChosen(), remoteDecision.getChosen(), type));
        txtRemoteNeglected.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalNeglected(), remoteDecision.getNeglected(), type));
        txtRemoteAchieving.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalAchieving(), remoteDecision.getAchieving(), type));
        txtRemoteAccepting.getChildren().setAll(getDiffHighlightedTextNodes(conflictManagerViewModel.getOriginalAccepting(), remoteDecision.getAccepting(), type));
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
    }

    @FXML
    public void updateDiff() {
        updateLocalDecisionFields(boxDiffType.getValue());
        updateRemoteDecisionFields(boxDiffType.getValue());
    }

    @FXML
    public void mergeLocalNonConflicting() {
        conflictManagerViewModel.mergeLocalNonConflicting();
    }

    @FXML
    public void mergeRemoteNonConflicting() {
        conflictManagerViewModel.mergeRemoteNonConflicting();
    }

    @FXML
    public void mergeNonConflicting() {
        mergeLocalNonConflicting();
        mergeRemoteNonConflicting();
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
