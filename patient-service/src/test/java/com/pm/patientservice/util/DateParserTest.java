package com.pm.patientservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.pm.patientservice.exception.InvalidDateFormatException;

class DateParserTest {

    @Test
    void parsesIsoFormat() {
        LocalDate date = DateParser.parseFlexibleDate("2000-10-15");
        assertEquals(LocalDate.of(2000, 10, 15), date);
    }

    @Test
    void parsesItalianFormat() {
        LocalDate date = DateParser.parseFlexibleDate("15-10-2000");
        assertEquals(LocalDate.of(2000, 10, 15), date);
    }

    @Test
    void parsesUsFormat() {
        LocalDate date = DateParser.parseFlexibleDate("10-15-2000");
        assertEquals(LocalDate.of(2000, 10, 15), date);
    }

    @Test
    void rejectsAmbiguousFormat() {
        assertThrows(InvalidDateFormatException.class, () -> DateParser.parseFlexibleDate("05-06-2000"));
    }
}
