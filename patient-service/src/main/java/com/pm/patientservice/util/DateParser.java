package com.pm.patientservice.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import com.pm.patientservice.exception.InvalidDateFormatException;

public final class DateParser {

    private static final String SUPPORTED_FORMATS_MESSAGE = "Invalid date format. Supported formats are "
            + "yyyy-MM-dd (recommended), dd-MM-yyyy, dd/MM/yyyy, MM-dd-yyyy, MM/dd/yyyy.";

    private static final String AMBIGUOUS_FORMAT_MESSAGE = "Ambiguous date format. Use yyyy-MM-dd for dates where "
            + "both day and month are 12 or less.";

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter IT_DASH_FORMATTER = DateTimeFormatter.ofPattern("d-M-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter IT_SLASH_FORMATTER = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter US_DASH_FORMATTER = DateTimeFormatter.ofPattern("M-d-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter US_SLASH_FORMATTER = DateTimeFormatter.ofPattern("M/d/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private DateParser() {
    }

    public static LocalDate parseFlexibleDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            throw new InvalidDateFormatException("Date of birth is required.");
        }

        String date = rawDate.trim();

        if (date.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parse(date, ISO_FORMATTER);
        }

        if (!date.matches("^\\d{1,2}[-/]\\d{1,2}[-/]\\d{4}$")) {
            throw new InvalidDateFormatException(SUPPORTED_FORMATS_MESSAGE);
        }

        String separator = date.contains("/") ? "/" : "-";
        String[] parts = date.split(separator);
        int first = parseNumericPart(parts[0]);
        int second = parseNumericPart(parts[1]);

        if (first <= 12 && second <= 12) {
            throw new InvalidDateFormatException(AMBIGUOUS_FORMAT_MESSAGE);
        }

        boolean italianFormat = first > 12;

        if (italianFormat) {
            return parse(date, "/".equals(separator) ? IT_SLASH_FORMATTER : IT_DASH_FORMATTER);
        }

        return parse(date, "/".equals(separator) ? US_SLASH_FORMATTER : US_DASH_FORMATTER);
    }

    private static int parseNumericPart(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new InvalidDateFormatException(SUPPORTED_FORMATS_MESSAGE);
        }
    }

    private static LocalDate parse(String value, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(value, formatter);
        } catch (DateTimeParseException ex) {
            throw new InvalidDateFormatException(SUPPORTED_FORMATS_MESSAGE);
        }
    }
}
