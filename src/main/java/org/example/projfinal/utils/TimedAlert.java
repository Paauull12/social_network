package org.example.projfinal.utils;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TimedAlert {

    public static void showAlert(String message) {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initStyle(StageStyle.UNDECORATED);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");

        StackPane root = new StackPane(messageLabel);
        root.setStyle("-fx-padding: 10; -fx-background-color: transparent;");

        Scene scene = new Scene(root);
        alertStage.setScene(scene);
        alertStage.setAlwaysOnTop(true);
        alertStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> alertStage.close());
        delay.play();
    }
}
