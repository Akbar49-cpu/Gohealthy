package model;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    private final SimpleStringProperty tanggal = new SimpleStringProperty();
    private final SimpleStringProperty id = new SimpleStringProperty();
    private final SimpleStringProperty nama = new SimpleStringProperty();
    private final SimpleStringProperty bidang = new SimpleStringProperty();

    public Person(String tanggal, String id, String nama, String bidang) {
        setTanggal(tanggal);
        setID(id);
        setNama(nama);
        setBidang(bidang);
    }

    public String getTanggal() {return tanggal.get(); }
    public void setTanggal(String v) { this.tanggal.set(v); }

    public String getID() {return id.get(); }
    public void setID(String v) { this.id.set(v); }

    public String getNama() {return nama.get(); }
    public void setNama(String v) { this.nama.set(v); }

    public String getBidang() {return bidang.get(); }
    public void setBidang(String v) { this.bidang.set(v); }
}

