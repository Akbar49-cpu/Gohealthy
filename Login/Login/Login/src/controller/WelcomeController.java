package controller;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeController {

    @FXML
    private VBox rootPane;

    @FXML
    public void initialize() {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            try {
                Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(new Scene(loginRoot));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }
}