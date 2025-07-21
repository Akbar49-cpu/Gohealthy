package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.RiwayatMakanan;
import util.AppData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CatatMakananController {
    
    @FXML
    private DatePicker tanggalPicker;
    
    @FXML
    private RadioButton radioPagi, radioSiang, radioMalam;
    
    @FXML
    private TextField txtMakanan1, txtMakanan2, txtMakanan3, txtMakanan4, txtMakanan5;
    
    @FXML
    private TextField txtBerat1, txtBerat2, txtBerat3, txtBerat4, txtBerat5;
    
    @FXML
    private TextField txtKalori1, txtKalori2, txtKalori3, txtKalori4, txtKalori5;
    
    @FXML
    private Label lblTotalKalori;
    
    @FXML
    private TableView<RiwayatMakanan> riwayatKalori;
    
    @FXML
    private TableColumn<RiwayatMakanan, String> columnTanggal;
    
    @FXML
    private TableColumn<RiwayatMakanan, String> columnWaktuMakan;
    
    @FXML
    private TableColumn<RiwayatMakanan, String> columnMakanan;
    
    @FXML
    private TableColumn<RiwayatMakanan, String> columnBeratGram;
    
    @FXML
    private TableColumn<RiwayatMakanan, String> columnKalori;
    
    @FXML
    private LineChart<String, Number> grafikTotalKalori;
    
    @FXML
    private ComboBox<String> filterPeriode;
    
    @FXML
    private Label kaloriMax;
    
    @FXML
    private Label kaloriMin;
    
    @FXML
    private javafx.scene.control.Button btnHapus;
    
    @FXML
    private javafx.scene.control.Button btnPerbarui;
    
    private ToggleGroup waktuMakanGroup;
    private Map<String, Double> dataKalori;
    
    @FXML
    private void initialize() {
        waktuMakanGroup = new ToggleGroup();
        radioPagi.setToggleGroup(waktuMakanGroup);
        radioSiang.setToggleGroup(waktuMakanGroup);
        radioMalam.setToggleGroup(waktuMakanGroup);
        
        radioPagi.setSelected(true);
        
        columnTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalFormatted"));
        columnWaktuMakan.setCellValueFactory(new PropertyValueFactory<>("waktuMakan"));
        columnMakanan.setCellValueFactory(new PropertyValueFactory<>("makanan"));
        columnBeratGram.setCellValueFactory(new PropertyValueFactory<>("beratGramFormatted"));
        columnKalori.setCellValueFactory(new PropertyValueFactory<>("kaloriFormatted"));
        
        ArrayList<String> filterOptions = new ArrayList<>();
        filterOptions.add("Semua Data");
        filterOptions.add("Seminggu");
        filterOptions.add("Sebulan");
        filterOptions.add("3 Bulan");
        filterOptions.add("6 Bulan");
        filterOptions.add("Setahun");
        filterPeriode.getItems().setAll(filterOptions);
        filterPeriode.setValue("Semua Data");
        filterPeriode.setOnAction(e -> updateGrafik());
        
        initializeDataKalori();
        
        txtKalori1.setEditable(false);
        txtKalori2.setEditable(false);
        txtKalori3.setEditable(false);
        txtKalori4.setEditable(false);
        txtKalori5.setEditable(false);
        
        tanggalPicker.setValue(LocalDate.now());
        
        initializeDemoData();
        
        loadRiwayatData();
        updateGrafik();
    }
    
    private void initializeDataKalori() {
        dataKalori = new HashMap<>();
        dataKalori.put("nasi putih", 175.0);
        dataKalori.put("ayam goreng", 260.0);
        dataKalori.put("telur rebus", 155.0);
        dataKalori.put("sayur bayam", 35.0);
        dataKalori.put("tempe goreng", 200.0);
        dataKalori.put("tahu kukus", 80.0);
        dataKalori.put("ikan bakar", 160.0);
        dataKalori.put("roti tawar", 250.0);
        dataKalori.put("mie instan", 380.0);
        dataKalori.put("kentang rebus", 87.0);
    }
    
    private void initializeDemoData() {
        List<RiwayatMakanan> currentUserData = AppData.getCurrentUserRiwayatMakanan();
        if (currentUserData.isEmpty() && AppData.currentUser != null && 
            AppData.isFirstTimeUser(AppData.currentUser.getEmail())) {
            
            currentUserData.add(new RiwayatMakanan("nasi putih", 150, 262.5, LocalDate.now(), "Pagi"));
            currentUserData.add(new RiwayatMakanan("telur rebus", 100, 155, LocalDate.now(), "Pagi"));
            
            currentUserData.add(new RiwayatMakanan("ayam goreng", 200, 520, LocalDate.now().minusDays(1), "Siang"));
            currentUserData.add(new RiwayatMakanan("sayur bayam", 150, 52.5, LocalDate.now().minusDays(1), "Malam"));
            
            currentUserData.add(new RiwayatMakanan("tempe goreng", 100, 200, LocalDate.now().minusDays(2), "Pagi"));
            currentUserData.add(new RiwayatMakanan("mie instan", 100, 380, LocalDate.now().minusDays(2), "Siang"));
            
            currentUserData.add(new RiwayatMakanan("ikan bakar", 150, 240, LocalDate.now().minusDays(5), "Siang"));
            currentUserData.add(new RiwayatMakanan("roti tawar", 100, 250, LocalDate.now().minusDays(5), "Pagi"));
            
            currentUserData.add(new RiwayatMakanan("tahu kukus", 200, 160, LocalDate.now().minusDays(10), "Malam"));
            currentUserData.add(new RiwayatMakanan("kentang rebus", 150, 130.5, LocalDate.now().minusDays(10), "Siang"));
            
            currentUserData.add(new RiwayatMakanan("nasi putih", 100, 175, LocalDate.now().minusDays(20), "Pagi"));
            currentUserData.add(new RiwayatMakanan("ayam goreng", 100, 260, LocalDate.now().minusDays(20), "Siang"));
            
            currentUserData.add(new RiwayatMakanan("mie instan", 150, 570, LocalDate.now().minusDays(40), "Malam"));
            currentUserData.add(new RiwayatMakanan("telur rebus", 150, 232.5, LocalDate.now().minusDays(40), "Pagi"));
            
            currentUserData.add(new RiwayatMakanan("tempe goreng", 120, 240, LocalDate.now().minusDays(100), "Siang"));
            currentUserData.add(new RiwayatMakanan("sayur bayam", 100, 35, LocalDate.now().minusDays(100), "Malam"));
        }
    }
    
    @FXML
    private void hitungKalori() {
        double totalKalori = 0.0;
        
        TextField[] txtMakanans = {txtMakanan1, txtMakanan2, txtMakanan3, txtMakanan4, txtMakanan5};
        TextField[] txtBerats = {txtBerat1, txtBerat2, txtBerat3, txtBerat4, txtBerat5};
        TextField[] txtKaloris = {txtKalori1, txtKalori2, txtKalori3, txtKalori4, txtKalori5};
        
        for (int i = 0; i < txtMakanans.length; i++) {
            String makanan = txtMakanans[i].getText().trim().toLowerCase();
            String beratStr = txtBerats[i].getText().trim();
            
            if (!makanan.isEmpty() && !beratStr.isEmpty()) {
                try {
                    double berat = Double.parseDouble(beratStr);
                    double kaloriPer100g = dataKalori.getOrDefault(makanan, 100.0);
                    double kaloriTotal = (kaloriPer100g * berat) / 100.0;
                    
                    txtKaloris[i].setText(String.format("%.2f", kaloriTotal));
                    txtKaloris[i].setEditable(false);
                    totalKalori += kaloriTotal;
                } catch (NumberFormatException e) {
                    txtKaloris[i].setText("0.00");
                    txtKaloris[i].setEditable(false);
                }
            } else {
                txtKaloris[i].setText("0.00");
                txtKaloris[i].setEditable(false);
            }
        }
        
        lblTotalKalori.setText(String.format("%.2f kkal", totalKalori));
    }
    
    @FXML
    private void simpanData() {
        LocalDate tanggal = tanggalPicker.getValue();
        if (tanggal == null) {
            showAlert("Error", "Harap pilih tanggal terlebih dahulu!");
            return;
        }
        
        String waktuMakan = "";
        if (radioPagi.isSelected()) waktuMakan = "Pagi";
        else if (radioSiang.isSelected()) waktuMakan = "Siang";
        else if (radioMalam.isSelected()) waktuMakan = "Malam";
        
        TextField[] txtMakanans = {txtMakanan1, txtMakanan2, txtMakanan3, txtMakanan4, txtMakanan5};
        TextField[] txtBerats = {txtBerat1, txtBerat2, txtBerat3, txtBerat4, txtBerat5};
        TextField[] txtKaloris = {txtKalori1, txtKalori2, txtKalori3, txtKalori4, txtKalori5};
        
        boolean adaData = false;
        
        for (int i = 0; i < txtMakanans.length; i++) {
            String makanan = txtMakanans[i].getText().trim();
            String beratStr = txtBerats[i].getText().trim();
            String kaloriStr = txtKaloris[i].getText().trim();
            
            if (!makanan.isEmpty() && !beratStr.isEmpty() && !kaloriStr.isEmpty()) {
                try {
                    double berat = Double.parseDouble(beratStr);
                    double kalori = Double.parseDouble(kaloriStr);
                    
                    RiwayatMakanan riwayat = new RiwayatMakanan(makanan, berat, kalori, tanggal, waktuMakan);
                    AppData.getCurrentUserRiwayatMakanan().add(riwayat);
                    adaData = true;
                    
                } catch (NumberFormatException e) {
                    showAlert("Error", "Format angka tidak valid pada baris " + (i + 1));
                    return;
                }
            }
        }
        
        if (adaData) {
            AppData.saveToXml();
            showAlert("Success", "Data berhasil disimpan!");
            clearForm();
            loadRiwayatData();
            updateGrafik();
        } else {
            showAlert("Warning", "Tidak ada data yang disimpan!");
        }
    }
    
    private void clearForm() {
        txtMakanan1.clear(); txtMakanan2.clear(); txtMakanan3.clear(); txtMakanan4.clear(); txtMakanan5.clear();
        txtBerat1.clear(); txtBerat2.clear(); txtBerat3.clear(); txtBerat4.clear(); txtBerat5.clear();
        txtKalori1.clear(); txtKalori2.clear(); txtKalori3.clear(); txtKalori4.clear(); txtKalori5.clear();
        lblTotalKalori.setText("0.00");
    }
    
    private void loadRiwayatData() {
        ArrayList<RiwayatMakanan> dataList = new ArrayList<>(AppData.getCurrentUserRiwayatMakanan());
        riwayatKalori.getItems().setAll(dataList);
    }
    
    private void updateGrafik() {
        grafikTotalKalori.getData().clear();
        
        List<RiwayatMakanan> filteredData = getFilteredData();
        
        Map<LocalDate, Double> totalKaloriPerHari = filteredData.stream()
            .collect(Collectors.groupingBy(
                RiwayatMakanan::getTanggal,
                Collectors.summingDouble(RiwayatMakanan::getKalori)
            ));
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Kalori per Hari");
        
        totalKaloriPerHari.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String tanggalStr = entry.getKey().toString();
                series.getData().add(new XYChart.Data<>(tanggalStr, entry.getValue()));
            });
        
        grafikTotalKalori.getData().add(series);
        
        updateStatistik(totalKaloriPerHari);
    }
    
    private List<RiwayatMakanan> getFilteredData() {
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        
        String selectedPeriod = filterPeriode.getValue();
        if (selectedPeriod == null) selectedPeriod = "Semua Data";
        
        switch (selectedPeriod) {
            case "Seminggu":
                startDate = today.minusDays(7);
                break;
            case "Sebulan":
                startDate = today.minusDays(30);
                break;
            case "3 Bulan":
                startDate = today.minusDays(90);
                break;
            case "6 Bulan":
                startDate = today.minusDays(180);
                break;
            case "Setahun":
                startDate = today.minusDays(365);
                break;
            case "Semua Data":
            default:
                return new ArrayList<>(AppData.getCurrentUserRiwayatMakanan());
        }
        
        return AppData.getCurrentUserRiwayatMakanan().stream()
            .filter(riwayat -> {
                LocalDate tanggalRiwayat = riwayat.getTanggal();
                return (tanggalRiwayat.isEqual(startDate) || tanggalRiwayat.isAfter(startDate)) 
                    && (tanggalRiwayat.isBefore(today) || tanggalRiwayat.isEqual(today));
            })
            .collect(Collectors.toList());
    }
    
    private void updateStatistik(Map<LocalDate, Double> totalKaloriPerHari) {
        if (totalKaloriPerHari.isEmpty()) {
            kaloriMax.setText("0.00 kkal");
            kaloriMin.setText("0.00 kkal");
            return;
        }
        
        double maxKalori = totalKaloriPerHari.values().stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(0.0);
            
        double minKalori = totalKaloriPerHari.values().stream()
            .mapToDouble(Double::doubleValue)
            .min()
            .orElse(0.0);
        
        kaloriMax.setText(String.format("%.2f kkal", maxKalori));
        kaloriMin.setText(String.format("%.2f kkal", minKalori));
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void kembaliKeDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            if (controller != null) {
                controller.refreshKaloriTracking();
            }
            
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
    private void handleHapus(ActionEvent event) {
        RiwayatMakanan selectedItem = riwayatKalori.getSelectionModel().getSelectedItem();
        
        if (selectedItem == null) {
            showAlert("Peringatan", "Silakan pilih data yang ingin dihapus terlebih dahulu!");
            return;
        }
        
        javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus data makanan \"" + selectedItem.getMakanan() + "\"?");
        
        java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            try {
                List<RiwayatMakanan> userRiwayatMakanan = AppData.getCurrentUserRiwayatMakanan();
                userRiwayatMakanan.removeIf(rm -> 
                    rm.getMakanan().equals(selectedItem.getMakanan()) &&
                    rm.getTanggal().equals(selectedItem.getTanggal()) &&
                    rm.getWaktuMakan().equals(selectedItem.getWaktuMakan()) &&
                    rm.getBeratGram() == selectedItem.getBeratGram()
                );
                
                AppData.saveToXml();
                
                loadRiwayatData();
                updateGrafik();
                
                showAlert("Berhasil", "Data makanan berhasil dihapus!");
                
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Gagal menghapus data: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handlePerbarui(ActionEvent event) {
        RiwayatMakanan selectedItem = riwayatKalori.getSelectionModel().getSelectedItem();
        
        if (selectedItem == null) {
            showAlert("Peringatan", "Silakan pilih data yang ingin diperbarui terlebih dahulu!");
            return;
        }
        
        try {
            javafx.scene.control.Dialog<RiwayatMakanan> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Perbarui Data Makanan");
            dialog.setHeaderText("Edit data makanan yang dipilih");
            
            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
            
            javafx.scene.control.TextField makananField = new javafx.scene.control.TextField();
            makananField.setText(selectedItem.getMakanan());
            javafx.scene.control.TextField beratField = new javafx.scene.control.TextField();
            beratField.setText(String.valueOf(selectedItem.getBeratGram()));
            javafx.scene.control.DatePicker tanggalField = new javafx.scene.control.DatePicker();
            tanggalField.setValue(selectedItem.getTanggal());
            javafx.scene.control.ComboBox<String> waktuField = new javafx.scene.control.ComboBox<>();
            waktuField.getItems().addAll("Pagi", "Siang", "Malam");
            waktuField.setValue(selectedItem.getWaktuMakan());
            
            grid.add(new javafx.scene.control.Label("Makanan:"), 0, 0);
            grid.add(makananField, 1, 0);
            grid.add(new javafx.scene.control.Label("Berat (gram):"), 0, 1);
            grid.add(beratField, 1, 1);
            grid.add(new javafx.scene.control.Label("Tanggal:"), 0, 2);
            grid.add(tanggalField, 1, 2);
            grid.add(new javafx.scene.control.Label("Waktu Makan:"), 0, 3);
            grid.add(waktuField, 1, 3);
            
            dialog.getDialogPane().setContent(grid);
            
            javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Simpan", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);
            
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    try {
                        String makanan = makananField.getText().trim().toLowerCase();
                        double berat = Double.parseDouble(beratField.getText().trim());
                        LocalDate tanggal = tanggalField.getValue();
                        String waktu = waktuField.getValue();
                        
                        double kaloriPer100g = dataKalori.getOrDefault(makanan, 100.0);
                        double totalKalori = (kaloriPer100g * berat) / 100.0;
                        
                        return new RiwayatMakanan(makanan, (int)berat, totalKalori, tanggal, waktu);
                    } catch (NumberFormatException e) {
                        showAlert("Error", "Berat harus berupa angka!");
                        return null;
                    }
                }
                return null;
            });
            
            java.util.Optional<RiwayatMakanan> result = dialog.showAndWait();
            
            if (result.isPresent()) {
                RiwayatMakanan updatedItem = result.get();
                
                List<RiwayatMakanan> userRiwayatMakanan = AppData.getCurrentUserRiwayatMakanan();
                for (int i = 0; i < userRiwayatMakanan.size(); i++) {
                    RiwayatMakanan rm = userRiwayatMakanan.get(i);
                    if (rm.getMakanan().equals(selectedItem.getMakanan()) &&
                        rm.getTanggal().equals(selectedItem.getTanggal()) &&
                        rm.getWaktuMakan().equals(selectedItem.getWaktuMakan()) &&
                        rm.getBeratGram() == selectedItem.getBeratGram()) {
                        
                        userRiwayatMakanan.set(i, updatedItem);
                        break;
                    }
                }
                
                AppData.saveToXml();
                
                loadRiwayatData();
                updateGrafik();
                
                showAlert("Berhasil", "Data makanan berhasil diperbarui!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memperbarui data: " + e.getMessage());
        }
    }
}
