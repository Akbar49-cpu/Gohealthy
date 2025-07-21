package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataBeratBadan {
    private final LocalDate tanggalDate;
    private final double berat;

    public DataBeratBadan(LocalDate tanggal, double berat) {
        this.tanggalDate = tanggal;
        this.berat = berat;
    }

    public String getTanggal() {
        return tanggalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate getTanggalDate() {
        return tanggalDate;
    }

    public double getBerat() {
        return berat;
    }
    
    @Override
    public String toString() {
        return "DataBeratBadan{" +
                "tanggal=" + tanggalDate +
                ", berat=" + berat +
                '}';
    }
}
