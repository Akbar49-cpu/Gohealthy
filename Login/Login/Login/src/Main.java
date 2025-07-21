import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Artikel;
import model.UserData;
import util.AppData;
import util.XmlManager;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        
        UserData loadedData = XmlManager.loadData();
        if (loadedData != null && loadedData.getUsers() != null) {
            AppData.userList.addAll(loadedData.getUsers());
            AppData.loadUserDataFromXml();
        }
        
        loadArtikelData();
    }
    
    private void loadArtikelData() {
        AppData.artikelList.clear();
        
        AppData.artikelList.add(new Artikel(
            "1",
            "Pentingnya pola hidup sehat",
            "Pola hidup sehat telah lama diakui sebagai pondasi utama dalam menjaga kesehatan fisik dan mental seseorang.",
            "Pola hidup sehat adalah kunci utama untuk mencapai kesehatan yang optimal. Ini meliputi pola makan yang seimbang, olahraga teratur, istirahat yang cukup, dan mengelola stres dengan baik. Dengan menerapkan pola hidup sehat, kita dapat mencegah berbagai penyakit kronis seperti diabetes, hipertensi, dan penyakit jantung.",
            "Dr. Health Expert",
            "15 Juli 2025",
            "Kesehatan Umum"
        ));
        
        AppData.artikelList.add(new Artikel(
            "2",
            "Bahaya gula berlebih",
            "Konsumsi gula berlebihan dapat memicu berbagai gangguan kesehatan seperti diabetes dan obesitas.",
            "Gula berlebihan dalam makanan dan minuman telah menjadi perhatian serius dalam dunia kesehatan. Konsumsi gula yang berlebihan dapat menyebabkan obesitas, diabetes tipe 2, kerusakan gigi, dan berbagai masalah kesehatan lainnya. Penting untuk membatasi konsumsi gula harian dan memilih alternatif yang lebih sehat.",
            "Dr. Nutrition Specialist",
            "14 Juli 2025",
            "Nutrisi"
        ));
        
        AppData.artikelList.add(new Artikel(
            "3",
            "Pentingnya makan sehat",
            "Makanan sehat berperan penting dalam menjaga energi, berat badan ideal, dan daya tahan tubuh.",
            "Makan sehat bukan hanya tentang membatasi kalori, tetapi tentang memberikan nutrisi yang tepat untuk tubuh. Makanan sehat yang kaya akan vitamin, mineral, dan antioksidan dapat meningkatkan sistem kekebalan tubuh, memberikan energi yang stabil, dan membantu menjaga berat badan ideal.",
            "Ahli Gizi Terdaftar",
            "13 Juli 2025",
            "Nutrisi"
        ));
        
        AppData.artikelList.add(new Artikel(
            "4",
            "Manfaat olahraga rutin",
            "Aktivitas fisik rutin seperti olahraga ringan membantu menjaga tubuh tetap bugar dan sehat.",
            "Olahraga rutin memiliki manfaat yang luar biasa bagi kesehatan fisik dan mental. Aktivitas fisik dapat meningkatkan kesehatan jantung, memperkuat otot dan tulang, meningkatkan mood, dan membantu mengelola berat badan. Bahkan olahraga ringan selama 30 menit setiap hari dapat memberikan manfaat kesehatan yang signifikan.",
            "Pelatih Kebugaran",
            "12 Juli 2025",
            "Olahraga"
        ));
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/Welcome.fxml"));
        primaryStage.setTitle("Go Healthy");
        
        primaryStage.setScene(new Scene(root, 1920, 1080)); 
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        
        primaryStage.setMaximized(true);
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}