package com.example.clinicapplication.controllers;

import com.example.clinicapplication.models.Patient;
import com.example.clinicapplication.models.Trial;
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
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @GetMapping("/eligible/{trialId}")
    public List<Patient> findEligiblePatients(@PathVariable Long trialId) {
        return List.of();
    }
}
