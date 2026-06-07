package service;

import model.Surat;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for filtering operations.
 */
public class FilterService {

    /**
     * Filters letters by keyword in 'perihal' or 'nomorSurat'.
     * @param suratList List to filter
     * @param keyword The search keyword
     * @return Filtered list
     */
    public static List<Surat> filterByKeyword(List<Surat> suratList, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return suratList;
        }
        String lowerKeyword = keyword.toLowerCase();
        return suratList.stream()
                .filter(s -> s.getPerihal().toLowerCase().contains(lowerKeyword) || 
                             s.getNomorSurat().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}
