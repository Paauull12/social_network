package org.example.projfinal.controller.sceneloader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneLoader {
    private final Stage stage;

    public SceneLoader(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(Parent root) {
        try {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}