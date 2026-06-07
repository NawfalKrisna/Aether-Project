package service;

import data.DummyData;
import model.Surat;
import model.SuratKeluar;
import model.SuratMasuk;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling business logic related to Letters.
 */
public class SuratService {
    private List<Surat> semuaSurat;

    public SuratService() {
        // Initialize with dummy data
        this.semuaSurat = DummyData.generateData();
    }

    public List<Surat> getAllSurat() {
        return semuaSurat;
    }

    public List<SuratMasuk> getSuratMasuk() {
        return semuaSurat.stream()
                .filter(s -> s instanceof SuratMasuk)
                .map(s -> (SuratMasuk) s)
                .collect(Collectors.toList());
    }

    public List<SuratKeluar> getSuratKeluar() {
        return semuaSurat.stream()
                .filter(s -> s instanceof SuratKeluar)
                .map(s -> (SuratKeluar) s)
                .collect(Collectors.toList());
    }
    
    // CRUD methods can be added here
    public void tambahSurat(Surat surat) {
        semuaSurat.add(surat);
    }
}
