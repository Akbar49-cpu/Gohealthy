package controller;

import model.Person;
import model.JadwalPesanan;
import model.User;
import util.AppData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class SearchController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<Person> dataTableView;
    @FXML private TableColumn<Person, String> colTanggal;
    @FXML private TableColumn<Person, String> colId;
    @FXML private TableColumn<Person, String> colNama;
    @FXML private TableColumn<Person, String> colBidang;

    @FXML private TableView<Person> dataTableView1;
    @FXML private TableColumn<Person, String> colTanggal1;
    @FXML private TableColumn<Person, String> colId1;
    @FXML private TableColumn<Person, String> colNama1;
    @FXML private TableColumn<Person, String> colBidangPesan;

    @FXML private Button btnKembali;
    @FXML private Button btnMemesan;
    @FXML private Button btnBatal;

    private final ObservableList<Person> masterList = FXCollections.observableArrayList();
    private final ObservableList<Person> pesananList = FXCollections.observableArrayList();
    private FilteredList<Person> filteredList;
    
    private ArrayList<Person> sampleArrayList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (AppData.getCurrentUser() == null && !AppData.userList.isEmpty()) {
            AppData.currentUser = AppData.userList.get(0);
        }
        
        btnKembali.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnKembali.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loadDataFromArrayList();
        
        setupTableColumns();
        setupTableColumns2();
        setupSearchFilter();
        setupPemesananButton();
        setupBatalButton();
        
        loadExistingBookings();
        
        System.out.println("SearchController initialize() selesai. Data count: " + masterList.size());
    }

    @SuppressWarnings("unused")
    private void loadDataFromXML() {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(getClass().getResourceAsStream("/data.xml"));
            NodeList nodes = doc.getElementsByTagName("person");

            for (int i = 0; i < nodes.getLength(); i++) {
                String tanggal = nodes.item(i).getAttributes().getNamedItem("tanggal").getNodeValue();
                String id = nodes.item(i).getAttributes().getNamedItem("id").getNodeValue();
                String nama = nodes.item(i).getAttributes().getNamedItem("nama").getNodeValue();
                String bidang = nodes.item(i).getAttributes().getNamedItem("bidang").getNodeValue();

                masterList.add(new Person(tanggal, id, nama, bidang));
            }
            System.out.println("Data loaded successfully. Total records: " + masterList.size());
        } catch (Exception e) {
            System.err.println("Error loading data from XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDataFromArrayList() {
        try {
            masterList.clear();
            
            sampleArrayList.clear();
            sampleArrayList.add(new Person("16Juli2025", "A001", "Dr. Ahmad", "Dokter"));
            sampleArrayList.add(new Person("16Juli2025", "A002", "Siti Nurhaliza", "Ahli Gizi"));
            sampleArrayList.add(new Person("17Juli2025", "A003", "Dr. Budi", "Dokter"));
            sampleArrayList.add(new Person("17Juli2025", "A004", "Andi Permata", "Ahli Gizi"));
            sampleArrayList.add(new Person("18Juli2025", "A005", "Dr. Citra", "Dokter"));
            sampleArrayList.add(new Person("18Juli2025", "A006", "Dewi Sartika", "Ahli Gizi"));
            sampleArrayList.add(new Person("19Juli2025", "A007", "Dr. Dedi", "Dokter"));
            sampleArrayList.add(new Person("19Juli2025", "A008", "Eka Putri", "Ahli Gizi"));
            sampleArrayList.add(new Person("20Juli2025", "A009", "Dr. Fajar", "Dokter"));
            sampleArrayList.add(new Person("20Juli2025", "A010", "Fitri Handayani", "Ahli Gizi"));
            
            masterList.addAll(sampleArrayList);
            
            System.out.println("Data loaded successfully from ArrayList. Total records: " + masterList.size());
        } catch (Exception e) {
            System.err.println("Error loading data from ArrayList: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colId.setCellValueFactory(new PropertyValueFactory<>("iD"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colBidang.setCellValueFactory(new PropertyValueFactory<>("bidang"));
    }

    private void setupTableColumns2() {
        colTanggal1.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colId1.setCellValueFactory(new PropertyValueFactory<>("iD"));
        colNama1.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colBidangPesan.setCellValueFactory(new PropertyValueFactory<>("bidang"));
        
        dataTableView1.setItems(pesananList);
    }

    private void setupPemesananButton() {
        btnMemesan.setOnAction(e -> {
            System.out.println("Button Memesan diklik!");
            
            Person selectedPerson = dataTableView.getSelectionModel().getSelectedItem();
            
            System.out.println("Selected person: " + (selectedPerson != null ? selectedPerson.getNama() : "null"));
            
            if (selectedPerson != null) {
                pesananList.add(selectedPerson);
                
                masterList.remove(selectedPerson);
                
                saveJadwalPesanan(selectedPerson);
                
                dataTableView.refresh();
                dataTableView1.refresh();
                
                showAlert("Berhasil", "Jadwal berhasil dipesan:\n" + 
                         selectedPerson.getNama() + " - " + selectedPerson.getBidang() + 
                         "\nTanggal: " + selectedPerson.getTanggal());
                
                updateStatusDisplay();
                
                System.out.println("Jadwal berhasil dipesan: " + selectedPerson.getNama() + " - " + selectedPerson.getBidang());
            } else {
                System.out.println("Tidak ada jadwal yang dipilih!");
                showAlert("Peringatan", "Silakan pilih jadwal yang ingin dipesan terlebih dahulu!");
            }
        });
    }

    private void updateStatusDisplay() {
        System.out.println("Status: Jadwal tersedia = " + masterList.size() + 
                          ", Jadwal dipesan = " + pesananList.size());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveJadwalPesanan(Person selectedPerson) {
        try {
            System.out.println("Menyimpan jadwal pesanan untuk: " + selectedPerson.getNama());
            
            JadwalPesanan jadwalPesanan = new JadwalPesanan(
                selectedPerson.getTanggal(),
                selectedPerson.getID(),
                selectedPerson.getNama(),
                selectedPerson.getBidang(),
                AppData.getCurrentUser() != null ? AppData.getCurrentUser().getEmail() : "unknown"
            );
            
            User currentUser = AppData.getCurrentUser();
            System.out.println("Current user: " + (currentUser != null ? currentUser.getEmail() : "null"));
            
            if (currentUser != null) {
                currentUser.addJadwalPesanan(jadwalPesanan);
                AppData.saveData();
                System.out.println("Jadwal pesanan berhasil disimpan untuk user: " + currentUser.getEmail());
            } else {
                System.err.println("Error: Tidak ada user yang sedang login");
                System.out.println("Melanjutkan tanpa menyimpan ke user data...");
            }
        } catch (Exception ex) {
            System.err.println("Error saat menyimpan jadwal pesanan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void loadExistingBookings() {
        try {
            User currentUser = AppData.getCurrentUser();
            if (currentUser != null && currentUser.getJadwalPesananList() != null) {
                for (JadwalPesanan jadwal : currentUser.getJadwalPesananList()) {
                    Person person = new Person(
                        jadwal.getTanggal(),
                        jadwal.getId(),
                        jadwal.getNama(),
                        jadwal.getBidang()
                    );
                    pesananList.add(person);
                }
                System.out.println("Loaded " + pesananList.size() + " existing bookings for user: " + currentUser.getEmail());
            }
        } catch (Exception ex) {
            System.err.println("Error loading existing bookings: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    
    private void setupSearchFilter() {
        filteredList = new FilteredList<>(masterList, p -> true);
        dataTableView.setItems(filteredList);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; 
                }
                String key = newValue.toLowerCase().trim();
                
                boolean matches = person.getTanggal().toLowerCase().contains(key) ||
                                person.getID().toLowerCase().contains(key) ||
                                person.getNama().toLowerCase().contains(key) ||
                                person.getBidang().toLowerCase().contains(key);
                return matches;
            });
            System.out.println("Search applied for: '" + newValue + "'. Filtered results: " + filteredList.size());
        });
    }
    
    private void setupBatalButton() {
        btnBatal.setOnAction(e -> {
            Person selectedPerson = dataTableView1.getSelectionModel().getSelectedItem();
            
            if (selectedPerson != null) {
                masterList.add(selectedPerson);
                
                pesananList.remove(selectedPerson);
                
                removeJadwalPesanan(selectedPerson);
                
                dataTableView.refresh();
                dataTableView1.refresh();
                
                showAlert("Berhasil", "Jadwal berhasil dibatalkan:\n" + 
                         selectedPerson.getNama() + " - " + selectedPerson.getBidang() + 
                         "\nTanggal: " + selectedPerson.getTanggal());
                
                updateStatusDisplay();
                
                System.out.println("Jadwal berhasil dibatalkan: " + selectedPerson.getNama() + " - " + selectedPerson.getBidang());
            } else {
                showAlert("Peringatan", "Silakan pilih jadwal yang ingin dibatalkan terlebih dahulu!");
            }
        });
    }
    
    private void removeJadwalPesanan(Person selectedPerson) {
        try {
            User currentUser = AppData.getCurrentUser();
            if (currentUser != null && currentUser.getJadwalPesananList() != null) {
                currentUser.getJadwalPesananList().removeIf(jadwal -> 
                    jadwal.getTanggal().equals(selectedPerson.getTanggal()) &&
                    jadwal.getId().equals(selectedPerson.getID()) &&
                    jadwal.getNama().equals(selectedPerson.getNama()) &&
                    jadwal.getBidang().equals(selectedPerson.getBidang())
                );
                
                AppData.saveData();
                System.out.println("Jadwal pesanan berhasil dihapus untuk user: " + currentUser.getEmail());
            } else {
                System.err.println("Error: Tidak ada user yang sedang login atau tidak ada jadwal pesanan");
            }
        } catch (Exception ex) {
            System.err.println("Error saat menghapus jadwal pesanan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
