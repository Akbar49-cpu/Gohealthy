package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import model.User;
import model.UserData;
import model.RiwayatMakanan;
import model.DataBeratBadan;
import model.JadwalPesanan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmlManager {

    private static final String FILE_PATH = "users_xstream.xml";
    private static final String FALLBACK_PATH = "src/users_xstream.xml";
    private static XStream xstream;

    static {
        xstream = new XStream();

        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("user", User.class);
        xstream.alias("userData", UserData.class);
        xstream.alias("riwayatMakanan", RiwayatMakanan.class);
        xstream.alias("dataBeratBadan", DataBeratBadan.class);
        xstream.alias("jadwalPesanan", JadwalPesanan.class);
        xstream.addImplicitCollection(UserData.class, "users");
    }

    private static String getFilePath() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            return FILE_PATH;
        }
        
        file = new File(FALLBACK_PATH);
        if (file.exists()) {
            return FALLBACK_PATH;
        }
        
        return FILE_PATH;
    }

    public static void saveData(UserData userData) {
        String filePath = getFilePath();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            xstream.toXML(userData, fos);
            System.out.println("Data berhasil disimpan ke " + filePath);
        } catch (IOException e) {
            System.err.println("Error menyimpan data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static UserData loadData() {
        String filePath = getFilePath();
        File file = new File(filePath);
        
        System.out.println("Mencoba memuat data dari: " + file.getAbsolutePath());
        
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                UserData userData = (UserData) xstream.fromXML(fis);
                System.out.println("Data berhasil dimuat dari " + filePath);
                
                if (userData.getUsers() != null) {
                    System.out.println("Jumlah user yang dimuat: " + userData.getUsers().size());
                    for (User user : userData.getUsers()) {
                        if (user.getNama() == null || user.getNama().isEmpty()) {
                            String defaultName = user.getEmail().substring(0, user.getEmail().indexOf("@"));
                            user.setNama(defaultName);
                        }
                        System.out.println("User dimuat: " + user.getEmail());
                    }
                } else {
                    System.out.println("Tidak ada user yang ditemukan dalam file XML");
                }
                return userData;
            } catch (IOException e) {
                System.err.println("Error membaca file XML: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error parsing XML: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("File XML tidak ditemukan di: " + file.getAbsolutePath());
        }
        
        System.out.println("Membuat UserData baru karena file tidak dapat dimuat");
        return new UserData();
    }
}