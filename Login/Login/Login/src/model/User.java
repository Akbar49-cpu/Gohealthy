package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nama;
    private String email;
    private String password;
    private Double beratBadan;
    private Double tinggiBadan;
    private Double targetKalori;
    private List<RiwayatMakanan> riwayatMakananList = new ArrayList<>();
    private List<DataBeratBadan> dataBeratBadanList = new ArrayList<>();
    private List<JadwalPesanan> jadwalPesananList = new ArrayList<>();

  
    public User() {
    }

    public User(String nama, String email, String password) {
        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    public User(String nama, String email, String password, Double beratBadan, Double tinggiBadan) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.beratBadan = beratBadan;
        this.tinggiBadan = tinggiBadan;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Double getBeratBadan() { return beratBadan; }
    public void setBeratBadan(Double beratBadan) { this.beratBadan = beratBadan; }
    public Double getTinggiBadan() { return tinggiBadan; }
    public void setTinggiBadan(Double tinggiBadan) { this.tinggiBadan = tinggiBadan; }
    public Double getTargetKalori() { return targetKalori; }
    public void setTargetKalori(Double targetKalori) { this.targetKalori = targetKalori; }
    
    public List<RiwayatMakanan> getRiwayatMakananList() { return riwayatMakananList; }
    public void setRiwayatMakananList(List<RiwayatMakanan> riwayatMakananList) { this.riwayatMakananList = riwayatMakananList; }
    public List<DataBeratBadan> getDataBeratBadanList() { return dataBeratBadanList; }
    public void setDataBeratBadanList(List<DataBeratBadan> dataBeratBadanList) { this.dataBeratBadanList = dataBeratBadanList; }
    
    public List<JadwalPesanan> getJadwalPesananList() { return jadwalPesananList; }
    public void setJadwalPesananList(List<JadwalPesanan> jadwalPesananList) { this.jadwalPesananList = jadwalPesananList; }
    
    public void addJadwalPesanan(JadwalPesanan jadwalPesanan) {
        this.jadwalPesananList.add(jadwalPesanan);
    }
}