package model;

import java.util.Date;

/**
 * Represents an Incoming Letter (Surat Masuk).
 * Demonstrates Inheritance (extends Surat).
 */
public class SuratMasuk extends Surat {
    private String pengirim;
    private String filePath;

    public SuratMasuk(
    String id,
    String nomorSurat,
    Date tanggal,
    String perihal,
    String pengirim,
    String filePath
    ) {
        super(id, nomorSurat, tanggal, perihal);
        this.pengirim = pengirim;
        this.filePath = filePath;
    }

    public String getPengirim() { return pengirim; }
    public void setPengirim(String pengirim) { this.pengirim = pengirim; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    @Override
    public String getJenisSurat() {
        return "Surat Masuk";
    }
}
