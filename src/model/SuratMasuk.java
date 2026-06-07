package model;

import java.util.Date;

/**
 * Represents an Incoming Letter (Surat Masuk).
 * Demonstrates Inheritance (extends Surat).
 */
public class SuratMasuk extends Surat {
    private String pengirim;

    public SuratMasuk(String id, String nomorSurat, Date tanggal, String perihal, String pengirim) {
        super(id, nomorSurat, tanggal, perihal);
        this.pengirim = pengirim;
    }

    public String getPengirim() { return pengirim; }
    public void setPengirim(String pengirim) { this.pengirim = pengirim; }

    @Override
    public String getJenisSurat() {
        return "Surat Masuk";
    }
}
