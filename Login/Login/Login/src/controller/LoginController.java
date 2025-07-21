package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import util.AppData;

import javax.swing.JOptionPane;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private VBox rootPane;
    @FXML private TextField passwordTextField;
    @FXML private ToggleButton togglePasswordButton;
    @FXML private ImageView eyeIcon;

    private final Image eyeOpen = new Image(getClass().getResourceAsStream("/assets/eye-open.png"));
    private final Image eyeClosed = new Image(getClass().getResourceAsStream("/assets/eye-closed.png"));

    @FXML
    void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User foundUser = null;
        
        for (User user : AppData.userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser != null) {
            AppData.currentUser = foundUser;
            AppData.initializeUserData(foundUser.getEmail());
            loadScene("/view/menu.fxml");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void handleRegister() {
        loadScene("/view/Register.fxml");
    }

    @FXML
    void handleForgotPassword() {
        loadScene("/view/ForgotPassword.fxml");
    }
    
    private void loadScene(String fxmlPath) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            
            Parent newSceneRoot = FXMLLoader.load(getClass().getResource(fxmlPath));
            if (newSceneRoot == null) {
                System.err.println("Error: File FXML tidak ditemukan di path: " + fxmlPath);
                return;
            }

            Scene newScene = new Scene(newSceneRoot, 1920, 1080);
            stage.setScene(newScene);
            stage.setMinWidth(1024);
            stage.setMinHeight(768);
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            System.err.println("Gagal memuat halaman: " + fxmlPath);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memuat dashboard.\nSilakan cek konsol untuk detail.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void initialize() {
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        passwordTextField.visibleProperty().bind(togglePasswordButton.selectedProperty());
        passwordField.visibleProperty().bind(togglePasswordButton.selectedProperty().not());

        passwordTextField.managedProperty().bind(togglePasswordButton.selectedProperty());
        passwordField.managedProperty().bind(togglePasswordButton.selectedProperty().not());

        togglePasswordButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                eyeIcon.setImage(eyeOpen);
            } else {
                eyeIcon.setImage(eyeClosed);
            }
        });
    }
}