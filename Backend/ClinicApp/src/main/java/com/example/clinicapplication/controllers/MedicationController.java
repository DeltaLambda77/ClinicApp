package com.example.clinicapplication.controllers;

import com.example.clinicapplication.models.Medication;
import com.example.clinicapplication.services.MedicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<Medication> getAllMedications() {
        System.out.println("GET /api/medications called");
        return medicationService.getAllMedications();
    }
}