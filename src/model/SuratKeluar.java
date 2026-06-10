package model;

import java.util.Date;

/**
 * Represents an Outgoing Letter (Surat Keluar).
 * Demonstrates Inheritance (extends Surat).
 */
public class SuratKeluar extends Surat {
    private String penerima;
    private String filePath;

    public SuratKeluar(
    String id,
    String nomorSurat,
    Date tanggal,
    String perihal,
    String penerima,
    String filePath 
) {
        super(id, nomorSurat, tanggal, perihal);
        this.penerima = penerima;
        this.filePath = filePath;
    }

    public String getPenerima() { return penerima; }
    public void setPenerima(String penerima) { this.penerima = penerima; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    @Override
    public String getJenisSurat() {
        return "Surat Keluar";
    }
}
