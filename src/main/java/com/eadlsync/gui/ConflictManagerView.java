package com.eadlsync.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.eadlsync.model.diff.DiffManager;

/**
 * Created by tobias on 18/06/2017.
 */
public class ConflictManagerView {

    private ConflictManagerController conflictManagerController;

    public ConflictManagerView(DiffManager diffManager) {
        conflictManagerController = new ConflictManagerController(diffManager);
    }

    public boolean showDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resolve-conflicts-view.fxml"));
        try {
            loader.setController(conflictManagerController);
            DialogPane root = loader.load();
            root.getStylesheets().add(getClass().getResource("/resolve-conflicts-view.css").toExternalForm());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Resolve conflicts");
            alert.setResizable(true);
            alert.setDialogPane(root);
            alert.initStyle(StageStyle.UTILITY);
            alert.initModality(Modality.WINDOW_MODAL);

            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(event -> window.hide());

            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return conflictManagerController.isFinishedSuccessfully();
    }


}
