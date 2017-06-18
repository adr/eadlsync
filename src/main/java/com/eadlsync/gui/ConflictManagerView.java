package com.eadlsync.gui;

import com.eadlsync.model.diff.DiffManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;

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

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Resolve conflicts");
            alert.setResizable(true);
            alert.setDialogPane(root);
            alert.initStyle(StageStyle.UTILITY);
            alert.initModality(Modality.NONE);

            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(event -> window.hide());

            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return conflictManagerController.isFinishedSuccessfully();
    }


}
