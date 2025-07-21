package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RiwayatMakanan {
    private String makanan;
    private double beratGram;
    private double kalori;
    private LocalDate tanggal;
    private String waktuMakan; 
    
    public RiwayatMakanan(String makanan, double beratGram, double kalori, LocalDate tanggal, String waktuMakan) {
        this.makanan = makanan;
        this.beratGram = beratGram;
        this.kalori = kalori;
        this.tanggal = tanggal;
        this.waktuMakan = waktuMakan;
    }
    
    public String getMakanan() { return makanan; }
    public double getBeratGram() { return beratGram; }
    public double getKalori() { return kalori; }
    public LocalDate getTanggal() { return tanggal; }
    public String getWaktuMakan() { return waktuMakan; }
    
    public String getBeratGramFormatted() {
        return String.format("%.1f", beratGram);
    }
    
    public String getKaloriFormatted() {
        return String.format("%.2f", kalori);
    }
    
    public String getTanggalFormatted() {
        return tanggal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    public void setMakanan(String makanan) { this.makanan = makanan; }
    public void setBeratGram(double beratGram) { this.beratGram = beratGram; }
    public void setKalori(double kalori) { this.kalori = kalori; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    public void setWaktuMakan(String waktuMakan) { this.waktuMakan = waktuMakan; }
    
    @Override
    public String toString() {
        return "RiwayatMakanan{" +
                "makanan='" + makanan + '\'' +
                ", beratGram=" + beratGram +
                ", kalori=" + kalori +
                ", tanggal=" + tanggal +
                ", waktuMakan='" + waktuMakan + '\'' +
                '}';
    }
}
