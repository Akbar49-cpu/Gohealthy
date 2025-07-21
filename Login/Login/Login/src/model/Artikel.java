package model;

public class Artikel {
    private String id;
    private String judul;
    private String deskripsi;
    private String konten;
    private String penulis;
    private String tanggal;
    private String kategori;
    
    public Artikel(String id, String judul, String deskripsi, String konten, String penulis, String tanggal, String kategori) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.konten = konten;
        this.penulis = penulis;
        this.tanggal = tanggal;
        this.kategori = kategori;
    }
    
    public String getId() { return id; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public String getKonten() { return konten; }
    public String getPenulis() { return penulis; }
    public String getTanggal() { return tanggal; }
    public String getKategori() { return kategori; }
    
    public void setId(String id) { this.id = id; }
    public void setJudul(String judul) { this.judul = judul; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setKonten(String konten) { this.konten = konten; }
    public void setPenulis(String penulis) { this.penulis = penulis; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    
    @Override
    public String toString() {
        return "Artikel{" +
                "id='" + id + '\'' +
                ", judul='" + judul + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", penulis='" + penulis + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", kategori='" + kategori + '\'' +
                '}';
    }
}
