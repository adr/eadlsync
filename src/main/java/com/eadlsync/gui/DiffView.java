package com.eadlsync.gui;

import com.eadlsync.model.decision.YStatementJustificationComparisionObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;

/**
 * Class to display a diff of multiple {@link YStatementJustificationComparisionObject} in a JavaFX dialog.
 */
public class DiffView {

    private DiffController diffController;

    public DiffView(List<YStatementJustificationComparisionObject> diff) {
        diffController = new DiffController(diff);
    }

    public void showDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/diff/diff-view.fxml"));
        try {
            loader.setController(diffController);
            DialogPane root = loader.load();
            root.getStylesheets().add(getClass().getResource("/gui/diff/diff-view.css").toExternalForm());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Diff Viewer");
            alert.setResizable(true);
            alert.setDialogPane(root);
            alert.initModality(Modality.WINDOW_MODAL);

            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(event -> window.hide());

            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
