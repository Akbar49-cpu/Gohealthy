package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.Artikel;
import model.RiwayatMakanan;
import util.AppData;


public class DashboardController implements Initializable {

    @FXML
    private Label namaMenu;
    
    @FXML
    private Label namaProfil;

    @FXML
    private AnchorPane cardArtikel1, cardArtikel2, cardArtikel3, cardArtikel4;
    
    @FXML
    private ProgressBar progressKalori;
    
    @FXML
    private ProgressIndicator indikatorKalori;
    
    @FXML
    private Button buttonTarget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
        loadArtikelData();
        updateKaloriTracking();
        setupButtonTarget();
    }
    
    private void setupButtonTarget() {
        buttonTarget.setOnAction(e -> handleTargetKalori());
    }
    
    @FXML
    private void handleTargetKalori() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Target Kalori");
        dialog.setHeaderText("Atur Target Kalori Harian");
        dialog.setContentText("Masukkan target kalori (dalam kkal):");
        
        if (AppData.currentUser != null && AppData.currentUser.getTargetKalori() != null) {
            dialog.getEditor().setText(String.valueOf(AppData.currentUser.getTargetKalori().intValue()));
        } else {
            dialog.getEditor().setText("2000");
        }
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(targetStr -> {
            try {
                double target = Double.parseDouble(targetStr);
                if (target > 0 && target <= 10000) {
                    if (AppData.currentUser != null) {
                        AppData.currentUser.setTargetKalori(target);
                        AppData.saveData();
                        updateKaloriTracking();
                    }
                } else {
                    showErrorDialog("Target kalori harus antara 1-10000 kkal");
                }
            } catch (NumberFormatException ex) {
                showErrorDialog("Format angka tidak valid");
            }
        });
    }
    
    private void updateKaloriTracking() {
        if (AppData.currentUser == null) {
            progressKalori.setProgress(0);
            indikatorKalori.setProgress(0);
            return;
        }
        
        double targetKalori = AppData.currentUser.getTargetKalori() != null ? 
            AppData.currentUser.getTargetKalori() : 2000.0;
        
        double kaloriHariIni = getKaloriHariIni();
        
        double progress = Math.min(kaloriHariIni / targetKalori, 1.0);
        
        progressKalori.setProgress(progress);
        indikatorKalori.setProgress(progress);
        
        buttonTarget.setText(String.format("Target: %.0f kkal", targetKalori));
    }
    
    private double getKaloriHariIni() {
        if (AppData.currentUser == null) return 0.0;
        
        LocalDate today = LocalDate.now();
        List<RiwayatMakanan> riwayatMakanan = AppData.getCurrentUserRiwayatMakanan();
        
        return riwayatMakanan.stream()
            .filter(riwayat -> riwayat.getTanggal().equals(today))
            .mapToDouble(RiwayatMakanan::getKalori)
            .sum();
    }
    
    private void showErrorDialog(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUserData() {
        try {
            if (AppData.currentUser != null) {
                String nama = AppData.currentUser.getNama();
                namaMenu.setText(nama + "!");
                namaProfil.setText(nama);
            } else if (!AppData.userList.isEmpty()) {
                String nama = AppData.userList.get(0).getNama();
                namaMenu.setText(nama + "!");
                namaProfil.setText(nama);
            }
        } catch (Exception e) {
            e.printStackTrace();
            namaMenu.setText("Pengguna!");
            namaProfil.setText("Pengguna");
        }
    }

    private void loadArtikelData() {
        try {
            if (!AppData.artikelList.isEmpty()) {
                System.out.println("Artikel data loaded: " + AppData.artikelList.size() + " artikel tersedia");
                
                for (int i = 0; i < Math.min(4, AppData.artikelList.size()); i++) {
                    System.out.println("Artikel " + (i+1) + ": " + AppData.artikelList.get(i).getJudul());
                }
                
                updateArtikelCards();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading artikel data: " + e.getMessage());
        }
    }
    
    private void updateArtikelCards() {
        if (AppData.artikelList.size() >= 4) {
            System.out.println("Cards akan menampilkan artikel dari ArrayList:");
            for (int i = 0; i < 4; i++) {
                System.out.println("Card " + (i+1) + " -> " + AppData.artikelList.get(i).getJudul());
            }
        }
    }
    
    public void refreshKaloriTracking() {
        updateKaloriTracking();
    }

    public static Artikel getArtikelById(String id) {
        for (Artikel artikel : AppData.artikelList) {
            if (artikel.getId().equals(id)) {
                return artikel;
            }
        }
        return null;
    }
    
    public static java.util.List<Artikel> getAllArtikel() {
        return AppData.artikelList;
    }

    @FXML public void handleCatatMakan(MouseEvent event) {
    try {
       
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/catatMakanan.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        System.out.println("Berpindah ke scene Catat Makanan");

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    public void handlePantauBerat(MouseEvent event) {
        try {
       
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PantauBB.fxml"));
        Parent root = loader.load();

       
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        System.out.println("Berpindah ke scene Catat Makanan");

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
    public void handleKonsultasi(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            System.out.println("Berpindah ke scene Search untuk konsultasi diet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBMI(MouseEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KalkulatorBMI.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        System.out.println("Berpindah ke scene Catat Makanan");

    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @FXML
    public void handleProfil(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profilPengguna.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Berpindah ke scene Profil Pengguna");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCardArtikel1Click(MouseEvent event) {
        pindahHalaman("/view/artikel1.fxml");
    }

    @FXML
    private void handleCardArtikel2Click(MouseEvent event) {
        pindahHalaman("/view/artikel2.fxml");
    }

    @FXML
    private void handleCardArtikel3Click(MouseEvent event) {
        pindahHalaman("/view/artikel3.fxml");
    }

    @FXML
    private void handleCardArtikel4Click(MouseEvent event) {
        pindahHalaman("/view/artikel4.fxml");
    }


    private void pindahHalaman(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) cardArtikel1.getScene().getWindow(); 
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            }
    }
}
