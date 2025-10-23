package com.example.clinicapplication.controllers;

import com.example.clinicapplication.dto.PatientDTO;
import com.example.clinicapplication.models.Patient;
import com.example.clinicapplication.services.PatientService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        System.out.println("GET /api/patients called");
        List<PatientDTO> patients = patientService.getAllPatients();
        System.out.println("Returning " + patients.size() + " patients");
        return patients;
    }

    @PostMapping
    public PatientDTO addPatient(@RequestBody Patient patient) {
        System.out.println("POST /api/patients called");
        System.out.println("Received: " + patient.getFirstName() + " " + patient.getLastName());
        PatientDTO saved = patientService.addPatient(patient);
        System.out.println("Saved with ID: " + saved.getPatientId());
        return saved;
    }

    @GetMapping("/count")
    public String countPatients() {
        return "Total patients: " + patientService.getAllPatients().size();
    }
}