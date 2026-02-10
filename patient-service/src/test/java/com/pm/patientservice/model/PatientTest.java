package com.pm.patientservice.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PatientTest {

    @Test
    void prePersistSetsRegisteredDateWhenMissing() {
        Patient patient = new Patient();
        patient.onCreate();
        assertNotNull(patient.getRegisteredDate());
    }

    @Test
    void prePersistKeepsProvidedRegisteredDate() {
        LocalDate expectedDate = LocalDate.of(2024, 1, 10);
        Patient patient = new Patient();
        patient.setRegisteredDate(expectedDate);

        patient.onCreate();

        assertEquals(expectedDate, patient.getRegisteredDate());
    }
}
