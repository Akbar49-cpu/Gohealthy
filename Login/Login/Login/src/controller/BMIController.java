package controller;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class BMIController {

    @FXML
    private TextField beratInput;

    @FXML
    private TextField bmiOutput;

    @FXML
    private Button buttonHitung;

    @FXML
    private Text close;

    @FXML
    private TextField statusOutput;

    @FXML
    private ImageView statusImage;

    @FXML
    private Text statusDescription;

    @FXML
    private TextField tinggiInput;
    
    @FXML 
    private Button btnKembali;

    @FXML
    void Hitung(ActionEvent event) {
        try {
            Double weightValue = Double.parseDouble(beratInput.getText());
            Double heightValue = Double.parseDouble(tinggiInput.getText());
            Double bmiValue = weightValue / ((heightValue/100) * (heightValue/100));
            setResult(bmiValue);
        } catch (Exception e) {}
        }

    void setResult (Double bmiValue) {
        bmiOutput.setText(String.format("%.2f", bmiValue));
        if (statusImage == null) {
            System.out.println("ERROR: statusImage is null! FXML injection failed.");
            return;
        }
       
        if (statusDescription == null) {
            System.out.println("ERROR: statusDescription is null! FXML injection failed.");
            return;
        }
        
        if (bmiValue < 18.5) {
            statusOutput.setText("Kurus");
            setStatusImage("images/kurus.png");
            setStatusDescription("Berat badanmu berada di bawah normal. Ini bisa menjadi tanda bahwa tubuhmu kekurangan nutrisi yang dibutuhkan. Cobalah untuk meningkatkan asupan makanan bergizi tinggi, perhatikan pola makan, dan konsultasikan ke tenaga medis atau ahli gizi agar berat badanmu bisa meningkat secara sehat.");
        } else if (bmiValue < 25) {
            statusOutput.setText("Normal");
            setStatusImage("images/normal.png");
            setStatusDescription("Kamu berada pada kisaran berat badan ideal! Tubuhmu dalam kondisi sehat dan seimbang. Pertahankan pola makan bergizi, olahraga teratur, serta gaya hidup aktif agar kesehatanmu tetap optimal. Tetap semangat menjaga kesehatan!");
        } else if (bmiValue < 30) {
            statusOutput.setText("Gemuk");
            setStatusImage("images/gemuk.png");
            setStatusDescription("Berat badanmu sedikit di atas normal. Ini bisa menjadi awal dari risiko gangguan kesehatan seperti tekanan darah tinggi atau diabetes. Perbaiki pola makan, kurangi makanan tinggi lemak dan gula, dan tingkatkan aktivitas fisik seperti olahraga ringan secara rutin. Mulailah dari kebiasaan kecil namun konsisten.");
        } else {
            statusOutput.setText("Obesitas");
            setStatusImage("images/obesitas.png");
            setStatusDescription("Berat badanmu masuk dalam kategori obesitas. Ini dapat meningkatkan risiko berbagai penyakit kronis seperti jantung, diabetes tipe 2, dan tekanan darah tinggi. Segera lakukan perubahan gaya hidup sehat perbanyak konsumsi makanan sehat, hindari makanan cepat saji, perbanyak aktivitas fisik, dan pertimbangkan berkonsultasi dengan dokter atau ahli gizi untuk langkah penanganan yang tepat.");
        }
    }
    
    private void setStatusImage(String imagePath) {
        try {
            String fullPath = "/view/" + imagePath;
            Image image = new Image(getClass().getResourceAsStream(fullPath));
            
            if (image != null && !image.isError()) {
                statusImage.setImage(image);
                System.out.println("Successfully loaded image: " + fullPath);
            } else {
                System.out.println("Image is null or has error: " + fullPath);
                tryAlternativePaths(imagePath);
            }
            
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            tryAlternativePaths(imagePath);
        }
    }
    
    private void tryAlternativePaths(String imagePath) {
        String[] alternativePaths = {
            "/" + imagePath,
            "../view/" + imagePath,
            "view/" + imagePath,
            imagePath
        };
        
        for (String path : alternativePaths) {
            try {
                Image image = new Image(getClass().getResourceAsStream(path));
                if (image != null && !image.isError()) {
                    statusImage.setImage(image);
                    System.out.println("Successfully loaded image with alternative path: " + path);
                    return;
                }
            } catch (Exception e) {
            }
        }
        
        System.out.println("Failed to load image with all alternative paths: " + imagePath);
    }
    
    private void setStatusDescription(String description) {
        if (statusDescription != null) {
            statusDescription.setText(description);
            System.out.println("Status description updated: " + description.substring(0, Math.min(50, description.length())) + "...");
        } else {
            System.out.println("ERROR: statusDescription is null! FXML injection failed.");
        }
    }

    @FXML
    void close(MouseEvent event) {
        System.exit(0);

    }
    
    @FXML
    private void initialize() {
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
}


}


