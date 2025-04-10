package uf12addressapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private static final String PATRO_DATA = "dd.MM.yyyy";
    private static final DateTimeFormatter FORMATEJADOR_DATA
            = DateTimeFormatter.ofPattern(PATRO_DATA);

    public static String format(LocalDate data) {
        if (data == null) {
            return null;
        }
        return FORMATEJADOR_DATA.format(data);
    }

    public static LocalDate parse(String data) {
        try {
            return FORMATEJADOR_DATA.parse(data, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validDate(String data) {
        return DateUtil.parse(data) != null;
    }

}
