package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Artikel;
import model.User;
import model.UserData;
import model.RiwayatMakanan;
import model.DataBeratBadan;

public class AppData {
    public static User currentUser = null;
    public static final List<User> userList = new ArrayList<>();
    public static final List<Artikel> artikelList = new ArrayList<>();
    
    public static final Map<String, List<RiwayatMakanan>> userRiwayatMakananMap = new HashMap<>();
    public static final Map<String, List<DataBeratBadan>> userDataBeratBadanMap = new HashMap<>();
    
    public static final List<RiwayatMakanan> riwayatMakananList = new ArrayList<>();
    public static final List<DataBeratBadan> dataBeratBadanList = new ArrayList<>();
    
    public static List<RiwayatMakanan> getCurrentUserRiwayatMakanan() {
        if (currentUser == null) return new ArrayList<>();
        return userRiwayatMakananMap.computeIfAbsent(currentUser.getEmail(), k -> new ArrayList<>());
    }
    
    public static List<DataBeratBadan> getCurrentUserDataBeratBadan() {
        if (currentUser == null) return new ArrayList<>();
        return userDataBeratBadanMap.computeIfAbsent(currentUser.getEmail(), k -> new ArrayList<>());
    }
    
    public static void initializeUserData(String email) {
        if (!userRiwayatMakananMap.containsKey(email)) {
            userRiwayatMakananMap.put(email, new ArrayList<>());
        }
        if (!userDataBeratBadanMap.containsKey(email)) {
            userDataBeratBadanMap.put(email, new ArrayList<>());
        }
    }
    
    public static boolean isFirstTimeUser(String email) {
        return !userRiwayatMakananMap.containsKey(email) && !userDataBeratBadanMap.containsKey(email);
    }
    
    public static void loadUserDataFromXml() {
        for (User user : userList) {
            String email = user.getEmail();
            if (user.getRiwayatMakananList() != null) {
                userRiwayatMakananMap.put(email, new ArrayList<>(user.getRiwayatMakananList()));
            }
            if (user.getDataBeratBadanList() != null) {
                userDataBeratBadanMap.put(email, new ArrayList<>(user.getDataBeratBadanList()));
            }
        }
    }
    
    public static void saveUserDataToXml() {
        for (User user : userList) {
            String email = user.getEmail();
            if (userRiwayatMakananMap.containsKey(email)) {
                user.setRiwayatMakananList(new ArrayList<>(userRiwayatMakananMap.get(email)));
            }
            if (userDataBeratBadanMap.containsKey(email)) {
                user.setDataBeratBadanList(new ArrayList<>(userDataBeratBadanMap.get(email)));
            }
        }
    }
    
    public static void saveToXml() {
        saveUserDataToXml();
        UserData userData = new UserData();
        userData.setUsers(userList);
        XmlManager.saveData(userData);
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void saveData() {
        saveToXml();
    }
}