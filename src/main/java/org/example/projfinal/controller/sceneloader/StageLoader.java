package org.example.projfinal.controller.sceneloader;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class StageLoader {

    private final SceneLoader sceneLoader;
    private final Stage stage;

    public StageLoader(Stage stage) {
        this.stage = stage;
        this.sceneLoader =  new SceneLoader(stage);
    }

    public StageLoader() {
        this.stage = new Stage();
        this.sceneLoader =  new SceneLoader(stage);
    }

    public void switchScene(Parent root) {
        sceneLoader.switchScene(root);
    }

}
