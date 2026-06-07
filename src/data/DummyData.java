package data;

import model.Surat;
import model.SuratKeluar;
import model.SuratMasuk;
import utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to generate dummy data for testing purposes.
 */
public class DummyData {

    /**
     * Generates a list of dummy letters (10 incoming, 10 outgoing).
     * @return List of Surat
     */
    public static List<Surat> generateData() {
        List<Surat> data = new ArrayList<>();
        
        // Generate 10 Surat Masuk
        for (int i = 1; i <= 10; i++) {
            data.add(new SuratMasuk(
                "SM" + i,
                "00" + i + "/SM/VI/2026",
                DateUtil.stringToDate("0" + (i % 9 + 1) + "/06/2026"), // dummy dates
                "Perihal Surat Masuk " + i,
                "Pengirim " + i
            ));
        }

        // Generate 10 Surat Keluar
        for (int i = 1; i <= 10; i++) {
            data.add(new SuratKeluar(
                "SK" + i,
                "00" + i + "/SK/VI/2026",
                DateUtil.stringToDate("0" + (i % 9 + 1) + "/06/2026"),
                "Perihal Surat Keluar " + i,
                "Penerima " + i
            ));
        }

        return data;
    }
}
