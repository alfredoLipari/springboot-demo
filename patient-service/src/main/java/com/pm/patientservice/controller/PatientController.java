package com.pm.patientservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;




@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Patient management endpoints")
public class PatientController {

    private final PatientService patientService = new PatientService(null);


    @GetMapping
    @Operation(summary = "Get all patients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patients returned successfully")
    })
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }
    
    @PostMapping
    @Operation(summary = "Create a patient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<PatientResponseDTO> postMethodName(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
        @ApiResponse(responseCode = "404", description = "Patient not found"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<PatientResponseDTO> putMethodName(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> putMethodName(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
