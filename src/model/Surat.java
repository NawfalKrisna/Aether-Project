package model;

import java.util.Date;

/**
 * Abstract class representing a generic Letter (Surat).
 * Demonstrates Abstraction and Encapsulation.
 */
public abstract class Surat {
    private String id;
    private String nomorSurat;
    private Date tanggal;
    private String perihal;

    public Surat(String id, String nomorSurat, Date tanggal, String perihal) {
        this.id = id;
        this.nomorSurat = nomorSurat;
        this.tanggal = tanggal;
        this.perihal = perihal;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomorSurat() { return nomorSurat; }
    public void setNomorSurat(String nomorSurat) { this.nomorSurat = nomorSurat; }

    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }

    public String getPerihal() { return perihal; }
    public void setPerihal(String perihal) { this.perihal = perihal; }

    /**
     * Abstract method to get the type of letter.
     * Demonstrates Polymorphism.
     * @return Type of letter (Surat Masuk / Surat Keluar)
     */
    public abstract String getJenisSurat();
}
