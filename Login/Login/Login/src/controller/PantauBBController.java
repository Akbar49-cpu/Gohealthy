package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.DataBeratBadan;
import util.AppData;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PantauBBController implements Initializable {

    @FXML
    private TextField beratInput;

    @FXML
    private DatePicker tanggalPicker;

    @FXML
    private Button tambahButton;

    @FXML
    private Button hapusButton;

    @FXML
    private TextField updateBeratInput;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text updateLabel;

    @FXML
    private TableView<DataBeratBadan> tableData;

    @FXML
    private TableColumn<DataBeratBadan, String> columnTanggal;

    @FXML
    private TableColumn<DataBeratBadan, Double> columnBerat;

    @FXML
    private LineChart<String, Number> grafikBerat;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private Text close;

    @FXML 
    private Button btnKembali;

    @FXML
    private Text beratTertinggi;

    @FXML
    private Text beratTerendah;

    private ArrayList<DataBeratBadan> dataList = new ArrayList<>();
    private DataBeratBadan selectedDataForUpdate = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnKembali.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnKembali.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        loadDataFromAppData();
        
        columnTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        columnBerat.setCellValueFactory(new PropertyValueFactory<>("berat"));
        tableData.getItems().setAll(dataList);
        tableData.setOnMouseClicked(event -> {
            System.out.println("Mouse clicked! Click count: " + event.getClickCount() + ", Primary button: " + event.isPrimaryButtonDown());
            if (event.getClickCount() == 1) {
                DataBeratBadan selectedItem = tableData.getSelectionModel().getSelectedItem();
                System.out.println("Single-click detected! Selected item: " + (selectedItem != null ? selectedItem.getTanggal() : "null"));
                if (selectedItem != null) {
                    showUpdateForm(selectedItem);
                }
            }
        });
        tableData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selection changed to: " + newValue.getTanggal());
                showUpdateForm(newValue);
            }
        });
        tableData.setRowFactory(tv -> {
            javafx.scene.control.TableRow<DataBeratBadan> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    DataBeratBadan item = row.getItem();
                    System.out.println("Row clicked: " + item.getTanggal());
                    showUpdateForm(item);
                }
            });
            return row;
        });
        tanggalPicker.setValue(LocalDate.now());
        filterComboBox.setItems(FXCollections.observableArrayList(
            "Semua Data", "Seminggu", "Sebulan", "3 Bulan", "6 Bulan", "1 Tahun"
        ));
        filterComboBox.setValue("Semua Data");
        grafikBerat.setTitle("Grafik Berat Badan");
        grafikBerat.getXAxis().setLabel("Tanggal");
        grafikBerat.getYAxis().setLabel("Berat (Kg)");
        updateGrafik();

        updateGrafik();
        System.out.println("Controller initialized successfully. Sample data added: " + dataList.size() + " items");
    }

    @FXML
    void tambahData(ActionEvent event) {
        try {
            String beratText = beratInput.getText().trim();
            LocalDate tanggal = tanggalPicker.getValue();

            if (beratText.isEmpty() || tanggal == null) {
                showAlert("Error", "Mohon isi berat badan dan pilih tanggal!");
                return;
            }

            double berat = Double.parseDouble(beratText);
            if (berat <= 0 || berat > 500) {
                showAlert("Error", "Berat badan harus antara 1-500 kg!");
                return;
            }

            boolean exists = dataList.stream()
                .anyMatch(data -> data.getTanggalDate().equals(tanggal));

            if (exists) {
                showAlert("Warning", "Data untuk tanggal tersebut sudah ada! Silakan edit atau pilih tanggal lain.");
                return;
            }
            DataBeratBadan newData = new DataBeratBadan(tanggal, berat);
            dataList.add(newData);

            dataList.sort((a, b) -> a.getTanggalDate().compareTo(b.getTanggalDate()));

            saveDataToAppData();
            
            tableData.getItems().setAll(dataList);
            updateGrafik();

            beratInput.clear();
            tanggalPicker.setValue(LocalDate.now());

            System.out.println("Data berhasil ditambahkan: " + newData.getTanggal() + " - " + newData.getBerat() + " kg");        } catch (NumberFormatException e) {
            showAlert("Error", "Format berat badan tidak valid!");
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    @FXML
    void hapusData(ActionEvent event) {
        DataBeratBadan selected = tableData.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dataList.remove(selected);
            
            saveDataToAppData();
            
            tableData.getItems().setAll(dataList);
            updateGrafik();
            hideUpdateForm();
            System.out.println("Data dihapus: " + selected.getTanggal() + " - " + selected.getBerat() + " kg");
        } else {
            showAlert("Warning", "Pilih data yang ingin dihapus dari tabel!");
        }
    }

    @FXML
    void filterGrafik(ActionEvent event) {
        updateGrafik();
    }

    @FXML
    void updateData(ActionEvent event) {
        try {
            String beratText = updateBeratInput.getText().trim();
            
            if (beratText.isEmpty()) {
                showAlert("Error", "Mohon isi berat badan baru!");
                return;
            }

            double beratBaru = Double.parseDouble(beratText);
            if (beratBaru <= 0 || beratBaru > 500) {
                showAlert("Error", "Berat badan harus antara 1-500 kg!");
                return;
            }

            if (selectedDataForUpdate != null) {
                int index = dataList.indexOf(selectedDataForUpdate);
                if (index >= 0) {
                    DataBeratBadan updatedData = new DataBeratBadan(
                        selectedDataForUpdate.getTanggalDate(), 
                        beratBaru
                    );
                    dataList.set(index, updatedData);
                    
                    saveDataToAppData();
                    
                    tableData.getItems().setAll(dataList);
                    updateGrafik();
                    hideUpdateForm();
                    System.out.println("Data berhasil diupdate: " + updatedData.getTanggal() + " - " + updatedData.getBerat() + " kg");
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Format berat badan tidak valid!");
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    @FXML
    void cancelUpdate(ActionEvent event) {
        hideUpdateForm();
    }

    private void showUpdateForm(DataBeratBadan data) {
        System.out.println("showUpdateForm called with data: " + data.getTanggal() + " - " + data.getBerat());
        if (updateLabel == null || updateBeratInput == null || updateButton == null || cancelButton == null) {
            System.err.println("ERROR: Update form components are null!");
            System.err.println("updateLabel: " + updateLabel);
            System.err.println("updateBeratInput: " + updateBeratInput);
            System.err.println("updateButton: " + updateButton);
            System.err.println("cancelButton: " + cancelButton);
            return;
        }
        
        selectedDataForUpdate = data;
        updateBeratInput.setText(String.valueOf(data.getBerat()));
        updateLabel.setVisible(true);
        updateBeratInput.setVisible(true);
        updateButton.setVisible(true);
        cancelButton.setVisible(true);
        
        System.out.println("Update form components set to visible");
        updateBeratInput.requestFocus();
        updateBeratInput.selectAll();
    }

    private void hideUpdateForm() {
        selectedDataForUpdate = null;
        updateBeratInput.clear();
        updateLabel.setVisible(false);
        updateBeratInput.setVisible(false);
        updateButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    private void updateGrafik() {
        grafikBerat.getData().clear();

        if (dataList.isEmpty()) {
            updateStatistik();
            return;
        }

        List<DataBeratBadan> filteredData = getFilteredData();

        if (filteredData.isEmpty()) {
            updateStatistik();
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Berat Badan");

        for (DataBeratBadan data : filteredData) {
            series.getData().add(new XYChart.Data<>(data.getTanggal(), data.getBerat()));
        }

        grafikBerat.getData().add(series);
        updateStatistik();
    }

    private List<DataBeratBadan> getFilteredData() {
        String filter = filterComboBox.getValue();
        LocalDate now = LocalDate.now();

        switch (filter) {
            case "1 Minggu":
                LocalDate weekAgo = now.minusWeeks(1);
                return dataList.stream()
                    .filter(data -> data.getTanggalDate().isAfter(weekAgo.minusDays(1)) && 
                                   data.getTanggalDate().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());

            case "1 Bulan":
                LocalDate monthAgo = now.minusMonths(1);
                return dataList.stream()
                    .filter(data -> data.getTanggalDate().isAfter(monthAgo.minusDays(1)) && 
                                   data.getTanggalDate().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());

            case "3 Bulan":
                LocalDate threeMonthsAgo = now.minusMonths(3);
                return dataList.stream()
                    .filter(data -> data.getTanggalDate().isAfter(threeMonthsAgo.minusDays(1)) && 
                                   data.getTanggalDate().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());

            case "6 Bulan":
                LocalDate sixMonthsAgo = now.minusMonths(6);
                return dataList.stream()
                    .filter(data -> data.getTanggalDate().isAfter(sixMonthsAgo.minusDays(1)) && 
                                   data.getTanggalDate().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());

            case "1 Tahun":
                LocalDate yearAgo = now.minusYears(1);
                return dataList.stream()
                    .filter(data -> data.getTanggalDate().isAfter(yearAgo.minusDays(1)) && 
                                   data.getTanggalDate().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());

            case "Semua Data":
            default:
                return new ArrayList<>(dataList);
        }
    }

    private void updateStatistik() {
        List<DataBeratBadan> filteredData = getFilteredData();
        
        if (filteredData.isEmpty()) {
            beratTertinggi.setText("• Berat Tertinggi: -");
            beratTerendah.setText("• Berat Terendah: -");
            return;
        }

        double max = filteredData.stream()
            .mapToDouble(DataBeratBadan::getBerat)
            .max()
            .orElse(0.0);

        double min = filteredData.stream()
            .mapToDouble(DataBeratBadan::getBerat)
            .min()
            .orElse(0.0);

        beratTertinggi.setText(String.format("• Berat Tertinggi: %.1f kg", max));
        beratTerendah.setText(String.format("• Berat Terendah: %.1f kg", min));
    }

    private void showAlert(String title, String message) {
        System.out.println(title + ": " + message);
    }

    @FXML
    void close(MouseEvent event) {
        System.exit(0);
    }
    
    private void loadDataFromAppData() {
        dataList.clear();
        dataList.addAll(AppData.getCurrentUserDataBeratBadan());
        if (tableData != null) {
            tableData.getItems().setAll(dataList);
        }
    }
    
    private void saveDataToAppData() {
        List<DataBeratBadan> currentUserData = AppData.getCurrentUserDataBeratBadan();
        currentUserData.clear();
        currentUserData.addAll(dataList);
        
        AppData.saveToXml();
    }
}
