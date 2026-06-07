package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for handling Date formatting and parsing.
 */
public class DateUtil {
    private static final String DEFAULT_FORMAT = "dd/MM/yyyy";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);

    /**
     * Converts a Date object to String.
     * @param date The date to format
     * @return Formatted date string
     */
    public static String dateToString(Date date) {
        if (date == null) return "";
        return formatter.format(date);
    }

    /**
     * Parses a string to Date object.
     * @param dateString The string to parse
     * @return Date object, or null if parsing fails
     */
    public static Date stringToDate(String dateString) {
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
