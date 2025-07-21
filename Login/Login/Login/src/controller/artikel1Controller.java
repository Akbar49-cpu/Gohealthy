package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class artikel1Controller {
     @FXML
    private Button btnKembali;

    @FXML
    public void initialize() {
        btnKembali.setOnAction(e -> {
            try {
          
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) btnKembali.getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
               
            }
        });
    }
    
}
