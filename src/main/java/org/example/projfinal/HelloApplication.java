package org.example.projfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projfinal.controller.AuthWindow;
import org.example.projfinal.controller.sceneloader.StageLoader;
import org.example.projfinal.repository.RepositoryConfig;
import org.example.projfinal.service.Network;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Network network = new Network(RepositoryConfig.DB_BASED.getUserRepo(), RepositoryConfig.DB_BASED.getFriendRepo(), RepositoryConfig.DB_BASED.getGroupRepo(), RepositoryConfig.DB_BASED.getMessageRepo());
        StageLoader stageLoader = new StageLoader(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projfinal/authwindow.fxml"));
        Parent root = fxmlLoader.load();
        AuthWindow controller = fxmlLoader.getController();
        controller.setNetwork(network);
        stageLoader.switchScene(root);
    }

    public static void main(String[] args) {
        launch();
    }
}