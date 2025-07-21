package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.User;
import model.DataBeratBadan;
import util.AppData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class profilPenggunaController {
    
    @FXML
    private TextField namaField;
    
    @FXML
    private TextField bbField;
    
    @FXML
    private TextField tbField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField pwField;
    
    @FXML
    private Button btnKembali;
    
    @FXML
    private Button btnLogOut;
    
    @FXML
    private Button btnHapusAkun;
    
    private User currentUser;
    
    @FXML
    private void initialize() {
        loadUserData();
        
        setupEventHandlers();
    }
    
    private void loadUserData() {
        if (AppData.currentUser != null) {
            currentUser = AppData.currentUser;
            
            System.out.println("Loading profile for user: " + currentUser.getNama() + " (" + currentUser.getEmail() + ")");
            
            namaField.setText(currentUser.getNama());
            emailField.setText(currentUser.getEmail());
            pwField.setText(currentUser.getPassword());
            
            loadLatestWeight();
            
            tbField.setText(currentUser.getTinggiBadan() != null ? 
                String.valueOf(currentUser.getTinggiBadan()) : "");
        } else {
            System.err.println("Error: No current user found!");
            if (!AppData.userList.isEmpty()) {
                System.out.println("Fallback: Using first user from userList");
                currentUser = AppData.userList.get(0);
                namaField.setText(currentUser.getNama());
                emailField.setText(currentUser.getEmail());
                pwField.setText(currentUser.getPassword());
                loadLatestWeight();
                tbField.setText(currentUser.getTinggiBadan() != null ? 
                    String.valueOf(currentUser.getTinggiBadan()) : "");
            }
        }
    }
    
    private void loadLatestWeight() {
        try {
            List<model.DataBeratBadan> currentUserWeightData = AppData.getCurrentUserDataBeratBadan();
            if (!currentUserWeightData.isEmpty()) {
                model.DataBeratBadan latestWeight = currentUserWeightData.stream()
                    .max((a, b) -> a.getTanggalDate().compareTo(b.getTanggalDate()))
                    .orElse(null);
                
                if (latestWeight != null) {
                    bbField.setText(String.valueOf(latestWeight.getBerat()));
                    return;
                }
            }
            
            if (currentUser.getBeratBadan() != null && currentUser.getBeratBadan() > 0) {
                bbField.setText(String.valueOf(currentUser.getBeratBadan()));
            } else {
                bbField.setText("");
            }
        } catch (Exception e) {
            bbField.setText("");
        }
    }
    
    private void setupEventHandlers() {
        namaField.setOnMouseClicked(e -> namaField.setEditable(true));
        bbField.setOnMouseClicked(e -> bbField.setEditable(true));
        tbField.setOnMouseClicked(e -> tbField.setEditable(true));
        emailField.setOnMouseClicked(e -> emailField.setEditable(true));
        pwField.setOnMouseClicked(e -> pwField.setEditable(true));
        
        namaField.setEditable(false);
        bbField.setEditable(false);
        tbField.setEditable(true);
        emailField.setEditable(false);
        pwField.setEditable(false);
        
        namaField.setStyle("-fx-background-color: #f0f0f0;");
        bbField.setStyle("-fx-background-color: #f0f0f0;");
        emailField.setStyle("-fx-background-color: #f0f0f0;");
        pwField.setStyle("-fx-background-color: #f0f0f0;");
    }
    
    @FXML
    private void handlePerbarui(ActionEvent event) {
        try {
            if (namaField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty() || 
                pwField.getText().trim().isEmpty()) {
                showAlert("Error", "Nama, email, dan password tidak boleh kosong!");
                return;
            }
            
            currentUser.setNama(namaField.getText().trim());
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setPassword(pwField.getText().trim());
            
            AppData.currentUser = currentUser;
            
            if (!tbField.getText().trim().isEmpty()) {
                try {
                    double tinggiBadan = Double.parseDouble(tbField.getText().trim());
                    currentUser.setTinggiBadan(tinggiBadan);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Tinggi badan harus berupa angka!");
                    return;
                }
            }
            
            if (!bbField.getText().trim().isEmpty()) {
                try {
                    double beratBadan = Double.parseDouble(bbField.getText().trim());
                    double beratBadanLama = currentUser.getBeratBadan() != null ? currentUser.getBeratBadan() : 0.0;
                    
                    currentUser.setBeratBadan(beratBadan);
                    
                    if (beratBadan != beratBadanLama) {
                        DataBeratBadan newData = new DataBeratBadan(LocalDate.now(), beratBadan);
                        List<DataBeratBadan> userDataBeratBadan = AppData.getCurrentUserDataBeratBadan();
                        
                        boolean existsToday = false;
                        for (int i = 0; i < userDataBeratBadan.size(); i++) {
                            DataBeratBadan existing = userDataBeratBadan.get(i);
                            if (existing.getTanggalDate().equals(LocalDate.now())) {
                                userDataBeratBadan.set(i, newData);
                                existsToday = true;
                                break;
                            }
                        }
                        
                        if (!existsToday) {
                            userDataBeratBadan.add(newData);
                        }
                        
                        System.out.println("Data berat badan berhasil ditambahkan ke PantauBB: " + beratBadan + " kg pada " + LocalDate.now());
                    }
                    
                } catch (NumberFormatException e) {
                    showAlert("Error", "Berat badan harus berupa angka!");
                    return;
                }
            }
            
            saveUserData();
            
            namaField.setEditable(false);
            bbField.setEditable(false);
            emailField.setEditable(false);
            pwField.setEditable(false);
            
            namaField.setStyle("-fx-background-color: #f0f0f0;");
            bbField.setStyle("-fx-background-color: #f0f0f0;");
            emailField.setStyle("-fx-background-color: #f0f0f0;");
            pwField.setStyle("-fx-background-color: #f0f0f0;");
            
            showAlert("Success", "Profil berhasil diperbarui!");
            
        } catch (Exception e) {
            showAlert("Error", "Gagal memperbarui profil: " + e.getMessage());
        }
    }
    
    private void saveUserData() {
        try {
            AppData.saveToXml();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menyimpan data ke XML", e);
        }
    }
    
    @FXML
    private void handleKembali(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke menu utama: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogOut(ActionEvent event) {
        try {
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Logout");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Apakah Anda yakin ingin keluar dari akun?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppData.currentUser = null;
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
                showAlert("Logout", "Anda telah berhasil keluar dari akun.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal logout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleHapusAkun(ActionEvent event) {
        try {
            Alert warningAlert = new Alert(AlertType.WARNING);
            warningAlert.setTitle("Peringatan Hapus Akun");
            warningAlert.setHeaderText("PERINGATAN!");
            warningAlert.setContentText("Tindakan ini akan menghapus SEMUA data akun Anda secara permanen, " +
                                      "termasuk:\n" +
                                      "• Data profil\n" +
                                      "• Riwayat makanan\n" +
                                      "• Data pantau berat badan\n" +
                                      "• Semua data lainnya\n\n" +
                                      "Tindakan ini TIDAK DAPAT DIBATALKAN!\n\n" +
                                      "Apakah Anda yakin ingin melanjutkan?");
            
            ButtonType lanjutkanButton = new ButtonType("Lanjutkan");
            ButtonType kembaliButton = new ButtonType("Kembali");
            warningAlert.getButtonTypes().setAll(lanjutkanButton, kembaliButton);
            
            Optional<ButtonType> result = warningAlert.showAndWait();
            if (result.isPresent() && result.get() == lanjutkanButton) {
                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();
                    
                    AppData.userList.removeIf(user -> user.getEmail().equals(userEmail));
                    
                    AppData.userRiwayatMakananMap.remove(userEmail);
                    AppData.userDataBeratBadanMap.remove(userEmail);
                    
                    AppData.currentUser = null;
                    
                    AppData.saveToXml();
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Akun Dihapus");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Akun Anda telah berhasil dihapus dari sistem.");
                    successAlert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menghapus akun: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
