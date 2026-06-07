package model;

import java.util.Date;

/**
 * Represents an Outgoing Letter (Surat Keluar).
 * Demonstrates Inheritance (extends Surat).
 */
public class SuratKeluar extends Surat {
    private String penerima;

    public SuratKeluar(String id, String nomorSurat, Date tanggal, String perihal, String penerima) {
        super(id, nomorSurat, tanggal, perihal);
        this.penerima = penerima;
    }

    public String getPenerima() { return penerima; }
    public void setPenerima(String penerima) { this.penerima = penerima; }

    @Override
    public String getJenisSurat() {
        return "Surat Keluar";
    }
}
