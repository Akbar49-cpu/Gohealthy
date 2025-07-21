package model;

public class JadwalPesanan {
    private String tanggal;
    private String id;
    private String nama;
    private String bidang;
    private String userEmail;

    public JadwalPesanan() {}

    public JadwalPesanan(String tanggal, String id, String nama, String bidang, String userEmail) {
        this.tanggal = tanggal;
        this.id = id;
        this.nama = nama;
        this.bidang = bidang;
        this.userEmail = userEmail;
    }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getBidang() { return bidang; }
    public void setBidang(String bidang) { this.bidang = bidang; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    @Override
    public String toString() {
        return "JadwalPesanan{" +
                "tanggal='" + tanggal + '\'' +
                ", id='" + id + '\'' +
                ", nama='" + nama + '\'' +
                ", bidang='" + bidang + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
