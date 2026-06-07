package service;

import model.Surat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Service for sorting operations.
 */
public class SortService {
    
    /**
     * Sorts a list of Surat by Date in ascending order.
     * @param suratList List of letters to sort
     */
    public static void sortByDateAsc(List<Surat> suratList) {
        Collections.sort(suratList, Comparator.comparing(Surat::getTanggal));
    }

    /**
     * Sorts a list of Surat by Date in descending order.
     * @param suratList List of letters to sort
     */
    public static void sortByDateDesc(List<Surat> suratList) {
        Collections.sort(suratList, (s1, s2) -> s2.getTanggal().compareTo(s1.getTanggal()));
    }
}
