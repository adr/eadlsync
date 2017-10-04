package io.github.adr.eadlsync.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import io.github.adr.eadlsync.gui.DiffUtilFX.DiffType;
import io.github.adr.eadlsync.model.decision.YStatementJustificationComparisionObject;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import io.github.adr.eadlsync.model.diff.DiffViewModel;

import static io.github.adr.eadlsync.gui.DiffUtilFX.getDiffHighlightedTextNodes;

/**
 * Controller for the diff viewer. This class specifies what should be displayed and how to react on user action.
 */
public class DiffController {

    private DiffViewModel diffViewModel;

    @FXML
    private Label lblTitle;
    @FXML
    private ComboBox<DiffType> boxDiffType;
    @FXML
    private ListView listDecisions;
    @FXML
    private Button btnFinish;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    private TextFlow txtLocalContext;
    @FXML
    private TextFlow txtLocalFacing;
    @FXML
    private TextFlow txtLocalChosen;
    @FXML
    private TextFlow txtLocalNeglected;
    @FXML
    private TextFlow txtLocalAchieving;
    @FXML
    private TextFlow txtLocalAccepting;
    @FXML
    private TextFlow txtRemoteContext;
    @FXML
    private TextFlow txtRemoteFacing;
    @FXML
    private TextFlow txtRemoteChosen;
    @FXML
    private TextFlow txtRemoteNeglected;
    @FXML
    private TextFlow txtRemoteAchieving;
    @FXML
    private TextFlow txtRemoteAccepting;


    public DiffController(List<YStatementJustificationComparisionObject> diff) {
        this.diffViewModel = new DiffViewModel(diff);
    }

    @FXML
    private void initialize() {
        lblTitle.textProperty().bind(diffViewModel.currentIdProperty());
        btnPrevious.disableProperty().bind(diffViewModel.canGoToPreviousDecisionProperty().not());
        btnNext.disableProperty().bind(diffViewModel.canGoToNextDecisionProperty().not());

        for (DiffType type : DiffType.values()) {
            boxDiffType.getItems().add(type);
        }
        boxDiffType.setValue(DiffType.WORDS);

        updateLocalDecisionFields();
        updateRemoteDecisionFields(boxDiffType.getValue());

        diffViewModel.currentLocalDecisionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateLocalDecisionFields();
            }
        });

        diffViewModel.currentRemoteDecisionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateRemoteDecisionFields(boxDiffType.getValue());
            }
        });

        listDecisions.itemsProperty().bind(diffViewModel.idsProperty());
        listDecisions.getSelectionModel().selectFirst();

        listDecisions.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                diffViewModel.currentIndexProperty().set(newValue.intValue());
            }
        });

        diffViewModel.currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                listDecisions.getSelectionModel().select(newValue.intValue());
            }
        });

    }

    private void updateLocalDecisionFields() {
        YStatementJustificationWrapper localDecision = diffViewModel.getCurrentLocalDecision();
        txtLocalContext.getChildren().setAll(new Text(localDecision.getContext()));
        txtLocalFacing.getChildren().setAll(new Text(localDecision.getFacing()));
        txtLocalChosen.getChildren().setAll(new Text(localDecision.getChosen()));
        txtLocalNeglected.getChildren().setAll(new Text(localDecision.getNeglected()));
        txtLocalAchieving.getChildren().setAll(new Text(localDecision.getAchieving()));
        txtLocalAccepting.getChildren().setAll(new Text(localDecision.getAccepting()));
    }

    private void updateRemoteDecisionFields(DiffType type) {
        YStatementJustificationWrapper localDecision = diffViewModel.getCurrentLocalDecision();
        YStatementJustificationWrapper remoteDecision = diffViewModel.getCurrentRemoteDecision();
        txtRemoteContext.getChildren().setAll(getDiffHighlightedTextNodes(localDecision.getContext(), remoteDecision.getContext(), type));
        txtRemoteFacing.getChildren().setAll(getDiffHighlightedTextNodes(localDecision.getFacing(), remoteDecision.getFacing(), type));
        txtRemoteChosen.getChildren().setAll(getDiffHighlightedTextNodes(localDecision.getChosen(), remoteDecision.getChosen(), type));
        txtRemoteNeglected.getChildren().setAll(getDiffHighlightedTextNodes(localDecision.getNeglected(), remoteDecision.getNeglected(), type));
        txtRemoteAchieving.getChildren().setAll(getDiffHighlightedTextNodes(localDecision.getAchieving(), remoteDecision.getAchieving(), type));
        txtRemoteAccepting.getChildren().setAll(getDiffHighlightedTextNodes(localDecision.getAccepting(), remoteDecision.getAccepting(), type));
    }

    @FXML
    public void updateDiff() {
        updateLocalDecisionFields();
        updateRemoteDecisionFields(boxDiffType.getValue());
    }

    @FXML
    public void goBack() {
        diffViewModel.goToPreviousDecision();
    }

    @FXML
    public void goNext() {
        diffViewModel.goToNextDecision();
    }

    @FXML
    public void close() {
        Stage stage = (Stage) btnFinish.getScene().getWindow();
        stage.close();
    }

}
