package com.jamestiotio.sentienterprize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;

public class UnitTestUtils {
    // Adapted from https://stackoverflow.com/a/20232680/10243394
    public static boolean isValidDateFormat(String format, String value, Locale locale) {
        LocalDateTime ldt;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale).withResolverStyle(ResolverStyle.SMART);

        try {
            ldt = LocalDateTime.parse(value, formatter);
            String result = ldt.format(formatter);

            return result.equals(value);
        }
        catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, formatter);
                String result = ld.format(formatter);

                return result.equals(value);
            }
            catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, formatter);
                    String result = lt.format(formatter);

                    return result.equals(value);
                }
                catch (DateTimeParseException e2) {
                    // Debugging purposes
                    e2.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean isValidTransactionCodeFormat(String value) {
        boolean matchFound = Pattern.compile("[A-Z0-9]{5}").matcher(value).find();

        return (value.length() == 5) && matchFound;
    }
}